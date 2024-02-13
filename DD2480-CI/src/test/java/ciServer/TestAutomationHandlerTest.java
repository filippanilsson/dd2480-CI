package ciServer;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAutomationHandlerTest {
    HttpServletRequest request;
    TestAutomationHandler testAutomationHandler;

    String testFilePath;
    BuildHistoryManager buildHistoryManager;

    /**
     * SETUP OF VALID MOCK PUSH EVENT HTTP REQUEST
     */
    @Before
    public void setupValid() {
        request = mock(HttpServletRequest.class);
        when(request.getHeader("X-GitHub-Event")).thenReturn("push");
        testFilePath = "buildHistoryTest.json";
        buildHistoryManager = new BuildHistoryManager(testFilePath);
    }

    /**
     * INVALID INPUT TESTS
     */
    @Test
    public void assertThatCloneFailThrowsException() {
        when(request.getParameter("payload")).thenReturn(
                "{\"ref\": \"test_commits\", " +
                        "\"repository\":{\"clone_url\": \"invalidURL\"}, " +
                        "\"commits\": [{\"id\": \"094eb282f230eeb40b7f35ca6e68bf8496287108\"}, {\"id\": \"testID2\"}]}");
        testAutomationHandler = new TestAutomationHandler(request,buildHistoryManager);
        assertThrows(RuntimeException.class, ()->{testAutomationHandler.runTests();});
        (new File(testFilePath)).delete();
    }

    /**
     * VALID INPUT TESTS
     */
    @Test
    public void assertThatValidInputDoesNotThrowError() {
        when(request.getParameter("payload")).thenReturn(
                "{\"ref\": \"issue/17\", " +
                        "\"repository\":{\"clone_url\": \"https://github.com/filippanilsson/dd2480-CI.git\"}, " +
                        "\"commits\": [{\"id\": \"127a44f543afaa3eb231138d6610cb08b75e1e0d\"}, {\"id\": \"testID2\"}]}");
        testAutomationHandler = new TestAutomationHandler(request,buildHistoryManager);
        assertDoesNotThrow(() -> testAutomationHandler.runTests());
    }

}