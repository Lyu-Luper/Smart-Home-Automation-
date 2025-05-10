package SmartHomeAutomationSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SmartHomeUI extends JFrame {
    private JLabel tempLabel, lightLabel, doorLabel;
    private JCheckBox acBox, lightBox, lockBox;
    private JTextField tempPrefField, lightPrefField;
    private JButton updateButton;
    private JTextArea logArea;

    private double temperature = 22.0;
    private int lightLevel = 300;
    private boolean doorOpen = false;

    public SmartHomeUI() {
        setTitle("Smart Home Automation System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sensorPanel = new JPanel(new GridLayout(3, 2));
        tempLabel = new JLabel("Temperature: " + temperature + " °C");
        lightLabel = new JLabel("Light Level: " + lightLevel + " lx");
        doorLabel = new JLabel("Door Status: " + (doorOpen ? "Open" : "Closed"));
        
        sensorPanel.add(tempLabel);
        sensorPanel.add(lightLabel);
        sensorPanel.add(doorLabel);

        JPanel devicePanel = new JPanel(new GridLayout(3, 2));
        acBox = new JCheckBox("Air Conditioner");
        lightBox = new JCheckBox("Lights");
        lockBox = new JCheckBox("Door Lock");
        
        tempPrefField = new JTextField("26");
        lightPrefField = new JTextField("400");

        devicePanel.add(new JLabel("AC Temp Pref (°C):"));
        devicePanel.add(tempPrefField);
        devicePanel.add(new JLabel("Light Pref (lx):"));
        devicePanel.add(lightPrefField);
        devicePanel.add(acBox);
        devicePanel.add(lightBox);

        updateButton = new JButton("Update Preferences and Simulate");
        logArea = new JTextArea();
        logArea.setEditable(false);

        add(sensorPanel, BorderLayout.NORTH);
        add(devicePanel, BorderLayout.CENTER);
        add(updateButton, BorderLayout.SOUTH);
        add(new JScrollPane(logArea), BorderLayout.EAST);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulateSensorData();
                applyPreferences();
            }
        });
    }

    private void simulateSensorData() {
        Random rand = new Random();
        temperature = 20 + rand.nextDouble() * 15;
        lightLevel = 100 + rand.nextInt(500);
        doorOpen = rand.nextBoolean();

        tempLabel.setText("Temperature: " + String.format("%.2f", temperature) + " °C");
        lightLabel.setText("Light Level: " + lightLevel + " lx");
        doorLabel.setText("Door Status: " + (doorOpen ? "Open" : "Closed"));
    }

    private void applyPreferences() {
        try {
            double tempPref = Double.parseDouble(tempPrefField.getText());
            int lightPref = Integer.parseInt(lightPrefField.getText());

            StringBuilder log = new StringBuilder();
            log.append("\n[Edge Controller Logs]\n");
            
            if (temperature > tempPref) {
                acBox.setSelected(true);
                log.append("Temperature = ").append(temperature).append(", AC turned ON\n");
            } else {
                acBox.setSelected(false);
                log.append("Temperature = ").append(temperature).append(", AC turned OFF\n");
            }

            if (lightLevel < lightPref) {
                lightBox.setSelected(true);
                log.append("Light Level = ").append(lightLevel).append(", Lights turned ON\n");
            } else {
                lightBox.setSelected(false);
                log.append("Light Level = ").append(lightLevel).append(", Lights turned OFF\n");
            }

            lockBox.setSelected(!doorOpen);
            log.append("Door is ").append(doorOpen ? "Open, Lock OFF\n" : "Closed, Lock ON\n");

            logArea.append(log.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid preference values!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SmartHomeUI().setVisible(true);
        });
    }
}

