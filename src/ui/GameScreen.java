package ui;

import model.*;
import view.*;
import javax.swing.*;
import java.awt.*;
import events.EventManager;


public class GameScreen extends JPanel {

    private final EventManager eventManager = new EventManager();
    private GameManager manager;
    private JButton[][] buttons;
    private MainWindow parentWindow;

    public GameScreen(GameManager manager, MainWindow parentWindow) {
        this.manager = manager;
        this.parentWindow = parentWindow;
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(GameManager.GRID_SIZE, GameManager.GRID_SIZE));
        buttons = new JButton[GameManager.GRID_SIZE][GameManager.GRID_SIZE];

        for (int y = 0; y < GameManager.GRID_SIZE; y++) {
            for (int x = 0; x < GameManager.GRID_SIZE; x++) {
                JButton btn = new JButton();
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setPreferredSize(new Dimension(35, 35));
                int finalX = x;
                int finalY = y;

                btn.addActionListener(e -> handleTileClick(finalX, finalY));
                buttons[y][x] = btn;
                gridPanel.add(btn);
            }
        }

        StatusPanel statusPanel = new StatusPanel(manager);
        add(statusPanel, BorderLayout.WEST);
        add(gridPanel, BorderLayout.CENTER);


        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        JButton resourceBtn = new JButton("Buy Resources");
        resourceBtn.addActionListener(e -> new ResourceShop(manager));
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(resourceBtn);
        add(sidePanel, BorderLayout.EAST);

        refreshUI();
        startGameLoop();
    }
    private void attemptUpgrade(Building b) {
        int cost = b.getUpgradeCostMoney();


        int matCost = 20 * b.getLevel();


        if (manager.getMoney() < cost ||
                manager.getConcrete() < matCost ||
                manager.getIron() < matCost ||
                manager.getGlass() < matCost) {
            JOptionPane.showMessageDialog(this,
                    "Not enough resources to upgrade " + b.getName() + ".\n" +
                            "Cost: " + cost + " money and " + matCost + " of each material.",
                    "Insufficient Resources", JOptionPane.WARNING_MESSAGE);
            return;
        }


        manager.changeMoney(-cost);
        manager.addConcrete(-matCost);
        manager.addIron(-matCost);
        manager.addGlass(-matCost);
        b.upgrade();

        JOptionPane.showMessageDialog(this,
                b.getName() + " has been upgraded to level " + b.getLevel() + "!",
                "Upgrade Successful", JOptionPane.INFORMATION_MESSAGE);
    }


    private void handleTileClick(int x, int y) {
        Tile tile = manager.getTiles()[y][x];
        if (tile.isEmpty()) {
            new BuildingShop(this, x, y);
        } else {
            Building b = tile.getBuilding();
            if (b.canUpgrade()) {
                int matCost = 20 * b.getLevel();
                int result = JOptionPane.showConfirmDialog(this,
                        "Upgrade " + b.getName() + " to level " + (b.getLevel() + 1) +
                                "?\nCost: " + b.getUpgradeCostMoney() + " money\n" +
                                "This will consume "+matCost+" of each material.",
                        "Upgrade Building",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    attemptUpgrade(b);
                    refreshUI();
                }
            } else {
                JOptionPane.showMessageDialog(this, b.getName() + " is already at max level.");
            }
        }
    }

    public GameManager getManager() {
        return manager;
    }

    public void refreshUI() {
        Tile[][] tiles = manager.getTiles();
        for (int y = 0; y < GameManager.GRID_SIZE; y++) {
            for (int x = 0; x < GameManager.GRID_SIZE; x++) {
                JButton btn = buttons[y][x];
                Building b = tiles[y][x].getBuilding();
                if (b != null) {
                    btn.setIcon(b.getIcon());
                    btn.setToolTipText(b.getName());
                } else {
                    btn.setIcon(null);
                    btn.setToolTipText(null);
                }
            }
        }
    }
    private int countPowerPlants() {
        int count = 0;
        Tile[][] grid = manager.getTiles();
        for (int y = 0; y < GameManager.GRID_SIZE; y++) {
            for (int x = 0; x < GameManager.GRID_SIZE; x++) {
                Building b = grid[y][x].getBuilding();
                if (b != null && b.getName().equalsIgnoreCase("Power Plant")) {
                    count++;
                }
            }
        }
        return count;
    }


    private void startGameLoop() {
        Timer timer = new Timer(5000, e -> {

            manager.setPopulation(manager.getTotalPopulationFromBuildings());

            int foodProduction = getFoodSupport();
            int foodNeeded = manager.getPopulation();
            int netFood = foodProduction - foodNeeded;

            manager.incrementDay();


            int income = manager.getPopulation() * 4;
            if (manager.isEconomicBoomActive()) {
                income *= 2;
            }
            manager.changeMoney(income);



            manager.changeFood(netFood);


            eventManager.maybeTriggerEvent(manager, this);

            if (manager.getEconomicBoomDays() > 0) {
                manager.setEconomicBoomDays(manager.getEconomicBoomDays() - 1);
            }

            if (manager.getHouseUpgradeBonusDays() > 0){
                manager.setHouseUpgradeBonusDays(manager.getHouseUpgradeBonusDays() - 1);
            }

            if (manager.getMaterialInflationDays() > 0){
                manager.setMaterialInflationDays(manager.getMaterialInflationDays() - 1);
            }

            if (manager.getFood() == 0 && manager.getPopulation() > 0) {
                manager.incrementStarvationDays();

                int starvationDays = manager.getStarvationDays();
                int loss = Math.max(1, (manager.getPopulation() * starvationDays) / 20);
                manager.addPopulation(-loss);

                JOptionPane.showMessageDialog(this,
                        "Starvation continues!\n" + loss + " people died.\n" +
                                starvationDays + " day(s) without food.",
                        "Starvation", JOptionPane.WARNING_MESSAGE);

            } else {

                manager.resetStarvationDays();
            }


            int netPower = getNetPower();
            int maxPower = countPowerPlants() * 20;
            int adjustedPower = Math.min(netPower, maxPower);
            manager.setPower(adjustedPower);


            if (manager.getDayCount() >= 20 &&
                    (manager.getPopulation() <= 0 || manager.getFood() < -50)) {
                parentWindow.showGameOverScreen();
            }

            refreshUI();
        });

        timer.start();
    }








    private int getNetPower() {
        return manager.getNetPowerFromBuildings();
    }


    private int getFoodSupport() {
        return manager.getTotalFoodProduction();
    }

}
