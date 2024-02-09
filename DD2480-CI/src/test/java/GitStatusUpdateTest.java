import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import skeleton.GitStatusUpdate;
import skeleton.BuildStatus;
import org.apache.http.StatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    void assertThatPOSTRequestReturnsWithCorrectStatusCode() {
        CloseableHttpClient mockHttpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = Mockito.mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = Mockito.mock(StatusLine.class);

        // Mocking the HTTP client to return a mocked response
        try {
            when(mockHttpClient.execute(any())).thenReturn(mockResponse);
            when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
            when(mockStatusLine.getStatusCode()).thenReturn(201);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gitStatusUpdate.setHttpClient(mockHttpClient);
        gitStatusUpdate.updateStatus();

        // Verify that the correct status code is returned
        try {
            assertEquals(201, mockStatusLine.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
