package events;

import model.GameManager;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Manages triggering of random game events based on probability.
 */
public class EventManager {
    private ArrayList<GameEvent> allEvents = new ArrayList<>();
    private Random random = new Random();

    /**
     * Constructs an EventManager and initializes all game events.
     */
    public EventManager() {
        allEvents.add(new StormEvent());
        allEvents.add(new MaterialInflationEvent());
        allEvents.add(new HouseUpgradeEvent());
        allEvents.add(new TreasureEvent());
        allEvents.add(new RobberyEvent());
        allEvents.add(new EconomicBoomEvent());
    }

    /**
     * With a 2% chance, randomly triggers a game event and applies its effects.
     *
     * @param manager the game manager handling game state
     * @param parent  the component to attach dialogs to
     */
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
