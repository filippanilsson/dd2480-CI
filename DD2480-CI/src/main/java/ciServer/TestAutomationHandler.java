package ciServer;

import org.apache.maven.shared.invoker.MavenInvocationException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;

/**
 Executing the automated tests of the commit on the branch where the change has been made
 and send notification
 */
public class TestAutomationHandler {
    String branch;
    String cloneURL;
    String[] commitIDs;
    String sha;
    BuildHistoryManager buildHistoryManager;


    /**
     * Creates and initializes a TestAutomationHandler object.
     *
     * @param request the Github request
     */
    public TestAutomationHandler(HttpServletRequest request, BuildHistoryManager buildHistoryManager) {
        RequestParser requestParser = new RequestParser(request);
        this.branch = requestParser.branch;
        this.cloneURL = requestParser.cloneURL;
        this.commitIDs = requestParser.commitIDs;
        this.sha = this.commitIDs[0];
        this.buildHistoryManager = buildHistoryManager;
    }

    /**
     * Run the tests from targeted maven directory
     */
    public void runTests() {
        testUpdateStatus(BuildStatus.PENDING);
        try {
            GitRepo gitRepo = new GitRepo(cloneURL, branch);
            gitRepo.checkoutCommit(this.sha);
            MavenInvokerBuilder mavenInvokerBuilder = new MavenInvokerBuilder(new File("repo"));
            BuildStatus buildResultStatus = BuildStatus.SUCCESS;
            try {
                mavenInvokerBuilder.build();
            } catch (MavenInvocationException e) {
                testUpdateStatus(BuildStatus.ERROR);
                buildHistoryManager.addBuildToHistory(this.sha, BuildStatus.ERROR, LocalDateTime.now(), mavenInvokerBuilder.getOutput());
                throw new RuntimeException(e);
            }
            if(!mavenInvokerBuilder.getBuildResult()) {
                buildResultStatus = BuildStatus.FAILURE;
            }
            testUpdateStatus(buildResultStatus);
            buildHistoryManager.addBuildToHistory(this.sha, buildResultStatus, LocalDateTime.now(), mavenInvokerBuilder.getOutput());
            gitRepo.close();
        } catch (Exception e) {
            testUpdateStatus(BuildStatus.ERROR);
            buildHistoryManager.addBuildToHistory(this.sha, BuildStatus.ERROR, LocalDateTime.now(), "build error");
            throw new RuntimeException(e);
        }
    }

    private void testUpdateStatus(BuildStatus buildStatus) {
        GitStatusUpdate gitStatusUpdate = new GitStatusUpdate(this.sha, buildStatus);
        gitStatusUpdate.updateStatus();
    }

}
