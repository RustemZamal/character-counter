package com.nota.charactercalculator.character.service;

import com.nota.charactercalculator.character.model.CharacterText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterServiceImplTest {

    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterServiceImpl();
    }

    @Test
    void testCountCharacters() {
        String testText = "aaaabccccddadbccde";
        String expectedText = "\"c\": 6,\"a\": 5,\"d\": 4,\"b\": 2,\"e\": 1";
        CharacterText text = new CharacterText();
        text.setText(testText);

        String actualText = characterService.cauntCharacters(text);

        assertEquals(expectedText, actualText);
    }
}