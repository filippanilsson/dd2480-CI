package skeleton;

import mavenBuilder.MavenInvokerBuilder;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class TestAutomationHandler {
    String branch;
    String cloneURL;
    String[] commitIDs;
    String sha;


    /**
     * Creates and initializes a TestAutomationHandler object.
     *
     * @param request the Github POST request
     */
    public TestAutomationHandler(HttpServletRequest request) {
        RequestParser requestParser = new RequestParser(request);
        this.branch = requestParser.branch;
        this.cloneURL = requestParser.cloneURL;
        this.commitIDs = requestParser.commitIDs;
        this.sha = this.commitIDs[0];
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
            try {
                mavenInvokerBuilder.build();
            } catch (MavenInvocationException e) {
                testUpdateStatus(BuildStatus.ERROR);
                throw new RuntimeException(e);
            }
            if(mavenInvokerBuilder.getBuildResult()) {
                testUpdateStatus(BuildStatus.SUCCESS);
            } else {
                testUpdateStatus(BuildStatus.FAILURE);
            }
            gitRepo.close();
        } catch (Exception e) {
            testUpdateStatus(BuildStatus.ERROR);
            throw new RuntimeException(e);
        }
    }

    private void testUpdateStatus(BuildStatus buildStatus) {
        GitStatusUpdate gitStatusUpdate = new GitStatusUpdate(this.sha, buildStatus);
        gitStatusUpdate.updateStatus();
    }

}
