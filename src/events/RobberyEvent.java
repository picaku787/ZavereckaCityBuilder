package events;

import model.GameManager;
import javax.swing.*;
import java.util.Random;

/**
 * Event representing a robbery where the player loses a small percentage of their money.
 */
public class RobberyEvent implements GameEvent {

    @Override
    public String getTitle() {
        return "Robbery!";
    }

    @Override
    public String getMessage() {
        return "A group of bandits has stolen some of your money during the night";
    }

    @Override
    public String[] getOptions() {
        return new String[]{"Accept the loss"};
    }

    /**
     * Applies the robbery effect by subtracting a random percentage of money
     * and displaying a message with the lost amount.
     */
    @Override
    public void apply(int option, GameManager manager) {
        int currentMoney = manager.getMoney();
        if (currentMoney <= 0) return;

        Random random = new Random();
        int percent = 1 + random.nextInt(10);
        int loss = (currentMoney * percent) / 100;

        manager.changeMoney(-loss);

        JOptionPane.showMessageDialog(null,
                "You lost " + loss + "$",
                "Robbery",
                JOptionPane.WARNING_MESSAGE);
    }
}
