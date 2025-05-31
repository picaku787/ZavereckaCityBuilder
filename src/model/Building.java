package model;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.URL;
/**
 * Represents a building in the game with associated costs, production values,
 * and functionality for upgrading and icon handling.
 */
public class Building implements Serializable {

    private String name;
    private int costMoney;
    private int costIron;
    private int costConcrete;
    private int costGlass;
    private int populationBonus;
    private int energyProduction;
    private int energyConsumption;
    private int foodProduction;
    private int level = 1;
    private int maxLevel = 3;
    private String baseIconName;
    private BuildingType type;
    private ImageIcon icon;

    /**
     * Constructs a Building object with all its properties initialized.
     */
    public Building(String name, int money, int iron, int concrete, int glass,
                    int populationBonus, int energyProduction, int energyConsumption,
                    int foodProduction, ImageIcon rawIcon, BuildingType type) {

        this.name = name;
        this.costMoney = money;
        this.costIron = iron;
        this.costConcrete = concrete;
        this.costGlass = glass;
        this.populationBonus = populationBonus;
        this.energyProduction = energyProduction;
        this.energyConsumption = energyConsumption;
        this.foodProduction = foodProduction;
        this.level = 1;
        this.maxLevel = 3;
        this.type = type;

        if (rawIcon != null && rawIcon.getDescription() != null) {
            this.baseIconName = rawIcon.getDescription();
        } else {
            this.baseIconName = "default";
            System.err.println("Icon description missing, using default baseIconName");
        }

        if (rawIcon != null) {
            Image scaledImage = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(scaledImage);
        } else {
            this.icon = null;
        }
    }

    /**
     * Returns the upgrade cost in money based on the building name and level.
     */
    public int getUpgradeCostMoney() {
        return switch (name) {
            case "House" -> 1000 * level;
            case "Shop" -> 1500 * level;
            case "Power Plant" -> 2000 * level;
            case "Warehouse" -> 2500 * level;
            default -> 500 * level;
        };
    }

    /**
     * Upgrades the building to the next level, applying different bonuses
     * if a house upgrade bonus is active.
     */
    public void upgrade() {
        if (!canUpgrade()) return;

        level++;

        GameManager manager = new GameManager();
        if (type == BuildingType.HOUSE && manager.getHouseUpgradeBonusDays() > 0) {
            this.populationBonus *= 20;
        } else {
            this.populationBonus *= 10;
        }
        this.foodProduction *= 10;
        this.energyProduction *= 10;
        this.energyConsumption *= 5;

        System.out.println(name + " upgraded to level " + level + ".");
    }

    public String getName() {
        return name;
    }

    public int getCostMoney() {
        return costMoney;
    }

    public int getCostIron() {
        return costIron;
    }

    public int getCostConcrete() {
        return costConcrete;
    }

    public int getCostGlass() {
        return costGlass;
    }

    public int getPopulationBonus() {
        return populationBonus;
    }

    public int getEnergyProduction() {
        return energyProduction;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public boolean canUpgrade() {
        return level < maxLevel;
    }

    public int getLevel() {
        return level;
    }

    public BuildingType getType() {
        return type;
    }

    /**
     * With help from ChatGPT
     *
     * Returns the scaled and appropriate icon for the building based on its level.
     */
    public ImageIcon getIcon() {
        String path = "/icons/" + baseIconName;
        if (level > 1) {
            path += "_lvl" + level;
        }
        path += ".png";

        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("Resource not found: " + path);
            return null;
        }

        ImageIcon raw = new ImageIcon(resource);
        Image scaled = raw.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}