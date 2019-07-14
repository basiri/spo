package com.abs.spo.controller.dto;



import com.abs.spo.controller.valdator.RoomValidity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;


/**
 * Input DTO for the controller
 * Has validity check
 * Room Validity : 1 < room-number<=100
 */
@RoomValidity
public class WorkforceRequestDTO {

    private int[]  rooms;
    @NotNull
    @Positive
    @Min(1)
    private int senior;
    @NotNull
    @PositiveOrZero
    private int junior;

    public WorkforceRequestDTO(int[] rooms, int senior, int junior) {
        this.rooms = rooms;
        this.senior = senior;
        this.junior = junior;
    }

    public int[] getRooms() {
        return rooms;
    }


    public int getSenior() {
        return senior;
    }


    public int getJunior() {
        return junior;
    }


    @Override
    public String toString() {
        return "WorkforceRequestDTO{" +
                "rooms=" + Arrays.toString(rooms) +
                ", senior=" + senior +
                ", junior=" + junior +
                '}';
    }
}
