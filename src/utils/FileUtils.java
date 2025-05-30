package utils;

import model.GameManager;

import java.io.*;

public class FileUtils {

    public static void saveGame(GameManager manager, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameManager loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameManager) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
