package com.dog_broad;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class MorseCodeGUI extends JFrame {
    private final JTextArea italianTextArea;
    private final JTextArea englishTextArea;
    private final JTextArea morseTextArea;
    private final JButton translateItalianToEnglishButton;
    private final JButton translateToMorseButton;
    private final JButton translateToEnglishButton;
    private final JButton playMorseButton;
    private final JButton stopMorseButton;
    private final JButton saveTextButton;
    private final JButton saveMorseButton;
    private JSlider speedSlider;
    private JSlider pitchSlider;
    private JSlider volumeSlider;

    public MorseCodeGUI() {
        IconFontSwing.register(FontAwesome.getIconFont());

        setTitle("Morse Code Translator");
        setSize(1400, 1100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        italianTextArea = new JTextArea();
        englishTextArea = new JTextArea();
        morseTextArea = new JTextArea();
        translateItalianToEnglishButton = new JButton("Translate to English", IconFontSwing.buildIcon(FontAwesome.ARROW_RIGHT, 25));
        translateItalianToEnglishButton.setFont(new Font("Arial", Font.BOLD, 20));
        translateToMorseButton = new JButton("Translate to Morse", IconFontSwing.buildIcon(FontAwesome.ARROW_RIGHT, 25));
        translateToMorseButton.setFont(new Font("Arial", Font.BOLD, 20));
        translateToEnglishButton = new JButton("Translate to English", IconFontSwing.buildIcon(FontAwesome.ARROW_LEFT, 25));
        translateToEnglishButton.setFont(new Font("Arial", Font.BOLD, 20));
        playMorseButton = new JButton("Play Morse Code", IconFontSwing.buildIcon(FontAwesome.PLAY, 25));
        stopMorseButton = new JButton("Stop Morse Code", IconFontSwing.buildIcon(FontAwesome.STOP, 25));
        saveTextButton = new JButton("Save Text", IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 25));
        saveMorseButton = new JButton("Save Morse", IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 25));
       

        JLabel italianLabel = new JLabel("Testo Italiano");
        italianTextArea.setFont(new Font("SansSerif", Font.PLAIN, 25));
        JLabel englishLabel = new JLabel("Testo Inglese");
        englishTextArea.setFont(new Font("SansSerif", Font.PLAIN, 25));
        JLabel morseLabel = new JLabel("Morse Code");
        morseTextArea.setFont(new Font("Monospaced", Font.PLAIN, 25));

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        addMenuBar(topPanel);
        add(topPanel, BorderLayout.NORTH);

        // Create panels to hold text areas and labels
        JPanel italianPanel = new JPanel(new BorderLayout());
        italianPanel.add(italianLabel, BorderLayout.NORTH);
        italianTextArea.setLineWrap(true);
        italianTextArea.setWrapStyleWord(true);
        italianPanel.add(new JScrollPane(italianTextArea), BorderLayout.CENTER);

        JPanel englishPanel = new JPanel(new BorderLayout());
        englishPanel.add(englishLabel, BorderLayout.NORTH);
        englishTextArea.setLineWrap(true);
        englishTextArea.setWrapStyleWord(true);
        englishPanel.add(new JScrollPane(englishTextArea), BorderLayout.CENTER);

        JPanel morsePanel = new JPanel(new BorderLayout());
        morsePanel.add(morseLabel, BorderLayout.NORTH);
        morseTextArea.setLineWrap(true);
        morseTextArea.setWrapStyleWord(true);
        morsePanel.add(new JScrollPane(morseTextArea), BorderLayout.CENTER);

        // Add panels to the main layout
        JPanel textPanel = new JPanel(new GridLayout(1, 3));
        textPanel.add(italianPanel);
        textPanel.add(englishPanel);
        textPanel.add(morsePanel);
        add(textPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3));
        buttonPanel.add(translateItalianToEnglishButton);
        buttonPanel.add(translateToMorseButton);
        buttonPanel.add(translateToEnglishButton);
        buttonPanel.add(playMorseButton);
        buttonPanel.add(stopMorseButton);
        buttonPanel.add(saveTextButton);
        buttonPanel.add(saveMorseButton);
        add(buttonPanel, BorderLayout.SOUTH);


        translateItalianToEnglishButton.addActionListener(e -> {
            try {
                String italianText = italianTextArea.getText();
            String englishText = MorseCodeTranslator.ItalianToEnglish(italianText);
               englishTextArea.setText(englishText);
            } catch (Exception ex) {
                showErrorDialog("Error translating to English: " + ex.getMessage());
            }
        });

        translateToMorseButton.addActionListener(e -> {
            try {
                String englishText = englishTextArea.getText();
                String morseText = MorseCodeTranslator.toMorse(englishText);
                morseTextArea.setText(morseText);
            } catch (Exception ex) {
                showErrorDialog("Error translating to Morse code: " + ex.getMessage());
            }
        });

        translateToEnglishButton.addActionListener(e -> {
            try {
                String morseText = morseTextArea.getText();
                String englishText = MorseCodeTranslator.toEnglish(morseText);
                englishTextArea.setText(englishText);
            } catch (Exception ex) {
                showErrorDialog("Error translating to English: " + ex.getMessage());
            }
        });

        playMorseButton.addActionListener(e -> {
            try {
                new Thread(() -> TranslationHelper.playMorseSound(morseTextArea.getText())).start();
            } catch (Exception ex) {
                showErrorDialog("Error playing Morse code sound: " + ex.getMessage());
            }
        });

        stopMorseButton.addActionListener(e -> {
            try {
                TranslationHelper.stopPlaying();
            } catch (Exception ex) {
                showErrorDialog("Error stopping Morse code sound: " + ex.getMessage());
            }
        });

        saveTextButton.addActionListener(e -> {
            try {
                saveToFile(englishTextArea.getText(), "text.txt");
            } catch (Exception ex) {
                showErrorDialog("Error saving text: " + ex.getMessage());
            }
        });

        saveMorseButton.addActionListener(e -> {
            try {
                saveToFile(morseTextArea.getText(), "morse.txt");
            } catch (Exception ex) {
                showErrorDialog("Error saving Morse code: " + ex.getMessage());
            }
        });


        speedSlider.addChangeListener(e -> {
            try {
                TranslationHelper.setDotDuration(speedSlider.getValue());
            } catch (Exception ex) {
                showErrorDialog("Error setting speed: " + ex.getMessage());
            }
        });

        pitchSlider.addChangeListener(e -> {
            try {
                TranslationHelper.setPitch(pitchSlider.getValue());
            } catch (Exception ex) {
                showErrorDialog("Error setting pitch: " + ex.getMessage());
            }
        });

        volumeSlider.addChangeListener(e -> {
            try {
                TranslationHelper.setVolume(volumeSlider.getValue() / 100f);
            } catch (Exception ex) {
                showErrorDialog("Error setting volume: " + ex.getMessage());
            }
        });

    }

    private static DefaultTableModel getDefaultTableModel() {
        String[] columnNames = {"Character", "Morse Code"};
        Object[][] data = {
                {'A', ".-"},
                {'B', "-..."},
                {'C', "-.-."},
                {'D', "-.."},
                {'E', "."},
                {'F', "..-."},
                {'G', "--."},
                {'H', "...."},
                {'I', ".."},
                {'J', ".---"},
                {'K', "-.-"},
                {'L', ".-.."},
                {'M', "--"},
                {'N', "-."},
                {'O', "---"},
                {'P', ".--."},
                {'Q', "--.-"},
                {'R', ".-."},
                {'S', "..."},
                {'T', "-"},
                {'U', "..-"},
                {'V', "...-"},
                {'W', ".--"},
                {'X', "-..-"},
                {'Y', "-.--"},
                {'Z', "--.."},
                {'0', "-----"},
                {'1', ".----"},
                {'2', "..---"},
                {'3', "...--"},
                {'4', "....-"},
                {'5', "....."},
                {'6', "-...."},
                {'7', "--..."},
                {'8', "---.."},
                {'9', "----."},
                {' ', "/"},  // Space in Morse code
                {'.', ".-.-.-"},  // Period
                {',', "--..--"},  // Comma
                {'?', "..--.."},  // Question mark
                {'!', "-.-.--"},  // Exclamation mark
                {'"', ".-..-."},  // Double quote
                {'\'', ".----."},  // Single quote or apostrophe
                {'(', "-.--."},  // Left parenthesis
                {')', "-.--.-"},  // Right parenthesis
                {'&', ".-..."},  // Ampersand
                {':', "---..."},  // Colon
                {';', "-.-.-."},  // Semicolon
                {'=', "-...-"},  // Equal sign
                {'+', ".-.-."},  // Plus sign
                {'-', "-....-"},  // Hyphen or dash
                {'_', "..--.-"},  // Underscore
                {'$', "...-..-"},  // Dollar sign
                {'@', ".--.-."}  // At symbol
        };

        // Create a non-editable table model
        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
    }


    private void addMenuBar(JPanel topPanel) {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveTextMenuItem = new JMenuItem("Save Text");
        JMenuItem saveMorseMenuItem = new JMenuItem("Save Morse");

        saveTextMenuItem.addActionListener(e -> saveToFile(englishTextArea.getText(), "text.txt"));
        saveMorseMenuItem.addActionListener(e -> saveToFile(morseTextArea.getText(), "morse.txt"));

        fileMenu.add(saveTextMenuItem);
        fileMenu.add(saveMorseMenuItem);
        menuBar.add(fileMenu);

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");

        speedSlider = createSlider("Speed", 50, 400, 200);
        pitchSlider = createSlider("Pitch", 500, 2000, 1000);
        volumeSlider = createSlider("Volume", 0, 100, 50);

        settingsMenu.add(createSliderPanel("Speed", speedSlider, "ms"));
        settingsMenu.add(createSliderPanel("Pitch", pitchSlider, "Hz"));
        settingsMenu.add(createSliderPanel("Volume", volumeSlider, "%"));
        menuBar.add(settingsMenu);

        topPanel.add(menuBar);
    }


    private JSlider createSlider(String pitch, int min, int max, int value) {
        JSlider slider = new JSlider(min, max, value);
        slider.setMajorTickSpacing((max - min) / 5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private JPanel createSliderPanel(String name, JSlider slider, String unit) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(name), BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(new JLabel(unit), BorderLayout.EAST);
        return panel;
    }

    private void saveToFile(String content, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(content);
                JOptionPane.showMessageDialog(this, "File saved: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                showErrorDialog("Error saving file: " + e.getMessage());
            }
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MorseCodeGUI().setVisible(true));
    }
}
