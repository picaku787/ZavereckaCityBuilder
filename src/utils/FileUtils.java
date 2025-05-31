package utils;

import model.GameManager;
import java.io.*;
/**
 * Utility class for saving and loading game state from a file.
 */
public class FileUtils {

    /**
     * Saves the game state to a specified file.
     *
     * @param manager  the GameManager instance to serialize
     * @param filename the destination filename
     */
    public static void saveGame(GameManager manager, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a game state from a specified file.
     *
     * @param filename the filename to load from
     * @return the deserialized GameManager instance, or null if loading fails
     */
    public static GameManager loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameManager) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
