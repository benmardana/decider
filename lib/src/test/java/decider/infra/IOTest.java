package decider.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import decider.domain.Decision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IOTest {

    private final String jsonString = "{\"branches\":[{\"conditions\":[{\"left\":\"%1$s\",\"operator\":\"==\",\"right\":\"abc\"},{\"left\":\"%2$s\",\"operator\":\">\",\"right\":4}],\"consequent\":true},{\"conditions\":[{\"left\":\"%2$s\",\"operator\":\"<\",\"right\":10}],\"consequent\":true}],\"alternative\":false}";
    private final Object[] bindings = new Object[]{"abc", 2};

    @Test
    public void deserializeJson() throws JsonProcessingException {
        Decision decision = IO.Deserialize(jsonString, bindings);
        assertEquals(true, decision.evaluate());
    }

    @Test
    public void serializeJson() throws JsonProcessingException {
        Decision decision = IO.Deserialize(jsonString, bindings);
        assertEquals(String.format(jsonString, bindings), IO.Serialize(decision));
    }

    @Test
    public void serializeYaml() throws JsonProcessingException {
        Decision decision = IO.Deserialize(jsonString, new Object[]{"abc", 2});
        String yamlString = """
                ---
                branches:
                - conditions:
                  - left: "abc"
                    operator: "=="
                    right: "abc"
                  - left: "2"
                    operator: ">"
                    right: 4
                  consequent: true
                - conditions:
                  - left: "2"
                    operator: "<"
                    right: 10
                  consequent: true
                alternative: false
                """;
        assertEquals(yamlString, IO.Serialize(decision, new YAMLFactory()));
    }
}