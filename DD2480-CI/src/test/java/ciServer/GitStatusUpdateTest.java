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

    /**
     * SETUP OF VALID MOCK POST REQUEST
     */
    @BeforeEach
    void setUp() {
        buildStatus = BuildStatus.SUCCESS;
        gitStatusUpdate = new GitStatusUpdate("840b1ffb9438a01fa5ec6f54b670b9222d2c8f0b", buildStatus);
    }

    /*
     *  VALID INPUT TEST
     */
    @Test
    public void assertThatValidInputDoesNotThrowError(){
        assertDoesNotThrow(() -> gitStatusUpdate.updateStatus());
    }

    /*
     *  INVALID INPUT TEST
     */
    @Test
    public void assertThatInvalidShaThrowsError(){
        gitStatusUpdate = new GitStatusUpdate("123", buildStatus);
        assertThrows(Error.class, () -> gitStatusUpdate.updateStatus());
    }

}
