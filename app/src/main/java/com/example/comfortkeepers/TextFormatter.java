package com.example.comfortkeepers;

/**
 * Utility class for text formatting operations
 */
public class TextFormatter {

    /**
     * Capitalizes the first letter of a string
     * @param text Input string to capitalize
     * @return String with first letter capitalized
     */
    public static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
     * Capitalizes the first letter of each word in a string
     * @param text Input string to capitalize
     * @return String with first letter of each word capitalized
     */
    public static String capitalizeWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        char[] chars = text.toLowerCase().toCharArray();
        boolean capitalize = true;

        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) {
                capitalize = true;
            } else if (capitalize) {
                chars[i] = Character.toUpperCase(chars[i]);
                capitalize = false;
            }
        }

        return new String(chars);
    }
}