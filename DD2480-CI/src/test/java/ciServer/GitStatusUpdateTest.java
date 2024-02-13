package ciServer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GitStatusUpdate class
 * */
public class GitStatusUpdateTest {

    private GitStatusUpdate gitStatusUpdate;
    private BuildStatus buildStatus;

    @BeforeEach
    void setUp() {
        buildStatus = BuildStatus.SUCCESS;
        gitStatusUpdate = new GitStatusUpdate("5d3b4dc5b80c09cb27f13e1b6a846188dddc9cf0", buildStatus);
    }

    @Test
    public void assertThatValidInputDoesNotThrowError(){
        assertDoesNotThrow(() -> gitStatusUpdate.updateStatus());
    }


    @Test
    public void assertThatInvalidShaThrowsError(){
        gitStatusUpdate = new GitStatusUpdate("123", buildStatus);
        assertThrows(Error.class, () -> gitStatusUpdate.updateStatus());
    }

}
