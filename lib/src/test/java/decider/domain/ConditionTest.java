package decider.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConditionTest {
    @Test
    public void testAnd() {
        Condition condition = new Condition(true, "&&", true);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testOr() {
        Condition condition = new Condition(true, "||", false);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testEquals() {
        Condition condition = new Condition(false, "==", false);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testNotEquals() {
        Condition condition = new Condition(true, "!=", false);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testGreaterThan() {
        Condition condition = new Condition(10, ">", 5);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testGreaterThanOrEqual() {
        Condition condition = new Condition(10, ">=", 10);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testLessThan() {
        Condition condition = new Condition(5, "<", 10);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testLessThanOrEqual() {
        Condition condition = new Condition(5, "<=", 5);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testUnknown() {
        Condition condition = new Condition(5, "%", 5);
        assertFalse(condition.evaluate());
    }

    @Test
    public void testNumber() {
        Condition condition = new Condition(1.0, "<", 5.0);
        assertTrue(condition.evaluate());
    }

    @Test
    public void testBigDecimal() {
        Condition condition = new Condition(BigDecimal.valueOf(1), "<", BigDecimal.valueOf(5));
        assertTrue(condition.evaluate());
    }

    @Test
    public void testInvalidOperands() {
        Condition condition = new Condition(new Object(), "<", new Object());
        assertFalse(condition.evaluate());
    }
}