package model;

import java.io.Serializable;
import java.util.*;

public class GameManager implements Serializable {

    private int dayCount = 0;
    public static final int GRID_SIZE = 20;

    private Tile[][] tiles;
    private int money = 100;
    private int food = 50;
    private int power = 0;
    private int population = 0;

    private int iron = 10;
    private int concrete = 10;
    private int glass = 10;

    private ArrayList<Building> buildings;

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


    public int getMoney() { return money; }
    public int getFood() { return food; }
    public int getPower() { return power; }
    public int getPopulation() { return population; }

    public int getIron() { return iron; }
    public int getConcrete() { return concrete; }
    public int getGlass() { return glass; }

    public void addPopulation(int amount) {
        population += amount;
    }

    public void changeMoney(int amount) {
        money += amount;
    }

    public void changeFood(int amount) {
        food += amount;
    }


    public void addIron(int amount) {
        iron += amount;
    }

    public void addConcrete(int amount) {
        concrete += amount;
    }

    public void addGlass(int amount) {
        glass += amount;
    }

    public void incrementDay() {
        dayCount++;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void build(Building building, int x, int y) {
        tiles[y][x].setBuilding(building);
        buildings.add(building);
        addPopulation(building.getPopulationBonus());
        changeMoney(-building.getCostMoney());
        iron -= building.getCostIron();
        concrete -= building.getCostConcrete();
        glass -= building.getCostGlass();
    }

    public boolean canAfford(Building b) {
        return money >= b.getCostMoney() &&
                iron >= b.getCostIron() &&
                concrete >= b.getCostConcrete() &&
                glass >= b.getCostGlass();
    }
}