package view;

import model.Building;
import model.BuildingType;
import ui.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Random;

/**
 * A shop window that allows the user to select and purchase a building.
 */
public class BuildingShop extends JDialog {

    /**
     * Helped by ChatGpt
     *
     * Loads an icon for a building from the resource directory.
     *
     * @param fileName the name of the icon file
     * @return the scaled ImageIcon with description
     */
    private ImageIcon loadIcon(String fileName) {
        String path = "/icons/" + fileName;
        URL resource = getClass().getResource(path);

        ImageIcon icon = new ImageIcon(resource);
        icon.setDescription(fileName.replace(".png", ""));

        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);
        scaledIcon.setDescription(icon.getDescription());
        return scaledIcon;
    }


    /**
     * Constructs the building shop interface with available building options.
     *
     * @param gameScreen the game screen to update after building
     * @param x          the x-coordinate where the building will be placed
     * @param y          the y-coordinate where the building will be placed
     */
    public BuildingShop(GameScreen gameScreen, int x, int y) {
        setTitle("Buy a Building");
        setSize(400, 300);
        setLayout(new GridLayout(1, 3));
        setLocationRelativeTo(null);
        setModal(true);


        add(buildingButton(
                new Building("House", 10, 2, 2, 1,
                        getRandomPop(), 0, 1, 0,
                        loadIcon("house.png"), BuildingType.HOUSE),
                gameScreen, x, y));

        add(buildingButton(
                new Building("Shop", 20, 1, 0, 1,
                        0, 0, 2, 20,
                        loadIcon("shop.png"),BuildingType.SHOP),
                gameScreen, x, y));

        add(buildingButton(
                new Building("Power Plant", 50, 2, 3, 0,
                        0, 20, 0, 0,
                        loadIcon("powerplant.png"),BuildingType.POWER_PLANT),
                gameScreen, x, y));

        add(buildingButton(
                new Building("Warehouse", 120, 4, 5, 2,
                        0, 0, 1, 0,
                        loadIcon("warehouse.png"),BuildingType.WAREHOUSE),
                gameScreen, x, y));

        setVisible(true);
    }
    /**
     * Creates a button representing a building that can be purchased and placed.
     *
     * @param b          the building to be created
     * @param gameScreen the game screen for interaction
     * @param x          the tile x-coordinate
     * @param y          the tile y-coordinate
     * @return the configured JButton
     */
    private JButton buildingButton(Building b, GameScreen gameScreen, int x, int y) {
        JButton button = new JButton("<html>" + b.getName() + "<br/>Money " + b.getCostMoney() +
                "<br/>Concrete " + b.getCostConcrete() + "<br/>Iron " + b.getCostIron() +
                "<br/>Glass " + b.getCostGlass() + "</html>", b.getIcon());

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.addActionListener(e -> {
            if (gameScreen.getManager().canAfford(b)) {
                gameScreen.getManager().build(b, x, y);
                gameScreen.refreshUI();
            } else {
                JOptionPane.showMessageDialog(this, "Not enough resources.");
            }
            dispose();
        });
        return button;
    }

    private Random rng = new Random();

    /**
     * Returns a random population bonus for houses.
     *
     * @return a random integer between 1 and 6
     */
    private int getRandomPop() {
        return rng.nextInt(6) + 1;
    }

}
