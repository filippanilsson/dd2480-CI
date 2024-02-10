package skeleton;

import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Checks if an HTTP request represents a GitHub push event and extracts information needed for build and notification.
 */

public class RequestParser {

    String branch;
    String cloneURL;
    String[] commitIDs;

    /**
     * Parses the payload GitHub push event in the form of an HTTP request and extracts the branch, clone URL, and commit IDs..
     * @param request the push event HTTP request
     */
    public RequestParser(HttpServletRequest request) {
        assert(isPushEvent(request));
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonPayload = (JSONObject) parser.parse(request.getParameter("payload"));
            this.branch = jsonPayload.get("ref").toString();
            this.cloneURL = ((JSONObject)jsonPayload.get("repository")).get("clone_url").toString();
            JSONArray jsonCommits = (JSONArray)jsonPayload.get("commits");
            this.commitIDs = new String[jsonCommits.size()];
            for (int i = 0; i < jsonCommits.size(); i++) {
                this.commitIDs[i] = ((JSONObject)jsonCommits.get(i)).get("id").toString();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if an HTTP request represents a GitHub push event.
     * @param request an HTTP request
     * @return a boolean indicating whether the request is a push event
     */
    public static boolean isPushEvent(HttpServletRequest request) {
        String type = request.getHeader("X-GitHub-Event");
        return (type != null && type.equals("push"));
    }

}