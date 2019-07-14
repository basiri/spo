package com.abs.spo.model;

import java.util.Arrays;

public class Gene {
    private WorkforceAssignee[] assignees;
    private int fitness;

    public Gene(WorkforceAssignee[] assignees, int fitness) {
        this.assignees = assignees;
        this.fitness = fitness;
    }

    public WorkforceAssignee[] getAssignees() {
        return assignees;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Gene{" +
                "assignees=" + Arrays.toString(assignees) +
                ", fitness=" + fitness +
                '}';
    }
}
