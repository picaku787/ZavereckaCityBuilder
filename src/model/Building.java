package model;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

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
    private ImageIcon icon;

    public Building(String name, int money, int iron, int concrete, int glass,
                    int populationBonus, int energyProduction, int energyConsumption,
                    int foodProduction, ImageIcon rawIcon) {

        this.name = name;
        this.costMoney = money;
        this.costIron = iron;
        this.costConcrete = concrete;
        this.costGlass = glass;
        this.populationBonus = populationBonus;
        this.energyProduction = energyProduction;
        this.energyConsumption = energyConsumption;
        this.foodProduction = foodProduction;

        Image scaledImage = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        this.icon = new ImageIcon(scaledImage);
    }

    public String getName() { return name; }
    public int getCostMoney() { return costMoney; }
    public int getCostIron() { return costIron; }
    public int getCostConcrete() { return costConcrete; }
    public int getCostGlass() { return costGlass; }
    public int getPopulationBonus() { return populationBonus; }
    public int getEnergyProduction() { return energyProduction; }
    public int getEnergyConsumption() { return energyConsumption; }
    public int getFoodProduction() { return foodProduction; }
    public ImageIcon getIcon() { return icon; }
}
