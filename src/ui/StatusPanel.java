package ui;

import model.GameManager;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private GameManager manager;

    private JLabel lblPopulation;
    private JLabel lblMoney;
    private JLabel lblFood;
    private JLabel lblPower;
    private JLabel lblIron;
    private JLabel lblConcrete;
    private JLabel lblGlass;
    private JLabel lblDay;


    public StatusPanel(GameManager manager) {
        this.manager = manager;

        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension(200, 300));
        setBackground(Color.DARK_GRAY);

        Font font = new Font("Arial", Font.BOLD, 16);

        lblPopulation = createLabel("Population: 0", font);
        lblMoney = createLabel("Money: 0", font);
        lblFood = createLabel("Food: 0", font);
        lblPower = createLabel("Power: 0", font);
        lblIron = createLabel("Iron: 0", font);
        lblConcrete = createLabel("Concrete: 0", font);
        lblGlass = createLabel("Glass: 0", font);
        lblDay = createLabel("Day: 0", font);

        add(lblPopulation);
        add(lblMoney);
        add(lblFood);
        add(lblPower);
        add(lblIron);
        add(lblConcrete);
        add(lblGlass);
        add(lblDay);

        Timer refresh = new Timer(500, e -> update());
        refresh.start();
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public void update() {
        lblPopulation.setText("Population: " + manager.getPopulation());
        lblMoney.setText("Money: " + manager.getMoney());
        lblFood.setText("Food: " + manager.getFood());
        lblPower.setText("Power: " + manager.getPower());
        lblIron.setText("Iron: " + manager.getIron());
        lblConcrete.setText("Concrete: " + manager.getConcrete());
        lblGlass.setText("Glass: " + manager.getGlass());
        lblDay.setText("Day: " + manager.getDayCount());
    }
}
