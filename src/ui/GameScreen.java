package ui;

import model.*;
import view.*;
import javax.swing.*;
import java.awt.*;


public class GameScreen extends JPanel {

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

    private void handleTileClick(int x, int y) {
        Tile tile = manager.getTiles()[y][x];
        if (tile.isEmpty()) {
            new BuildingShop(this, x, y);
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
            manager.incrementDay();

            manager.changeMoney(manager.getPopulation() * 4);
            manager.changeFood(-Math.max(0, manager.getPopulation() - getFoodSupport()));

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
        int total = 0;
        Tile[][] grid = manager.getTiles();
        for (int y = 0; y < GameManager.GRID_SIZE; y++) {
            for (int x = 0; x < GameManager.GRID_SIZE; x++) {
                Building b = grid[y][x].getBuilding();
                if (b != null) {
                    total += b.getEnergyProduction() - b.getEnergyConsumption();
                }
            }
        }
        return total;
    }

    private int getFoodSupport() {
        int total = 0;
        Tile[][] grid = manager.getTiles();
        for (int y = 0; y < GameManager.GRID_SIZE; y++) {
            for (int x = 0; x < GameManager.GRID_SIZE; x++) {
                Building b = grid[y][x].getBuilding();
                if (b != null) {
                    total += b.getFoodProduction();
                }
            }
        }
        return total;
    }
}
