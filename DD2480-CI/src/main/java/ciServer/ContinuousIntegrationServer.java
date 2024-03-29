package ciServer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler
{
    private final BuildHistoryManager buildHistoryManager = new BuildHistoryManager(System.getProperty("user.dir")+"/src/main/resources/buildHistory.json");

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        if (target.equals("/") && RequestParser.isPushEvent(request)) {
            //Handle Webhook request
            System.out.println("Handling Webhook request...");
            TestAutomationHandler testAutomationHandler = new TestAutomationHandler(request, buildHistoryManager);
            testAutomationHandler.runTests();
            System.out.println("...done");
        }
        else if (target.equals("/buildhistory")) {
            //Handle full build log request
            response.getWriter().println(buildHistoryManager.getFullBuildHistory(request.getRequestURL().toString()));
        }
        else if (target.startsWith("/buildhistory")) {
            //Handle single build log request
            response.getWriter().println(buildHistoryManager.getBuildInfoBySHA(target.split("/")[2]));
        }

    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8012);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}