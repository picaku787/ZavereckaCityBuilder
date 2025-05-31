package events;

import model.GameManager;
import model.Tile;
import model.Building;
import javax.swing.*;
import java.util.*;

/**
 * Event representing a storm that destroys a random number of buildings in the city.
 */
public class StormEvent implements GameEvent {

    @Override
    public String getTitle() {
        return "Storm";
    }

    @Override
    public String getMessage() {
        return "A powerful storm sweeps through your city, destroying 1–3 random buildings!";
    }

    @Override
    public String[] getOptions() {
        return new String[]{"OK"};
    }

    /**
     * Applies the storm effect by destroying 1–3 random buildings on occupied tiles.
     * A dialog displays the list of destroyed buildings.
     */
    @Override
    public void apply(int option, GameManager manager) {
        ArrayList<Tile> occupiedTiles = new ArrayList<>();

        Tile[][] grid = manager.getTiles();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (!grid[y][x].isEmpty()) {
                    occupiedTiles.add(grid[y][x]);
                }
            }
        }

        if (occupiedTiles.isEmpty()) return;

        Random random = new Random();
        int buildingsToRemove = 1 + random.nextInt(Math.min(3, occupiedTiles.size()));

        StringBuilder removedList = new StringBuilder("Buildings destroyed:\n");

        for (int i = 0; i < buildingsToRemove; i++) {
            Tile selected = occupiedTiles.remove(random.nextInt(occupiedTiles.size()));
            Building removed = selected.getBuilding();
            removedList.append("- ").append(removed.getName()).append(" (Level ")
                    .append(removed.getLevel()).append(")\n");

            manager.removeBuilding(removed);
            selected.setBuilding(null);
        }

        JOptionPane.showMessageDialog(null,
                removedList.toString(),
                "Storm Damage",
                JOptionPane.WARNING_MESSAGE);
    }
}
