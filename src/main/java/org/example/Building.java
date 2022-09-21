package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Building {
    private Lift lift;
    private List<Floor> floors;
    private long floorsCount;

    public long getFloorsCount() {
        return floorsCount;
    }

    public void setFloorsCount(long floorsCount) {
        this.floorsCount = floorsCount;
    }



    public Building() {
        // floors initialize
        floors = new ArrayList<>();
        this.floorsCount = Math.round(5 + Math.random()*15);
        for (int i = 0; i < floorsCount; i++) {
            floors.add(new Floor());
        }

        // lift initialize
        this.lift = new Lift();
    }




    public void runLift(){
        int factFloorNumber = 0;
        // цикл для перемещения лифта по этажам
        while (true){
            this.passengersGetOutOnTheFloorFromTheLift(factFloorNumber); // пассажиры выходят из лифта на этаж

            Floor floor = floors.get(factFloorNumber);
            Iterator<Man> manIterator = floor.menOnTheFloor.iterator();

            while (manIterator.hasNext()) {
                Man man = manIterator.next();
                if (!lift.isFull()) {
                    this.passengerGetOnTheLiftFromTheFloor(man, factFloorNumber);
                }
            }
        }
    }






    //пассажиры выходят на этаже из лифта
    private void passengersGetOutOnTheFloorFromTheLift(int factFloorNumber){
        floors.get(factFloorNumber).menGetOutToTheFloor(lift.passengerWhoNeedGetOffTheLift(factFloorNumber));
    }


    //пассажиры заходят в лифт с этажа
    private void passengerGetOnTheLiftFromTheFloor(Man man, int factFloorNumber){
        if (lift.getLiftDirection() == man.getTargetDirectionInBuilding(factFloorNumber)){
            lift.passengerGetOnTheLift(man);
        }
    }


    private class Lift{
        Direction liftMoveDirection;
        private List<Man> menInTheLift = new ArrayList();

        public Direction getLiftDirection() {
            return liftMoveDirection;
        }

        // пересчитывает направление движения лифта
        public void recomputeLiftDirection(long factFloor){
            int upDirection = 0;
            int downDirection = 0;
            for (Man man:menInTheLift) {
                Direction direction = man.getTargetDirectionInBuilding(factFloor);
                if (direction == Direction.UP){upDirection++;}
                if (direction == Direction.DOWN){downDirection++;}
            }
            liftMoveDirection = upDirection-downDirection == 0 ? Direction.NONE : (upDirection-downDirection > 0 ? Direction.UP : Direction.DOWN);
        }

        public void setDirection(Direction direction) {
            this.liftMoveDirection = direction;
        }
        public Lift() {
            liftMoveDirection = Direction.UP;
        }

        // пассажиры, чей этаж, выходят из лифта
        public List<Man> passengerWhoNeedGetOffTheLift(long factFloor){
            List<Man> menWhoGetOffTheLift = new ArrayList<>();
            if (menInTheLift.size() == 0) {
                return null;
            }
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

        // лифт заполнен
        public boolean isFull(){
            return menInTheLift.size() >= 5;
        }

        // пассажир заходит в лифт
        public void passengerGetOnTheLift(Man man){
            menInTheLift.add(man);
        }
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
                menOnTheFloor.add(new Man(floorsCount));
            }
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