package view;

import model.GameManager;
import javax.swing.*;
import java.awt.*;

/**
 * A shop window allowing players to purchase building materials like Iron, Concrete, and Glass.
 */
public class ResourceShop extends JDialog {

    /**
     * Constructs the resource shop and sets up purchase buttons.
     *
     * @param manager the game manager managing game state and resources
     */
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

    /**
     * Creates a resource purchase button for a given material.
     *
     * @param name    the name of the resource
     * @param cost    the base cost per unit
     * @param manager the game manager for handling resource logic
     * @return the configured JButton
     */
    private JButton resourceButton(String name, int cost, GameManager manager) {
        int displayCost = manager.isMaterialInflated() ? cost * 4 : cost;
        JButton btn = new JButton(name + " (" + displayCost + "$)");
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);

        btn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "How many units of " + name + " do you want to buy?");
            try {
                int amount = Integer.parseInt(input);


                int finalCost = cost;
                if (manager.isMaterialInflated()) {
                    finalCost *= 4;
                }

                int totalCost = amount * finalCost;

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
