package skeleton;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Allows interaction with a git repo by cloning from a given URI
 */
public class GitRepo implements AutoCloseable {
    private Git git;
    private File repoDir;

    /**
     * Initalizes an instance of the class
     * @param cloneUrl the URI to clone from
     * @param branch the branch the repo should checkout
     * @throws GitAPIException
     */
    public GitRepo(String cloneUrl, String branch) throws GitAPIException {
        File repoDir = new File("repo");
        Git git = cloneRepo(cloneUrl, branch, repoDir);

        this.git = git;
        this.repoDir = repoDir;
    }

    /**
     * Internal function for cloning from a remote repository
     * @param cloneUrl the URI to clone from
     * @param branch the branch the repo should checkout
     * @param repoDir the folder to which the repo should be cloned
     * @return A Git object that offers an API to interact with the repo
     * @throws GitAPIException
     */
    private Git cloneRepo(String cloneUrl, String branch, File repoDir) throws GitAPIException {
        Git git = Git.cloneRepository()
            .setURI(cloneUrl)
            .setDirectory(repoDir)
            .setBranch(branch)
            .call();
        return git;
    }

    /**
     * Checkouts a given commit
     * @param sha the commit sha to checkout
     * @throws Exception
     */
    public void checkoutCommit(String sha) throws Exception {
        this.git.checkout().setName(sha).call();
    }

    /**
     * Closes the used resources
     * @throws Exception
     */
    public void close() throws Exception {
        if (this.git != null) {
            this.git.close();
        }
        deleteDirectory(this.repoDir);
    }

    /**
     * Internal function for recursively removing a directory
     * @param directory the directory to remove
     */
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    } 
                }
            }
            directory.delete();
        }
    }
}