package decider.domain;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BranchTest {

    @Test
    public void evaluatesTruthyCondition() {
        Branch branch = new Branch(new Condition(true, "==", true), "Hello, world!");
        assertEquals(branch.evaluate(), "Hello, world!");
    }

    @Test
    public void evaluatesFalsyCondition() {
        Branch branch = new Branch(new Condition(true, "==", false), "Hello, world!");
        assertNull(branch.evaluate());
    }

    @Test
    public void evaluatesListOfTruthyConditions() {
        Branch branch = new Branch(List.of(new Condition(true, "==", true)), "Hello, world!");
        assertEquals(branch.evaluate(), "Hello, world!");
    }

    @Test
    public void evaluatesListIncludingFalseCondition() {
        Branch branch = new Branch(List.of(new Condition(true, "==", false)), "Hello, world!");
        assertNull(branch.evaluate());
    }
}