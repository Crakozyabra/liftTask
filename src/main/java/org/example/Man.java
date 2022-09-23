package org.example;

public class Man {
    long targetFloor;
    public Man(long targetFloor) {
        this.targetFloor = targetFloor;
    }

    public Direction getTargetDirectionInBuilding(long factFloor){
        return targetFloor-factFloor == 0 ? Direction.NONE : (targetFloor-factFloor > 0 ? Direction.UP : Direction.DOWN);
    }

}
