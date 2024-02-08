import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenInvokerBuilder {
    private static final List<String> PUBLISH_GOALS = Arrays.asList("test", "install");
    private StringBuilder output = new StringBuilder();
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
    }

    /**
     * This method will be called repeatedly and a new generation will be initiated
     */
    public void build() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setInteractive(false);
        request.setGoals(PUBLISH_GOALS);
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

    /**
     * Set the output handler by calling the program.
     *
     * @param invoker Invoker
     */
    private void setOutput(Invoker invoker) {
        invoker.setOutputHandler(new InvocationOutputHandler() {
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