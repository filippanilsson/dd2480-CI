package ciServer;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class GitRepoTest {
    /*
     *  VALID INPUT TESTS
     */
    @Test
    public void assertCloneValidRepo() {
        String cloneUrl = "https://github.com/filippanilsson/dd2480-decide.git";
        try (GitRepo gitRepo = new GitRepo(cloneUrl, "main")) {
            assertTrue(new File("repo", "src/tests/DecideTest.java").exists());
        } catch (Exception e) {
            fail("Got an exception");
        }
    }

    @Test
    public void assertCheckoutValidCommit() {
        String cloneUrl = "https://github.com/filippanilsson/dd2480-decide.git";
        String sha = "3b0e4a1695f5aaab05132f3bf7705c5cd63b32eb";
        try (GitRepo gitRepo = new GitRepo(cloneUrl, "main")) {
            gitRepo.checkoutCommit(sha);
            assertTrue(new File("repo", "src/tests/decideTest.java").exists());
        } catch (Exception e) {
            fail("Got an exception");
        }
    }

    @Test
    public void assertClose() {
        String cloneUrl = "https://github.com/filippanilsson/dd2480-decide.git";
        try {
            GitRepo gitRepo = new GitRepo(cloneUrl, "main");
            gitRepo.close();
            assertTrue(!(new File("repo").exists()));
        } catch (Exception e) {
            fail("Got an exception");
        }
    }

    /*
     *  INVALID INPUT TEST
     */
    @Test
    public void assertCloneInvalidRepoThrowsException() {
        String cloneUrl = "invalidURL";
        assertThrows(GitAPIException.class, () -> {try (GitRepo gitRepo = new GitRepo(cloneUrl, "main")) {}});

    }

    @Test
    public void assertCheckoutInvalidCommitThrowsException() {
        String cloneUrl = "https://github.com/filippanilsson/dd2480-decide.git";
        String sha = "invalidSha";
        try (GitRepo gitRepo = new GitRepo(cloneUrl, "main")) {
            assertThrows(GitAPIException.class, () -> {gitRepo.checkoutCommit(sha);});
        } catch (Exception e) {
            fail("Got an exception when cloning repo");
        }
    }
}
