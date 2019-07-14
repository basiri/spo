package com.abs.spo.model;


/**
 * for each structure WorkForceAssignee indicates number of senior ans junior cleaners
 */
public class WorkforceAssignee {
    private int senior;
    private int junior;

    public WorkforceAssignee(int senior, int junior) {
        this.senior = senior;
        this.junior = junior;
    }
    public int getSenior() {
        return senior;
    }
    public int getJunior() {
        return junior;
    }

}
