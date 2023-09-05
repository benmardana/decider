package decider.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import decider.infra.CustomLogger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Branch(List<Condition> conditions, Condition condition, Object consequent) {
    private static final Logger logger = CustomLogger.getInstance().logger;

    public Branch(@JsonProperty("conditions") List<Condition> conditions, @JsonProperty("condition") Condition condition, @JsonProperty("consequent") Object consequent) {
        this.conditions = conditions;
        this.condition = condition;
        this.consequent = consequent;
    }

    public Branch(List<Condition> conditions, Object consequent) {
        this(conditions, null, consequent);
    }

    public Branch(Condition condition, Object consequent) {
        this(null, condition, consequent);
    }

    @Nullable
    public Object evaluate() {
        Consumer<Condition> ConditionLogger = condition -> logger.fine(String.format("Evaluating: %s %s %s", condition.left, condition.operator, condition.right));
        if (Objects.nonNull(condition)) {
            logger.fine("Evaluating single clause...");
            ConditionLogger.accept(condition);
            if (condition.evaluate()) {
                logger.fine("Truthy evaluation");
                return consequent;
            }
        }
        if (Objects.nonNull(conditions)) {
            logger.fine("Evaluating set of clauses...");
            if (conditions.stream()
                    .peek(ConditionLogger)
                    .allMatch(Condition::evaluate)) {

                logger.fine("Truthy evaluation");
                return consequent;
            }
        }
        logger.fine("No truthy evaluation found in branch");
        return null;
    }
}