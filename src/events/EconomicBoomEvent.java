package events;

import model.GameManager;

/**
 * Event representing an economic boom that increases player's income for 5 days.
 */
public class EconomicBoomEvent implements GameEvent {

    @Override
    public String getTitle() {
        return "Economic Boom!";
    }

    @Override
    public String getMessage() {
        return "The economy is booming, you get more money from your people for 5 days!";
    }

    @Override
    public String[] getOptions() {
        return new String[]{"OK"};
    }

    /**
     * Applies the economic boom effect by setting bonus income days.
     */
    @Override
    public void apply(int option, GameManager manager) {
        manager.setEconomicBoomDays(5);
    }
}