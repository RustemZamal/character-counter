package com.nota.charactercalculator.character.controller;

import com.nota.charactercalculator.character.model.CharacterText;
import com.nota.charactercalculator.character.service.CharacterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/frequency")
    public String countCharacters(@RequestBody @Valid CharacterText text) {
        return characterService.cauntCharacters(text);
    }

    @GetMapping("/frequency")
    public String getCountedCharacters(
            @RequestParam @Size(min = 2, max = 250, message = "The size should be in the range from 2 to 250")
            @NotBlank(message = "The text should not be empty") final String text) {
        CharacterText charText = new CharacterText();
        charText.setText(text);
        return characterService.cauntCharacters(charText);
    }
}
