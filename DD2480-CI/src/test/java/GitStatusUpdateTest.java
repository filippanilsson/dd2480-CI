import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import skeleton.GitStatusUpdate;
import skeleton.BuildStatus;
import org.apache.http.StatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the GitStatusUpdate class
 * */
class GitStatusUpdateTest {

    private GitStatusUpdate gitStatusUpdate;
    private BuildStatus buildStatus;

    @BeforeEach
    void setUp() {
        buildStatus = BuildStatus.SUCCESS;
        gitStatusUpdate = new GitStatusUpdate("5d3b4dc5b80c09cb27f13e1b6a846188dddc9cf0", buildStatus);
    }

    @Test
    public void assertThatValidInputDoesNotThrowError(){
        assertDoesNotThrow(() ->gitStatusUpdate.updateStatus());
    }


    @Test
    public void assertThatInvalidShaThrowsError(){
        gitStatusUpdate = new GitStatusUpdate("123", buildStatus);
        assertThrows(Error.class, () -> gitStatusUpdate.updateStatus());
    }

    @Test
    public void assertInvalidGithubTokenThrowsError(){
        //TODO
    }
}
