package decider.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public record Decision(List<Branch> branches, Object alternative) {

    public Decision(
            @JsonProperty("branches") List<Branch> branches,
            @JsonProperty("alternative") Object alternative
    ) {
        this.branches = branches;
        this.alternative = alternative;
    }

    public Object evaluate() {
        return this.branches.stream()
                .map(Branch::evaluate)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(this.alternative);
    }
}
