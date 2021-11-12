package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    private int ID;
    private int arrivalTime;
    private int processingTime;
    private int finalTime;

    public Task(int id, int arrivalTime, int processingTime) {
        this.setID(id);
        this.setArrivalTime(arrivalTime);
        this.setProcessingTime(processingTime);
    }

    @Override
    public String toString() {
        return "(" +
                +ID + ","
                + arrivalTime + ","
                + processingTime +
                ") ";
    }
}
