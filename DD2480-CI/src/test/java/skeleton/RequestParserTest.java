package skeleton;

import javax.servlet.http.HttpServletRequest;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestParserTest {

    HttpServletRequest request;
    RequestParser requestParser;

    /**
     * SETUP OF VALID MOCK PUSH EVENT HTTP REQUEST
     */
    @Before
    public void setupValid() {
        request = mock(HttpServletRequest.class);
        when(request.getHeader("X-GitHub-Event")).thenReturn("push");
        when(request.getParameter("payload")).thenReturn(
                "{\"ref\": \"refs/heads/test\", " +
                        "\"repository\":{\"clone_url\": \"https://github.com/test/test.git\"}, " +
                        "\"commits\": [{\"id\": \"testID1\"}, {\"id\": \"testID2\"}]}");
        requestParser = new RequestParser(request);
    }

    /**
     * VALID INPUT TESTS
     */
    @Test
    public void assertThatGetCloneInformationReturnsCorrectBranch() {
        assertEquals(requestParser.branch,"refs/heads/test");
    }

    @Test
    public void assertThatGetCloneInformationReturnsCorrectCloneURL() {
        assertEquals(requestParser.cloneURL,"https://github.com/test/test.git");
    }

    @Test
    public void assertThatGetCloneInformationReturnsCorrectCommitIDs() {
        assertEquals(requestParser.commitIDs.length,2);
        assertEquals(requestParser.commitIDs[0],"testID1");
        assertEquals(requestParser.commitIDs[1],"testID2");
    }

    @Test
    public void assertThatIsPushEventReturnsTrueForPushEvent() {
        assertTrue(RequestParser.isPushEvent(request));
    }

    @Test
    public void assertThatIsPushEventReturnsFalseForGitHubNonPushEvent() {
        when(request.getHeader("X-GitHub-Event")).thenReturn("other");
        assertFalse(RequestParser.isPushEvent(request));
    }

    @Test
    public void assertThatIsPushEventReturnsFalseForNonGitHubEvent() {
        when(request.getHeader("X-GitHub-Event")).thenReturn(null);
        assertFalse(RequestParser.isPushEvent(request));
    }

    /**
     * INVALID INPUT TESTS
     */
    @Test
    public void assertThatGetCloneInformationNonPushEventThrowsException() {
        when(request.getHeader("X-GitHub-Event")).thenReturn(null);
        assertThrows(AssertionError.class, () -> {new RequestParser(request);});
    }


}
