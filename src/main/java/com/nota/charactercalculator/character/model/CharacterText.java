package com.nota.charactercalculator.character.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CharacterText {

    @NotBlank(message = "The text should not be empty")
    @Size(min = 2, max = 200_000, message = "The size should be in the range from 2 to 200_000")
    private String text;
}
