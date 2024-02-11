package mavenBuilderTest;

import mavenBuilder.MavenInvokerBuilder;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Test;

import java.nio.file.Paths;
public class MavenInvokerBuilderTest {
    @Test
    public void build() throws MavenInvocationException {
        MavenInvokerBuilder mavenInvokerBuilder = new MavenInvokerBuilder(Paths.get(".").toFile());
        mavenInvokerBuilder.build();
        String output = mavenInvokerBuilder.getOutput();
        System.out.println(output);
    }
}
