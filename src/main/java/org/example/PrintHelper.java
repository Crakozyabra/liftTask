package org.example;

public class PrintHelper {

    public static void printMenOnTheFloorInBuilding(Building building, int floorNumber, String message){
        System.out.println();
        System.out.println(message + " " + floorNumber + " floor:");
        for (Man man:building.getMenOnTheFloor(floorNumber)) {
            System.out.println("Man on the floor: target floor = " + man.getTargetFloor() + "; fact floor = " + floorNumber + "; direction = " + man.getTargetDirectionInBuilding(floorNumber));
        }
    }

    public static void printMenInTheLiftInBuilding(Building building){
        System.out.println();
        System.out.println("Lift: floor № = " + building.getCurrentLiftFloorNumber() + "; direction = " + building.getLiftDirection());
        System.out.println("-----------------------------------");
        for (Man man:building.getMenInTheLift()) {
            System.out.println("Man on the lift: target floor = " + man.getTargetFloor());
        }
        System.out.println();
        System.out.println();
    }
}
