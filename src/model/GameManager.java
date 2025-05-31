package model;

import java.io.Serializable;
import java.util.*;

/**
 * Manages the game state including the grid of tiles, resources, day count,
 * population tracking, and more.
 */
public class GameManager implements Serializable {

    public static final int GRID_SIZE = 20;

    private int dayCount = 0;
    private Tile[][] tiles;
    private int money = 100;
    private int food = 50;
    private int power = 0;
    private int population = 0;
    private int starvationDays = 0;
    private int houseUpgradeBonusDays = 0;
    private int materialInflationDays = 0;
    private int economicBoomDays = 0;
    private int iron = 10;
    private int concrete = 10;
    private int glass = 10;
    private ArrayList<Building> buildings;

    /**
     * Initializes a new game manager.
     */
    public GameManager() {
        tiles = new Tile[GRID_SIZE][GRID_SIZE];
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                tiles[y][x] = new Tile(x, y);
            }
        }
        buildings = new ArrayList<>();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setPower(int amount) {
        power = Math.max(0, amount);
    }

    public int getMoney() {
        return money;
    }

    public int getFood() {
        return food;
    }

    public int getPower() {
        return power;
    }

    public int getPopulation() {
        return population;
    }

    public int getIron() {
        return iron;
    }

    public int getConcrete() {
        return concrete;
    }

    public int getGlass() {
        return glass;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getHouseUpgradeBonusDays() {
        return houseUpgradeBonusDays;
    }

    public void setHouseUpgradeBonusDays(int houseUpgradeBonusDays) {
        this.houseUpgradeBonusDays = houseUpgradeBonusDays;
    }

    public int getMaterialInflationDays() {
        return materialInflationDays;
    }

    public void setMaterialInflationDays(int materialInflationDays) {
        this.materialInflationDays = materialInflationDays;
    }

    public boolean isMaterialInflated() {
        return materialInflationDays > 0;
    }

    public void addPopulation(int amount) {
        population += amount;
    }

    public void changeMoney(int amount) {
        money += amount;
    }

    public void changeFood(int amount) {
        food += amount;
        if (food > 100) {
            food = 100;
        } else if (food < 0) {
            food = 0;
        }
    }

    public void addIron(int amount) {
        iron = Math.min(iron + amount, getMaxMaterialStorage());
    }

    public void addConcrete(int amount) {
        concrete = Math.min(concrete + amount, getMaxMaterialStorage());
    }

    public void addGlass(int amount) {
        glass = Math.min(glass + amount, getMaxMaterialStorage());
    }

    public void incrementDay() {
        dayCount++;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void resetStarvationDays() {
        starvationDays = 0;
    }

    public void incrementStarvationDays() {
        starvationDays++;
    }

    public int getStarvationDays() {
        return starvationDays;
    }

    public void removeBuilding(Building b) {
        buildings.remove(b);
    }

    public void setEconomicBoomDays(int days) {
        economicBoomDays = days;
    }

    public int getEconomicBoomDays() {
        return economicBoomDays;
    }

    public boolean isEconomicBoomActive() {
        return economicBoomDays > 0;
    }

    /**
     * Calculates the total population from all constructed buildings.
     */
    public int getTotalPopulation() {
        int total = 0;
        for (Building b : buildings) {
            total += b.getPopulationBonus();
        }
        return total;
    }

    /**
     * Calculates total food production from all buildings.
     */
    public int getFoodProduction() {
        int total = 0;
        for (Building b : buildings) {
            total += b.getFoodProduction();
        }
        return total;
    }

    /**
     * Calculates energy from all buildings.
     */
    public int getPowerFromBuildings() {
        int total = 0;
        for (Building b : buildings) {
            total += b.getEnergyProduction() - b.getEnergyConsumption();
        }
        return total;
    }

    /**
     * Calculates the total material storage capacity based on warehouse buildings.
     */
    public int getMaxMaterialStorage() {
        int total = 10;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                Building b = tiles[y][x].getBuilding();
                if (b != null && b.getName().equalsIgnoreCase("Warehouse") && !(b.getLevel() == 1)) {
                    total += b.getLevel() * 30;
                }
                if (b != null && b.getName().equalsIgnoreCase("Warehouse") && b.getLevel() == 1) {
                    total += 10;
                }
            }
        }
        return total;
    }

    /**
     * Applies population, money, and material changes when constructing a building.
     */
    public void build(Building building, int x, int y) {
        tiles[y][x].setBuilding(building);
        buildings.add(building);
        addPopulation(building.getPopulationBonus());
        changeMoney(-building.getCostMoney());
        iron -= building.getCostIron();
        concrete -= building.getCostConcrete();
        glass -= building.getCostGlass();
    }

    /**
     * Returns whether the player has enough resources to construct a given building.
     */
    public boolean canAfford(Building b) {
        return money >= b.getCostMoney() &&
                iron >= b.getCostIron() &&
                concrete >= b.getCostConcrete() &&
                glass >= b.getCostGlass();
    }

    /**
     * Resets the game state, clearing resources, events, and all buildings.
     */
    public void reset() {
        money = 100;
        food = 100;
        power = 0;
        dayCount = 0;
        population = 0;
        iron = 10;
        concrete = 10;
        glass = 10;
        starvationDays = 0;
        houseUpgradeBonusDays = 0;
        materialInflationDays = 0;
        economicBoomDays = 0;
        buildings.clear();

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                tiles[y][x].setBuilding(null);
            }
        }
    }
}
