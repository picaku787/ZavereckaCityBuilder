package events;

import model.GameManager;

/**
 * Event representing Material inflation that causes the price of materials to escalate.
 */
public class MaterialInflationEvent implements GameEvent {
    public String getTitle() {
        return "Material Inflation";
    }

    public String getMessage() {
        return "Material prices have escalated! For the next 10 days, material costs are 4× higher.";
    }

    public String[] getOptions() {
        return new String[]{"OK"};
    }

    /**
     * Applies the material inflation effect by setting inflation days.
     */
    public void apply(int option, GameManager manager) {
        manager.setMaterialInflationDays(10);
    }
}