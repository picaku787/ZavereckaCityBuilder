package events;

import model.GameManager;

import java.util.Random;

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

    @Override
    public void apply(int option, GameManager manager) {
        int currentMoney = manager.getMoney();
        if (currentMoney <= 0) return;

        Random random = new Random();
        int percent = 1 + random.nextInt(10);
        int reward = (currentMoney * percent) / 100;

        manager.changeMoney(reward);

        javax.swing.JOptionPane.showMessageDialog(null,
                "You received " + reward + "$",
                "Treasure Found",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}