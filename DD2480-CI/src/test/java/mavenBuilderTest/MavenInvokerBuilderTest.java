package mavenBuilderTest;

import mavenBuilder.MavenInvokerBuilder;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import skeleton.GitRepo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class MavenInvokerBuilderTest {
    /*
     *  BUILD FUNCTION TEST
     */
    @Test
    public void build() throws MavenInvocationException {
        Path p = Paths.get("/Users/liangtianning/IdeaProjects/test");
        MavenInvokerBuilder mavenInvokerBuilder = new MavenInvokerBuilder(p.toFile());
        mavenInvokerBuilder.build();
        assertTrue(mavenInvokerBuilder.getBuildResult());
    }
}
