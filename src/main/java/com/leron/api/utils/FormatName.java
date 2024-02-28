package com.leron.api.utils;

import org.springframework.stereotype.Component;

@Component
public class FormatName {
    public static String firstUpper(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        StringBuilder formattedName = new StringBuilder();
        String[] words = name.trim().split("\\s+");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i > 0) {
                formattedName.append(" ");
            }
            formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }

        return formattedName.toString();
    }
}
