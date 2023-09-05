package decider;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import decider.domain.DataType;
import decider.domain.Decision;
import decider.infra.CustomLogger;
import decider.infra.IO;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

public class Decider {
    private static final Logger logger = CustomLogger.getInstance().logger;

    private static Decision Parse(String path, Object[] bindings) throws IOException {
        logger.fine("Reading file from path: " + path);
        String content = IO.ReadFile(path);
        logger.fine("File read from path: " + path);

        logger.fine("Attempting deserialization...");
        Decision decision = IO.Deserialize(content, bindings);
        logger.fine("Deserialization successful");
        return decision;
    }

    public static String Read(String path, Object[] bindings, @NotNull DataType dataType) throws IOException {
        logger.info("Attempting read...");
        Decision decision = Parse(path, bindings);

        logger.fine("Serialization format: " + dataType);
        logger.fine("Attempting serialization...");
        if (dataType.equals(DataType.YAML)) {
            JsonFactory yamlFactory = YAMLFactory.builder()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .build();
            String result = IO.Serialize(decision, yamlFactory);
            logger.fine("Serialization successful");
            logger.info("Read successful");
            return result;
        }

        String result = IO.Serialize(decision);
        logger.fine("Serialization successful");
        logger.info("Read successful");
        return result;
    }

    public static String Read(String path, Object[] bindings) throws IOException {
        return Read(path, bindings, DataType.JSON);
    }

    public static String Read(String path) throws IOException {
        return Read(path, null, DataType.JSON);
    }

    public static void Write(String json, String path) throws IOException {
        logger.info("Attempting write...");
        IO.WriteFile(json, path);
        logger.info("Write successful");
    }

    public static Object Evaluate(String path, Object[] bindings) throws IOException {
        Decision decision = Parse(path, bindings);
        logger.info("Attempting evaluation...");
        Object result = decision.evaluate();
        logger.info("Evaluation successful");
        return result;
    }
}
