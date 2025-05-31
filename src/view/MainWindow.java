package view;

import model.GameManager;
import ui.GameScreen;
import utils.FileUtils;
import javax.swing.*;

/**
 * The main window of the app. Handles game initialization, menus, saving/loading,
 * and transitioning between game states.
 */
public class MainWindow extends JFrame {

    private GameManager manager;
    private GameScreen gameScreen;

    /**
     * Constructs the main game window and initializes a new game.
     */
    public MainWindow() {
        setTitle("City Builder");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        setupMenu();
        newGame();

        setVisible(true);
    }

    /**
     * Sets up the top menu bar with options for new game, save/load, and exit.
     */
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> newGame());

        JMenuItem save = new JMenuItem("Save Game");
        save.addActionListener(e -> FileUtils.saveGame(manager, "save.dat"));

        JMenuItem load = new JMenuItem("Load Game");
        load.addActionListener(e -> {
            GameManager loaded = FileUtils.loadGame("save.dat");
            if (loaded != null) {
                loadGame(loaded);
            }
        });

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        menu.add(newGame);
        menu.add(save);
        menu.add(load);
        menu.add(exit);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    /**
     * Starts a new game and resets the current state.
     */
    private void newGame() {
        if (gameScreen != null) {
            gameScreen.stopGameLoop();
        }

        manager = new GameManager();
        manager.reset();
        gameScreen = new GameScreen(manager, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    /**
     * Loads a saved game from file and refreshes the UI.
     *
     * @param manager the loaded GameManager instance
     */
    private void loadGame(GameManager manager) {
        if (gameScreen != null) {
            gameScreen.stopGameLoop();
        }


        gameScreen = new GameScreen(manager, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    public void showGameOverScreen() {
        JOptionPane.showMessageDialog(this, "You lost the game! Game over.");
        newGame();
    }
}
