package decider.infra;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import decider.domain.Decision;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

public class IO {
    private static final Logger logger = CustomLogger.getInstance().logger;


    public static String ReadFile(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.readString(filePath);
    }


    public static void WriteFile(String content, String path) throws IOException {
        Path filePath = Path.of(path);
        Files.writeString(filePath, content);
    }

    public static Decision Deserialize(String json, Object[] bindings) throws JsonProcessingException {
        if (Objects.nonNull(bindings)) {
            json = String.format(json, bindings);
        } else {
            logger.fine("No bindings detected");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Decision.class);
    }

    public static String Serialize(Decision decision) throws JsonProcessingException {
        return Serialize(decision, new JsonFactory());
    }

    public static String Serialize(Decision decision, JsonFactory factory) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper(factory);
        return objectMapper.writeValueAsString(decision);
    }
}
