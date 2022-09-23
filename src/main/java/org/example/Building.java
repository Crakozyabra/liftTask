package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Building {

    private Lift lift;
    public List<Man> getMenInTheLift() {
        return lift.menInTheLift;
    }
    public void setLift(Lift lift) {
        this.lift = lift;
    }

    private List<Floor> floors;
    private long floorsCount;


    public Building() {
        floors = new ArrayList<>();
        this.floorsCount = Math.round(5 + Math.random() * 15);
        System.out.println("Floor counts in building is: " + this.floorsCount);
        System.out.println();
        for (int i = 0; i < floorsCount; i++) {
            floors.add(new Floor());
        }

        this.lift = new Lift();
    }


    private void printPassengersOnTheFloor(int factFloorNumber, String message){
        Floor floor = floors.get(factFloorNumber - 1);
        System.out.println();
        System.out.println(message + " " + factFloorNumber + " floor:");
        for (Man man:floor.menOnTheFloor) {
            System.out.println("Man on the floor: target floor = " + man.targetFloor + "; fact floor = " + factFloorNumber + "; direction = " + man.getTargetDirectionInBuilding(factFloorNumber));
        }
    }


    public void runLiftMoving() {
        int factFloorNumber = 1;

        // цикл для перемещения лифта по этажам
        while (true) {
            lift.delayOnTheFloor(1000); // остановка лифта на этаже

            this.passengersGetOutOnTheFloorFromTheLift(factFloorNumber); // пассажиры, если есть, выходят из лифта на этаж

            printPassengersOnTheFloor(factFloorNumber, "Men on the floor after lift come in"); // печать в консоль пассажиров на этаже до посадки в лифт

            this.passengerGetOnTheLiftFromTheFloor(factFloorNumber); // пассажиры заходят c этажа в лифт

            printPassengersOnTheFloor(factFloorNumber, "Men on the floor before lift come in"); // печать в консоль пассажиров на этаже после посадки в лифт

            lift.printMenInTheLift(factFloorNumber); // печать в консоль о пассажирах в лифте

            factFloorNumber = this.moveLift(factFloorNumber); // лифт двигается
        }
    }




    private int moveLift(int factFloorNumber) {
        if(lift.isEmpty()) {
            lift.recomputeLiftDirection(factFloorNumber); // пересчитать направление движения лифта
        } else if (lift.getLiftDirection() == Direction.UP) {
            if (floorsCount == factFloorNumber) {
                return (int) factFloorNumber;
            }
            factFloorNumber++;
        } else if (lift.getLiftDirection() == Direction.DOWN) {
            if (factFloorNumber == 0) {
                return 0;
            }
            factFloorNumber--;
        }
        return factFloorNumber;
    }





    //пассажиры выходят на этаже из лифта
    private void passengersGetOutOnTheFloorFromTheLift(int factFloorNumber){
        List<Man> men = lift.passengerWhoNeedGetOffTheLift(factFloorNumber); // пассажиры выходят из лифта
            floors.get(factFloorNumber - 1).
                    menGetOutToTheFloor(men); // пассажиры заходят на этаж
    }




    //пассажиры заходят в лифт с этажа
    private void passengerGetOnTheLiftFromTheFloor(int factFloorNumber){
        Floor floor = floors.get(factFloorNumber-1);
        Iterator<Man> manIterator = floor.menOnTheFloor.iterator();
        while (manIterator.hasNext()) {
            Man man = manIterator.next();
            if (!lift.isFull()) {
                if (lift.getLiftDirection() == man.getTargetDirectionInBuilding(factFloorNumber)) {
                    lift.menInTheLift.add(man);
                    manIterator.remove();
                }
            }
        }
    }




    private class Lift{
        public void delayOnTheFloor(long milliseconds){
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {}
        }

        Direction liftMoveDirection;

        private List<Man> menInTheLift = new ArrayList();

        public Direction getLiftDirection() {
            return liftMoveDirection;
        }

        // пересчитывает направление движения лифта в зависимости от количества пассажиров на этаже, где остановился лифт
        public void recomputeLiftDirection(long factFloorNumber){
            int upDirection = 0;
            int downDirection = 0;
            for (Man man:floors.get((int)factFloorNumber-1).menOnTheFloor) {
                Direction direction = man.getTargetDirectionInBuilding(factFloorNumber);
                if (direction == Direction.UP){upDirection++;}
                if (direction == Direction.DOWN){downDirection++;}
            }
            liftMoveDirection = upDirection-downDirection == 0 ? Direction.NONE : (upDirection-downDirection > 0 ? Direction.UP : Direction.DOWN);
        }

        public void printMenInTheLift(int floorNumber){
            System.out.println();
            System.out.println("Lift: floor № = " + floorNumber + "; direction = " + lift.getLiftDirection());
            System.out.println("-----------------------------------");
            for (Man man:lift.menInTheLift) {
                System.out.println("Man on the lift: target floor = " + man.targetFloor);
            }
            System.out.println();
            System.out.println();
        }

        public Lift() {
            liftMoveDirection = Direction.NONE;
        }


        // пассажиры, чей этаж, выходят из лифта
        public List<Man> passengerWhoNeedGetOffTheLift(long factFloor){
            if (menInTheLift.size() == 0) {
                return null;
            }
            List<Man> menWhoGetOffTheLift = new ArrayList<>();
            Iterator<Man> manIterator = menInTheLift.iterator();
            while (manIterator.hasNext()){
                Man man = manIterator.next();
                if (man.getTargetDirectionInBuilding(factFloor) == Direction.NONE){
                    menWhoGetOffTheLift.add(man);
                    manIterator.remove();
                }
            }
            return menWhoGetOffTheLift;
        }

        public boolean isFull(){
            return menInTheLift.size() >= 5;
        }

        public boolean isEmpty(){return menInTheLift.size() == 0;}
    }




    private class Floor{
        private Direction upButton;
        private Direction downButton;
        private long menCount;
        private List<Man> menOnTheFloor;

        public Floor(){
            upButton = Direction.NONE;
            downButton = Direction.NONE;
            menCount = Math.round(Math.random()*10);
            menOnTheFloor = new ArrayList<>();
            for (int i = 0; i < menCount; i++) {
                long targetFloor = 1 + Math.round(Math.random()*(floorsCount-1));
                menOnTheFloor.add(new Man(targetFloor));
            }
        }

        public void printMenOnTheFloor(int floorNumber){
            System.out.println("Men on the " + floorNumber + " floor");
            for (Man man:this.menOnTheFloor) {
                System.out.println("Man" + man.targetFloor);
            }
            System.out.println();
        }

        private void menGetOutToTheFloor(List<Man> menInTheLift){
            if (menInTheLift != null) {
                if (menInTheLift.size() != 0) {
                    Collections.addAll(menInTheLift);
                }
            }
        }

        public boolean isUpButton() {
            return upButton == Direction.UP;
        }

        public void setUpButton(Direction upButton) {
            this.upButton = upButton;
        }

        public boolean isDownButton() {
            return downButton == Direction.DOWN;
        }

        public void setDownButton(Direction downButton) {
            this.downButton = downButton;
        }
    }
}