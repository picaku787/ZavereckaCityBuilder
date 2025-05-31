package events;

import model.GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EventManager {
    private ArrayList<GameEvent> allEvents = new ArrayList<>();
    private Random random = new Random();

    public EventManager() {
        allEvents.add(new StormEvent());
        allEvents.add(new MaterialInflationEvent());
        allEvents.add(new HouseUpgradeEvent());
        allEvents.add(new TreasureEvent());
        allEvents.add(new RobberyEvent());
        allEvents.add(new EconomicBoomEvent());
    }

    public void maybeTriggerEvent(GameManager manager, Component parent) {
        if (random.nextDouble() < 0.02) {
            GameEvent event = allEvents.get(random.nextInt(allEvents.size()));
            int choice = JOptionPane.showOptionDialog(parent,
                    event.getMessage(),
                    event.getTitle(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    event.getOptions(),
                    event.getOptions()[0]);

            event.apply(choice, manager);
        }
    }
}
