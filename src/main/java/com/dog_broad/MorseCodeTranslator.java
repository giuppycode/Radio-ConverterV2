package com.dog_broad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

public class MorseCodeTranslator {
    private static final Map<Character, String> englishToMorse = new HashMap<>();
    private static final Map<String, Character> morseToEnglish = new HashMap<>();
    public static final String ItalianToEnglish = null;

    static {
        // Initialize the mappings
        String[] english = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.,?!'()&:;=+-_$@".split("");
        String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--",
                "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
                "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", ".-.-.-", "--..--",
                "..--..", "-.-.--", ".-..-.", ".----.", "-.--.", "-.--.-", ".-...", "---...", "-.-.-.", "-...-",
                ".-.-.", "-....-", "..--.-", "...-..-", ".--.-."};

        for (int i = 0; i < english.length; i++) {
            englishToMorse.put(english[i].charAt(0), morse[i]);
            morseToEnglish.put(morse[i], english[i].charAt(0));
        }
    }

    public static String toMorse(String englishText) {
        StringBuilder morseText = new StringBuilder();
        for (char c : englishText.toUpperCase().toCharArray()) {
            if (englishToMorse.containsKey(c)) {
                morseText.append(englishToMorse.get(c)).append(" ");
            } else {
                morseText.append(" ");
            }
        }
        return morseText.toString().trim();
    }

    public static String toEnglish(String morseText) {
        StringBuilder englishText = new StringBuilder();
        for (String morseChar : morseText.split(" ")) {
            if (morseToEnglish.containsKey(morseChar)) {
                englishText.append(morseToEnglish.get(morseChar));
            } else {
                englishText.append(" ");
            }
        }
        return englishText.toString().trim();
    }

    public static String ItalianToEnglish(String italianText)  {
            String englishText = "";
            try {
                englishText = translate("it", "en", italianText);
            } catch (IOException e) {
                englishText = "Translation error: " + e.getMessage();
            }
            return englishText;
        }   

    private static String translate(String langFrom, String langTo, String text) throws IOException {

        String urlStr = "https://script.google.com/macros/s/AKfycbwk8IOAm2bkyClriHU_blQWXFdfOtPB3XAfqTLv6ANJQk-zs7RPznWP0AXyx6Jv31Fv/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MorseCodeGUI().setVisible(true));
    }
}
