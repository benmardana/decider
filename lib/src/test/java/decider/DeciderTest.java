package decider;

import decider.domain.DataType;
import decider.infra.IO;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class DeciderTest {
    private final Object[] bindings = new Object[]{"abc", 2};
    String jsonString = "{\"branches\":[{\"conditions\":[{\"left\":\"%1$s\",\"operator\":\"==\",\"right\":\"abc\"},{\"left\":\"%2$s\",\"operator\":\">\",\"right\":4}],\"consequent\":true},{\"condition\":{\"left\":\"%2$s\",\"operator\":\"<\",\"right\":10},\"consequent\":true}],\"alternative\":false}";
    String jsonStringBound = "{\"branches\":[{\"conditions\":[{\"left\":\"abc\",\"operator\":\"==\",\"right\":\"abc\"},{\"left\":\"2\",\"operator\":\">\",\"right\":4}],\"consequent\":true},{\"condition\":{\"left\":\"2\",\"operator\":\"<\",\"right\":10},\"consequent\":true}],\"alternative\":false}";
    String samplePath = "src/test/resources/sample.json";
    String sampleYamlPath = "src/test/resources/sample.yaml";
    String outputPath = "src/test/resources/output.json";
    String rewritePath = "src/test/resources/rewrite.json";

    @Test
    public void evaluate() throws IOException {
        assertEquals(true, Decider.Evaluate(samplePath, bindings));
    }

    @Test
    public void readJsonToJsonString() throws IOException {
        assertEquals(jsonString, Decider.Read(samplePath));
    }

    @Test
    public void readJsonToJsonStringWithBindings() throws IOException {
        assertEquals(jsonStringBound, Decider.Read(samplePath, bindings));
    }

    @Test
    public void readJsonToYamlString() throws IOException {
        String yaml = IO.ReadFile(sampleYamlPath);
        assertEquals(yaml, Decider.Read(samplePath, bindings, DataType.YAML));
    }

    @Test
    public void writeStringToFile() throws IOException {
        Files.deleteIfExists(Path.of(outputPath));
        Decider.Write(jsonStringBound, outputPath);
        assertEquals(jsonStringBound, Files.readString(Path.of(outputPath)));
        Files.deleteIfExists(Path.of(outputPath));
    }

    @Test
    public void rewriteJson() throws IOException {
        Files.deleteIfExists(Path.of(rewritePath));
        String json = Decider.Read(samplePath);
        CharSequence targetGreaterThan = "\"left\":\"%2$s\",\"operator\":\"<\",\"right\":10";
        CharSequence targetLessThan = "\"left\":\"%2$s\",\"operator\":\">\",\"right\":10";
        json = json.replace(targetGreaterThan, targetLessThan);
        Decider.Write(json, rewritePath);
        assertEquals(false, Decider.Evaluate(rewritePath, bindings));
        Files.deleteIfExists(Path.of(rewritePath));
    }
}