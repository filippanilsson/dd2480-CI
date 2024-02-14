package ciServer;

import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class BuildHistoryManagerTest {

    String testFilePath = "buildHistoryTest.json";
    String testBuildHistoryUrl = "http://test.url/buildhistory";

    /**
     * CORRECT FILE HANDLING TESTS
     */
    @Test
    public void assertThatNonexistentFilePathDoesNotThrowException(){
        File file = new File(testFilePath);
        if (file.exists()) file.delete();
        try {
            BuildHistoryManager buildHistoryManager = new BuildHistoryManager(testFilePath);
        }
        catch (Exception e) {
            fail();
        }
        file.delete();
    }

    @Test
    public void assertThatEmptyHistoryFileDoesNotThrowException() throws IOException {
        File file = new File(testFilePath);
        if (!file.exists()) file.createNewFile();
        try {
            BuildHistoryManager buildHistoryManager = new BuildHistoryManager(testFilePath);
        }
        catch (Exception e) {
            fail();
        }
        file.delete();
    }

    /**
     * CORRECT OUTPUT TESTS
     */
    @Test
    public void assertThatFullBuildHistoryManagerReturnsCorrectList() throws IOException {
        File file = new File(testFilePath);
        if (!file.exists()) file.createNewFile();
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(testFilePath);
        buildHistoryManager.addBuildToHistory("sha1", BuildStatus.SUCCESS, LocalDateTime.now(), "logs...");
        buildHistoryManager.addBuildToHistory("sha2", BuildStatus.FAILURE, LocalDateTime.now(), "logs...");
        buildHistoryManager.addBuildToHistory("sha3", BuildStatus.PENDING, LocalDateTime.now(), "logs...");
        assertEquals(buildHistoryManager.getFullBuildHistory(testBuildHistoryUrl),
                "<b>BUILD HISTORY</b><br>" +
                        "<a href=\"http://test.url/buildhistory/sha1\">http://test.url/buildhistory/sha1</a><br>" +
                        "<a href=\"http://test.url/buildhistory/sha2\">http://test.url/buildhistory/sha2</a><br>" +
                        "<a href=\"http://test.url/buildhistory/sha3\">http://test.url/buildhistory/sha3</a><br>");
        file.delete();
    }

    @Test
    public void assertThatGetBuildInfoBySHAReturnsCorrectBuild() throws IOException {
        File file = new File(testFilePath);
        if (!file.exists()) file.createNewFile();
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(testFilePath);
        LocalDateTime now = LocalDateTime.now();
        buildHistoryManager.addBuildToHistory("sha1", BuildStatus.SUCCESS, now, "logs...");
        assertEquals(buildHistoryManager.getBuildInfoBySHA("sha1"),
                "<b>COMMIT SHA:</b> sha1" +
                        "<br><b>STATUS:</b> success" +
                        "<br><b>TIMESTAMP:</b> " +
                        now.toString() +
                        "<br><b>BUILD LOGS:</b> logs...");
        file.delete();
    }

    @Test
    public void assertThatNewManagerLoadsFullBuildList() throws IOException {
        File file = new File(testFilePath);
        if (!file.exists()) file.createNewFile();
        BuildHistoryManager buildHistoryManager = new BuildHistoryManager(testFilePath);
        LocalDateTime now = LocalDateTime.now();
        buildHistoryManager.addBuildToHistory("sha1", BuildStatus.SUCCESS, now, "logs...");
        buildHistoryManager.addBuildToHistory("sha2", BuildStatus.FAILURE, LocalDateTime.now(), "logs...");
        buildHistoryManager.addBuildToHistory("sha3", BuildStatus.PENDING, LocalDateTime.now(), "logs...");
        BuildHistoryManager newBuildHistoryManager = new BuildHistoryManager(testFilePath);
        assertEquals(newBuildHistoryManager.getFullBuildHistory(testBuildHistoryUrl),
                "<b>BUILD HISTORY</b><br>" +
                        "<a href=\"http://test.url/buildhistory/sha1\">http://test.url/buildhistory/sha1</a><br>" +
                        "<a href=\"http://test.url/buildhistory/sha2\">http://test.url/buildhistory/sha2</a><br>" +
                        "<a href=\"http://test.url/buildhistory/sha3\">http://test.url/buildhistory/sha3</a><br>");
        file.delete();
    }

}