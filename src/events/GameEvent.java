package events;

import model.GameManager;
/**
 * Interface representing a game event.
 * Each event has a title, a message, options for the player,
 * and a method to apply the selected option to the game state.
 */
public interface GameEvent {
    /**
     * Returns the title of the event.
     *
     * @return the event title
     */
    String getTitle();

    /**
     * Returns the message describing the event.
     *
     * @return the event message
     */
    String getMessage();

    /**
     * Returns the list of options available for the player to choose.
     *
     * @return an array of option strings
     */
    String[] getOptions();

    /**
     * Applies the effect of the chosen option to the game manager.
     *
     * @param option the selected option index
     * @param manager the game manager instance
     */
    void apply(int option, GameManager manager);
}
