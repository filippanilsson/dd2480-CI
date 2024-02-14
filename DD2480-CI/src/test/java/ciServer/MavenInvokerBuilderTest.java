package ciServer;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MavenInvokerBuilderTest {
    /*
     *  BUILD FUNCTION TEST
     */
    @Test
    public void build() throws MavenInvocationException {
        Path p = Paths.get(System.getProperty("user.dir")+"/src/test/resources/buildTestProject");
        MavenInvokerBuilder mavenInvokerBuilder = new MavenInvokerBuilder(p.toFile());
        mavenInvokerBuilder.build();
        assertTrue(mavenInvokerBuilder.getBuildResult());
    }

    @After
    public void deleteDirectory() throws IOException {
        File file = new File(System.getProperty("user.dir")+"/repo");
        if (file.exists()) file.delete();
    }
}