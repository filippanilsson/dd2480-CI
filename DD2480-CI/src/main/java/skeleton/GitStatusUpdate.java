package skeleton;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;


/**
 * Updates the commit status on the repository to one of four keywords - success, failure, error, and pending
 * by connecting to the GitHub API.
 * */
public class GitStatusUpdate {
    private final BuildStatus buildStatus;
    private final String gitUri;
    private final String sha;
    private final String githubToken;


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
        this.githubToken = System.getenv("ci_token"); // make sure this works
    }

    /**
     * Sets the status of the commit by changing values of the description, status and context.
     *
     * @param json The JSON object that contains the values to be sent to the GitHub API
     * @return A JSON object containing the commit status and other information
     */
    private JSONObject statusSetter(JSONObject json) {

        switch (buildStatus) {
            case SUCCESS:
                json.put("description", "Build succeeded");
                break;
            case FAILURE:
                json.put("description", "Build failed");
                break;
            case PENDING:
                json.put("description", "Build pending");
                break;
            default:
                json.put("description", "Build error");
                break;
        }
        json.put("state", buildStatus.toString());
        json.put("context", "dd2480-CI");

        return json;
    }

    /**
     * Creates a HTTP POST request to update the commit status.
     * Throws an exception if the POST request fails.
     * @param servletResponse The response that we send a message to if the API POST request succeeded
     */
    public void updateStatus(HttpServletResponse servletResponse)  {
        try {
            JSONObject json = statusSetter(new JSONObject());

            HttpClient httpClient = HttpClients.createDefault();
            String apiUrl = gitUri + "repos/filippanilsson/dd2480-CI/statuses/" + sha;

            HttpPost httpPost = new HttpPost(apiUrl);

            httpPost.setHeader("Authorization", "Bearer " + githubToken);
            httpPost.setHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);

            servletResponse.getWriter().println("Response status code: " + response.getStatusLine().getStatusCode() + "\n");
            servletResponse.getWriter().println("Status updated \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


