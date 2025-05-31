package view;

import model.GameManager;
import ui.GameScreen;
import utils.FileUtils;

import javax.swing.*;


public class MainWindow extends JFrame {

    private GameManager manager;
    private GameScreen gameScreen;

    public MainWindow() {
        setTitle("City Builder");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        setupMenu();
        newGame();

        setVisible(true);
    }

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

    private void loadGame(GameManager gm) {
        if (gameScreen != null) {
            gameScreen.stopGameLoop();
        }

        manager = gm;
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
