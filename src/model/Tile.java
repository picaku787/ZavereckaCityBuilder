package model;

import java.io.Serializable;

public class Tile implements Serializable {


    private final int x, y;
    private Building building;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.building = null;
    }

    public boolean isEmpty() {
        return building == null;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }
}
