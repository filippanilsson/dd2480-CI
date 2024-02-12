package skeleton;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Manages the build history of a continuous integration server
 */
public class BuildHistoryManager {

    private File buildHistoryFile;
    private JSONArray buildEntries;

    public BuildHistoryManager(String filePath) {
        load(filePath);
    }

    /**
     * Creates the history and loads any existing entries already in the history file.
     * @param filePath the path of the history file
     */
    public void load(String filePath) {
        buildHistoryFile = new File(filePath);
        if (!buildHistoryFile.exists()) {
            try {
                buildHistoryFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            buildEntries = new JSONArray();
        }
        else if (buildHistoryFile.length() == 0) {
            buildEntries = new JSONArray();
        }
        else {
            try (FileReader reader = new FileReader(buildHistoryFile)) {
                JSONParser parser = new JSONParser();
                buildEntries = (JSONArray) parser.parse(reader);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a new build to the history.
     * @param sha the commit SHA of the build
     * @param buildStatus the status of the build
     * @param timestamp the timestamp of the build
     * @param buildLogs the build logs
     */
    public void addBuildToHistory(String sha, BuildStatus buildStatus, LocalDateTime timestamp, String buildLogs) {
        JSONObject json = new JSONObject();
        json.put("Sha", sha);
        json.put("Timestamp", timestamp.toString());
        json.put("Status", buildStatus.toString());
        json.put("Logs", buildLogs);
        buildEntries.add(json);
        try (FileWriter writer = new FileWriter(buildHistoryFile)) {
            writer.write(buildEntries.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the full list of URLs for builds in the history.
     * @return a String build list containing build list encoded in HTML
     */
    public String getFullBuildHistory(String buildHistoryUrl) {
        StringBuilder s = new StringBuilder();
        s.append("<b>BUILD HISTORY</b><br>");
        for (Object entry : buildEntries) {
            String buildUrl = buildHistoryUrl + "/" + ((JSONObject)entry).get("Sha").toString();;
            s.append("<a href=\"");
            s.append(buildUrl);
            s.append("\">");
            s.append(buildUrl);
            s.append("</a><br>");
        }
        return s.toString();
    }

    /**
     * Gets the build information with a given commit SHA from the history, if it exists.
     * @param sha the commit SHA of the desired build
     * @return a String containing the build information encoded in HTML
     */
    public String getBuildInfoBySHA(String sha) {
        for (Object entry : buildEntries) {
            JSONObject json = (JSONObject) entry;
            if (json.get("Sha").toString().equals(sha)) {
                StringBuilder s = new StringBuilder();
                s.append("<b>COMMIT SHA:</b> ");
                s.append(json.get("Sha").toString());
                s.append("<br><b>STATUS:</b> ");
                s.append(json.get("Status").toString());
                s.append("<br><b>TIMESTAMP:</b> ");
                s.append(json.get("Timestamp").toString());
                s.append("<br><b>BUILD LOGS:</b> ");
                s.append(json.get("Logs").toString());
                return s.toString();
            }
        }
        return "";
    }

}
