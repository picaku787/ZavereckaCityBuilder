package events;

import model.GameManager;
/**
 * Event representing a temporary boost to house population bonuses due to a housing boom.
 */
public class HouseUpgradeEvent implements GameEvent {

    @Override
    public String getTitle() {
        return "Housing Boom";
    }

    @Override
    public String getMessage() {
        return "Due to a housing boom, upgraded houses provide double the population bonus for 10 days.";
    }

    @Override
    public String[] getOptions() {
        return new String[]{"OK"};
    }

    /**
     * Applies the housing boom effect by setting the bonus duration.
     */
    @Override
    public void apply(int option, GameManager manager) {
        manager.setHouseUpgradeBonusDays(10);
    }
}