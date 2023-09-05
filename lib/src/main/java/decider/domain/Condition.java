package decider.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

public class Condition {
    public final Object left;
    public final String operator;
    public final Object right;
    private final Callable<Boolean> evaluator;


    @Contract(pure = true)
    public Condition(@JsonProperty("left") Object left, @JsonProperty("operator") @NotNull String operator, @JsonProperty("right") Object right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        switch (operator) {
            case "&&" -> this.evaluator = () -> ((Boolean) left) && ((Boolean) right);
            case "||" -> this.evaluator = () -> ((Boolean) left) || ((Boolean) right);
            case "==" -> this.evaluator = () -> left.equals(right);
            case "!=" -> this.evaluator = () -> !left.equals(right);
            case "<" -> this.evaluator = () -> getBigDecimal(left).compareTo(getBigDecimal(right)) < 0;
            case "<=" -> this.evaluator = () -> getBigDecimal(left).compareTo(getBigDecimal(right)) <= 0;
            case ">" -> this.evaluator = () -> getBigDecimal(left).compareTo(getBigDecimal(right)) > 0;
            case ">=" -> this.evaluator = () -> getBigDecimal(left).compareTo(getBigDecimal(right)) >= 0;
            default -> this.evaluator = () -> false;
        }
    }

    private @NotNull BigDecimal getBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        } else if (value instanceof String) {
            return new BigDecimal((String) value);
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else {
            return BigDecimal.valueOf(0);
        }
    }

    public Boolean evaluate() {
        try {
            return this.evaluator.call();
        } catch (Exception e) {
            return false;
        }
    }
}