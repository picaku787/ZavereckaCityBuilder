package view;

import model.Building;
import ui.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Random;

public class BuildingShop extends JDialog {

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



    public BuildingShop(GameScreen gameScreen, int x, int y) {
        setTitle("Buy a Building");
        setSize(400, 300);
        setLayout(new GridLayout(1, 3));
        setLocationRelativeTo(null);
        setModal(true);


        add(buildingButton(
                new Building("House", 10, 2, 2, 1,
                        getRandomPop(), 0, 1, 0,
                        loadIcon("house.png")),
                gameScreen, x, y));

        add(buildingButton(
                new Building("Shop", 20, 1, 0, 1,
                        0, 0, 2, 20,
                        loadIcon("shop.png")),
                gameScreen, x, y));

        add(buildingButton(
                new Building("Power Plant", 50, 2, 3, 0,
                        0, 20, 0, 0,
                        loadIcon("powerplant.png")),
                gameScreen, x, y));

        add(buildingButton(
                new Building("Warehouse", 120, 4, 5, 2,
                        0, 0, 1, 0,
                        loadIcon("warehouse.png")),
                gameScreen, x, y));

        setVisible(true);
    }

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

    private int getRandomPop() {
        return rng.nextInt(6) + 1;
    }

}
