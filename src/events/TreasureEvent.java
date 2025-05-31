package events;

import model.GameManager;
import javax.swing.*;
import java.util.Random;

/**
 * Event representing the discovery of a hidden treasure, granting the player a bonus.
 */
public class TreasureEvent implements GameEvent {

    @Override
    public String getTitle() {
        return "Treasure Discovered!";
    }

    @Override
    public String getMessage() {
        return "A hidden treasure has been discovered in the city!";
    }

    @Override
    public String[] getOptions() {
        return new String[]{"Claim it!"};
    }

    /**
     * Applies the treasure event by granting the player a random percentage increase in money.
     */
    @Override
    public void apply(int option, GameManager manager) {
        int currentMoney = manager.getMoney();
        if (currentMoney <= 0) return;

        Random random = new Random();
        int percent = 1 + random.nextInt(10);
        int reward = (currentMoney * percent) / 100;

        manager.changeMoney(reward);

        JOptionPane.showMessageDialog(null,
                "You received " + reward + "$",
                "Treasure Found",
                JOptionPane.INFORMATION_MESSAGE);
    }
}