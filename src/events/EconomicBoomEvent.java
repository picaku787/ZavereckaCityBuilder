package events;

import model.GameManager;

public class EconomicBoomEvent implements GameEvent {

    @Override
    public String getTitle() {
        return "Economic Boom!";
    }

    @Override
    public String getMessage() {
        return "The economy is booming, you get more money from you people for 5 days!";
    }

    @Override
    public String[] getOptions() {
        return new String[]{"OK"};
    }

    @Override
    public void apply(int option, GameManager manager) {

    }
}
