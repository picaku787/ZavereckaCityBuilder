package events;

import model.GameManager;

public class MaterialInflationEvent implements GameEvent {
    public String getTitle() {
        return "Material Inflation";
    }

    public String getMessage() {
        return "Material prices have skyrocketed! For the next 10 days, material costs are 4× higher.";
    }

    public String[] getOptions() {
        return new String[]{"OK"};
    }

    public void apply(int option, GameManager manager) {
        manager.setMaterialInflationDays(10);
    }
}