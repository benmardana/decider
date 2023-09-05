package decider.domain;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DecisionTest {

    @Test
    public void evaluatesSingleBranch() {
        Branch branch = new Branch(List.of(new Condition(true, "==", true)), 10);
        Decision decision = new Decision(List.of(branch), 0);
        assertEquals(decision.evaluate(), 10);
    }

    @Test
    public void evaluatesSingleFailingBranch() {
        Branch branch = new Branch(List.of(new Condition(true, "==", false)), 10);
        Decision decision = new Decision(List.of(branch), 0);
        assertEquals(decision.evaluate(), 0);
    }

    @Test
    public void evaluatesMultipleBranches() {
        Branch branchOne = new Branch(List.of(new Condition(true, "==", false)), 10);

        Branch branchTwo = new Branch(List.of(new Condition(true, "==", true)), 20);
        Decision decision = new Decision(List.of(branchOne, branchTwo), 0);
        assertEquals(20, decision.evaluate());
    }
}