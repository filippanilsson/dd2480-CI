package skeleton;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.net.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Updates the commit status on the repository to one of four keywords - success, failure, error, and pending
 * by connecting to the GitHub API
 * */
public class GitStatusUpdate {
    private final BuildStatus buildStatus;
    private final String gitUri;
    private final String sha;
    private final String githubToken;
    private CloseableHttpClient httpClient; // Added member variable for CloseableHttpClient


    /**
     * Constructor for the GitStatusUpdate class
     * @param sha The SHA id for the commit
     * @param buildStatus The status of the finished build
     * */
    public GitStatusUpdate( String sha, BuildStatus buildStatus) {
        this.sha = sha;
        this.buildStatus = buildStatus;
        this.gitUri = "https://api.github.com/";
        this.githubToken = System.getenv("GITHUB_TOKEN");
    }

    /**
     * Sets the status of the commit by changing values of the description, status and context.
     * @param json The JSON object that contains the values to be sent to the GitHub API
     * */
    private void statusSetter(JSONObject json) {
        switch (buildStatus){
            case SUCCESS:
                json.put("description", "Build success");
            case FAILURE:
                json.put("description", "Build failed");
            case PENDING:
                json.put("description", "Build pending");
            default:
                json.put("description", "Build error");
        }
        json.put("status", buildStatus.toString());
        json.put("context", "Group 12");
    }

    // Added setter method for CloseableHttpClient
    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Creates a HTTP POST request to update the commit status.
     * Throws an exception if the POST request fails.
     */
    public void updateStatus(){
        JSONObject json = new JSONObject();
        statusSetter(json);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(gitUri + "repos/filippanilsson/dd2480-CI/statuses/" + sha);
            StringEntity params = new StringEntity(json.toString());
            httpPost.addHeader("accept", " application/vnd.github+json");
            httpPost.addHeader("Authorization", "token " + githubToken);
            httpPost.setEntity(params);
            httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new JSONException("Error", e);
        }
    }
}


