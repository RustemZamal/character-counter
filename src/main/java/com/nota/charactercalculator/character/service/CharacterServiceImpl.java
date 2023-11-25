package com.nota.charactercalculator.character.service;

import com.nota.charactercalculator.character.model.CharacterText;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CharacterServiceImpl implements CharacterService {

    @Override
    public String cauntCharacters(final CharacterText text) {
        Map<Character, Integer> frequencyChar = new HashMap<>();

        for (int i = 0; i < text.getText().length(); i++) {
            char c = text.getText().charAt(i);
            frequencyChar.put(c, frequencyChar.getOrDefault(c, 0) + 1);
        }

        return frequencyChar.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(entry -> "\"" + entry.getKey() + "\": " + entry.getValue())
                .collect(Collectors.joining(","));
    }
}
