package ciServer;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.maven.shared.invoker.*;

/**
 * execute the build for maven repo, and retrieve the build output and result status.
 */
public class MavenInvokerBuilder {
    private static final List<String> PUBLISH_GOALS = Collections.singletonList("test");
    private final StringBuilder output = new StringBuilder();
    private boolean buildResult = false;
    private File localRepositoryDir;
    /**
     * Define a field for the calling program instance.
     **/
    private final Invoker invoker;

    /**
     * Instantiating the calling program in the class constructor
     *
     * @param localRepositoryDir The local repository directory for the Maven build.
     **/
    public MavenInvokerBuilder(File localRepositoryDir) {
        this.localRepositoryDir = localRepositoryDir;
        this.invoker = new DefaultInvoker();
        this.invoker.setLocalRepositoryDirectory(localRepositoryDir);
        String mavenHomePath = System.getenv("M2_HOME");

        if (mavenHomePath != null) {
            File mavenHome = new File(mavenHomePath);
            this.invoker.setMavenHome(mavenHome);
        } else {
            // Handle the case where "MAVEN_HOME" is not set
            System.err.println("Error: MAVEN_HOME environment variable is not set.");
        }
    }

    /**
     * This method will be called repeatedly and a new generation will be initiated
     *
     * @throws MavenInvocationException If an error occurs during Maven build invocation.
     */
    public void build() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setRecursive(false);
        request.setGoals(PUBLISH_GOALS);
        request.setBatchMode(false);
        request.setBaseDirectory(this.localRepositoryDir);
        setOutput(request);
        InvocationResult result = this.invoker.execute(request);
        if (result.getExitCode() != 0) {
            if (result.getExecutionException() != null) {
                throw new MavenInvocationException("error!",
                        result.getExecutionException());
            }
        }
        buildResult = (result.getExitCode() == 0);
    }

    /**
     * Sets the output handler by invoking the request.
     *
     * @param request InvocationRequest
     */
    private void setOutput(InvocationRequest request) {
        request.setOutputHandler(new InvocationOutputHandler() {
            @Override
            public void consumeLine(String line) {
                output.append(line).append(System.lineSeparator());
            }
        });
    }

    /**
     * Retrieves the captured output of the Maven build.
     *
     * @return A string containing the Maven build output.
     */
    public String getOutput() {
        return output.toString();
    }
    /**
     * Retrieves the result status of the Maven build.
     *
     * @return `true` if the build was successful, `false` otherwise.
     */
    public Boolean getBuildResult() {
        return buildResult;
    }
}