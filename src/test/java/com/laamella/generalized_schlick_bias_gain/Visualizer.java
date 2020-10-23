package com.laamella.generalized_schlick_bias_gain;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

import static com.laamella.generalized_schlick_bias_gain.GainBiasDoubleFunctions.biasAndGain;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.Color.*;
import static java.lang.Double.parseDouble;
import static java.lang.Math.round;
import static javax.swing.SwingUtilities.invokeLater;

class Visualizer extends JFrame {

    Visualizer() {
        JTextField tTextField = new JTextField("0.5", 5);
        JTextField sTextField = new JTextField("8", 5);
        GraphPanel graph = new GraphPanel(
                () -> parseDouble(tTextField.getText()),
                () -> parseDouble(sTextField.getText()));

        tTextField.addActionListener(e -> graph.repaint());
        sTextField.addActionListener(e -> graph.repaint());

        Container contentPane = getContentPane();
        JPanel input = new JPanel();
        contentPane.add(input, NORTH);
        input.add(new JLabel("t:"));
        input.add(tTextField);
        input.add(new JLabel("s:"));
        input.add(sTextField);
        contentPane.add(graph, CENTER);
    }

    static class GraphPanel extends JPanel {
        private final Supplier<Double> tSupplier;
        private final Supplier<Double> sSupplier;

        GraphPanel(Supplier<Double> tSupplier, Supplier<Double> sSupplier) {
            this.tSupplier = tSupplier;
            this.sSupplier = sSupplier;
            setBackground(WHITE);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                int w = g.getClipBounds().width;
                int h = g.getClipBounds().height;
                g.setColor(YELLOW);
                int horizontalSections = 4;
                for (int line = 1; line < horizontalSections; line++) {
                    int y = line * (h / horizontalSections);
                    g.drawLine(0, y, w, y);
                }
                int verticalSections = 4;
                for (int line = 1; line < verticalSections; line++) {
                    int x = line * (w / verticalSections);
                    g.drawLine(x, 0, x, h);
                }
                g.setColor(BLACK);
                double t = tSupplier.get();
                double s = sSupplier.get();
                int subsample = 10;
                for (int i = 0; i < w * subsample; i++) {
                    double x = i / (double) (w * subsample);

                    g.setColor(BLACK);
                    int y = h - (int) round(biasAndGain(x, s, t) * h);
                    g.drawLine(i / subsample, y, i / subsample, y);
                }
            } catch (NumberFormatException e) {
                // It's okay.
            }
        }
    }

    public static void main(String[] args) {
        invokeLater(() -> {
            Visualizer visualizer = new Visualizer();
            visualizer.setDefaultCloseOperation(EXIT_ON_CLOSE);
            visualizer.setSize(400, 400);
            visualizer.setVisible(true);
        });
    }
}