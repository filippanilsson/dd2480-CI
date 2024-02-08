package mavenBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.shared.invoker.*;

public class MavenInvokerBuilder {
    private static final List<String> PUBLISH_GOALS = Arrays.asList("clean","package","-DskipTests");
    private final StringBuilder output = new StringBuilder();
    ;
    /**
     * Define a field for the calling program instance.
     **/
    private final Invoker invoker;

    /**
     * Instantiating the calling program in the class constructor
     **/
    public MavenInvokerBuilder(File localRepositoryDir) {
        this.invoker = new DefaultInvoker();
        this.invoker.setLocalRepositoryDirectory(localRepositoryDir);
        String mavenHomePath = System.getenv("MAVEN_HOME");

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
     */
    public void build() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setRecursive(false);
        request.setGoals(PUBLISH_GOALS);
        request.setInputStream(InputStream.nullInputStream());
        setOutput(request);
        InvocationResult result = this.invoker.execute(request);
        if (result.getExitCode() != 0) {
            if (result.getExecutionException() != null) {
                throw new MavenInvocationException("error!",
                        result.getExecutionException());
            }
        }
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

    public String getOutput() {
        return output.toString();
    }
}