package events;

import model.GameManager;

public class HouseUpgradeEvent implements GameEvent {
    public String getTitle() {
        return "Housing Boom";
    }

    public String getMessage() {
        return "Due to a housing boom, upgraded houses provide double the population bonus for 10 days.";
    }

    public String[] getOptions() {
        return new String[]{"OK"};
    }

    public void apply(int option, GameManager manager) {
        manager.setHouseUpgradeBonusDays(10);
    }
}