package ui;

import model.*;
import view.*;
import javax.swing.*;
import java.awt.*;
import events.EventManager;
/**
 * The main game panel containing the tile grid, UI panels and game loop logic.
 * Manages building placement, upgrades and daily updates.
 */
public class GameScreen extends JPanel {

    private final EventManager eventManager = new EventManager();
    private GameManager manager;
    private JButton[][] buttons;
    private MainWindow parentWindow;
    private Timer gameTimer;

    /**
     * Constructs the game screen, initializes UI components, and starts the game loop.
     *
     * @param manager the game manager controlling the game state
     * @param parentWindow the parent frame hosting the game
     */
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
    /**
     * Attempts to upgrade the selected building, checking for resource availability.
     *
     * @param b the building to upgrade
     */
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

    /**
     * Handles player interaction with a tile either placing a building or upgrading an existing one.
     *
     * @param x the x-coordinate of the clicked tile
     * @param y the y-coordinate of the clicked tile
     */
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

    /**
     * Counts the number of power plant buildings currently placed.
     */
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

    /**
     * Starts the main game loop which handles day advancement, population/food logic,
     * resource changes, event triggering, and game over checks.
     */
    private void startGameLoop() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer = new Timer(5000, e -> {
            manager.setPopulation(manager.getTotalPopulation());

            int foodProduction = manager.getFoodProduction();
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

            if (manager.getPower()==0) {
                JOptionPane.showMessageDialog(this,"There's not enough power to keep the shops running!","No Power",JOptionPane.WARNING_MESSAGE);
                manager.setFood(0);
            }

            if (manager.getEconomicBoomDays() > 0)
                manager.setEconomicBoomDays(manager.getEconomicBoomDays() - 1);

            if (manager.getHouseUpgradeBonusDays() > 0)
                manager.setHouseUpgradeBonusDays(manager.getHouseUpgradeBonusDays() - 1);

            if (manager.getMaterialInflationDays() > 0)
                manager.setMaterialInflationDays(manager.getMaterialInflationDays() - 1);

            if (manager.getPopulation() > 0 && manager.getFood() <= 0) {
                manager.incrementStarvationDays();
                int starvationDays = manager.getStarvationDays();

                if (starvationDays == 1) {
                    JOptionPane.showMessageDialog(this,
                            "There's not enough food! Your people will starve in 20 days.",
                            "Starvation Warning", JOptionPane.WARNING_MESSAGE);
                } else if (starvationDays < 20) {
                    JOptionPane.showMessageDialog(this,
                            "Starvation continues (" + starvationDays + " days without food).",
                            "Starvation", JOptionPane.WARNING_MESSAGE);
                } else {
                    manager.setPopulation(0);
                    JOptionPane.showMessageDialog(this,
                            "Your people have starved after 20 days with no food.",
                            "Game Over", JOptionPane.ERROR_MESSAGE);
                    manager.resetStarvationDays();
                }
            } else {
                manager.resetStarvationDays();
            }

            int netPower = manager.getPowerFromBuildings();
            int maxPower = countPowerPlants() * 20;
            int adjustedPower = Math.min(netPower, maxPower);
            manager.setPower(adjustedPower);

            if (manager.getDayCount() >= 20 && manager.getPopulation() <= 0) {
                parentWindow.showGameOverScreen();
            }

            refreshUI();
        });

        gameTimer.start();
    }

    /**
     * Stops the game loop timer.
     */
    public void stopGameLoop() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}
