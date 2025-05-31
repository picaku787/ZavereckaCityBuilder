package model;

import java.io.Serializable;
/**
 * Represents a single tile in the game grid which can hold a building.
 */
public class Tile implements Serializable {

    private final int x, y;
    private Building building;

    /**
     * Constructs a Tile at the specified coordinates.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.building = null;
    }

    /**
     * Returns true if no building is currently placed on the tile.
     *
     * @return true if the tile is empty; false otherwise
     */
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
