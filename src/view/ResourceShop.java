package view;

import model.GameManager;

import javax.swing.*;
import java.awt.*;

public class ResourceShop extends JDialog {

    public ResourceShop(GameManager manager) {
        setTitle("Buy Resources");
        setSize(400, 200);
        setLayout(new GridLayout(1, 3));
        setLocationRelativeTo(null);
        setModal(true);

        add(resourceButton("Iron", 5, manager));
        add(resourceButton("Concrete", 5, manager));
        add(resourceButton("Glass", 5, manager));

        setVisible(true);
    }

    private JButton resourceButton(String name, int cost, GameManager manager) {
        JButton btn = new JButton(name + " (" + cost + "$)");
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);

        btn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "How many units of " + name + " do you want to buy?");
            try {
                int amount = Integer.parseInt(input);
                int totalCost = amount * cost;

                int current = switch (name) {
                    case "Iron" -> manager.getIron();
                    case "Concrete" -> manager.getConcrete();
                    case "Glass" -> manager.getGlass();
                    default -> 0;
                };

                int maxStorage = manager.getMaxMaterialStorage();
                int availableSpace = maxStorage - current;

                if (amount > availableSpace) {
                    JOptionPane.showMessageDialog(this, "You can only store " + availableSpace + " more units.");
                    return;
                }

                if (manager.getMoney() >= totalCost) {
                    manager.changeMoney(-totalCost);
                    switch (name) {
                        case "Iron" -> manager.addIron(amount);
                        case "Concrete" -> manager.addConcrete(amount);
                        case "Glass" -> manager.addGlass(amount);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough money.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        });


        return btn;
    }
}
