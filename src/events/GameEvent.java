package events;

import model.GameManager;

public interface GameEvent {
    String getTitle();
    String getMessage();
    String[] getOptions();
    void apply(int option, GameManager manager);
}
