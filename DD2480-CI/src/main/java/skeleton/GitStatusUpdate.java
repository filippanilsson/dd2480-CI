package skeleton;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
     *
     * @param sha         The SHA id for the commit
     * @param buildStatus The status of the finished build
     */
    public GitStatusUpdate(String sha, BuildStatus buildStatus) {
        this.sha = sha;
        this.buildStatus = buildStatus;
        this.gitUri = "https://api.github.com/";
        this.githubToken = "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";
    }

    /**
     * Sets the status of the commit by changing values of the description, status and context.
     *
     * @param json The JSON object that contains the values to be sent to the GitHub API
     */
    private JSONObject statusSetter(JSONObject json) {
        switch (buildStatus) {
            case SUCCESS:
                json.put("description", "Build success");
            case FAILURE:
                json.put("description", "Build failed");
            case PENDING:
                json.put("description", "Build pending");
            default:
                json.put("description", "Build error");
        }
        json.put("state", buildStatus.toString());
        json.put("context", "Group 12");

        return json;
    }

    /**
     * Creates a HTTP POST request to update the commit status.
     * Throws an exception if the POST request fails.
     */
    public void updateStatus(HttpServletResponse servletResponse) throws IOException {
        JSONObject json = statusSetter(new JSONObject());

        HttpClient httpClient = HttpClients.createDefault();
        String apiUrl = gitUri + "repos/filippanilsson/dd2480-CI/statuses/" + sha;

        HttpPost httpPost = new HttpPost(apiUrl);

        httpPost.setHeader("Authorization", "Bearer " + githubToken);
        httpPost.setHeader("Content-Type", "application/json");

        HttpResponse response;
        try {
            StringEntity entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            System.out.println("Response status code: " + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        servletResponse.getWriter().println("Status updated");
    }

}


