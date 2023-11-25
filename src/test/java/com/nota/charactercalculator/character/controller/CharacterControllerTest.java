package com.nota.charactercalculator.character.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nota.charactercalculator.character.model.CharacterText;
import com.nota.charactercalculator.character.service.CharacterServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CharacterController.class)
class CharacterControllerTest {

    @MockBean
    private CharacterServiceImpl characterService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final String inputText = "aaaaabcccc";


    private String getText() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 8000; i++) {
            for (int j = 97; j <= 121; j++) {
                builder.append((char) j);

            }
        }

        builder.append("a");

        return builder.toString();
    }

    @Test
    void countCharacters() throws Exception {
        CharacterText text = new CharacterText();
        text.setText(inputText);
        String expectedOutput = "\"a\": 5, \"c\": 4, \"b\": 1";

        when(characterService.cauntCharacters(any(CharacterText.class))).thenReturn(expectedOutput);

        String actualOutput = mvc.perform(post("/characters/frequency")
                        .content(mapper.writeValueAsString(text))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expectedOutput, actualOutput);
        verify(characterService, times(1)).cauntCharacters(any(CharacterText.class));
    }

    @Test
    void countCharacters_whenTextExceedsSize_theReturnBadRequest() throws Exception {
        CharacterText text = new CharacterText();
        text.setText(getText());
        String messageError = String.format(
                "Field: text, Error: The size should be in the range from 2 to 200_000, Actual length: %s",
                text.getText().length());

        mvc.perform(post("/characters/frequency")
                        .content(mapper.writeValueAsString(text))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(messageError));

        verify(characterService, never()).cauntCharacters(any(CharacterText.class));
    }

    @Test
    void countCharacters_whenTextIsEmpty_theReturnBadRequest() throws Exception {
        CharacterText emptyText = new CharacterText();
        emptyText.setText("   ");

        String messageError = String.format(
                "Field: text, Error: The text should not be empty, Actual length: %s", 0);

        mvc.perform(post("/characters/frequency")
                        .content(mapper.writeValueAsString(emptyText))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(messageError))
                .andReturn();

        verify(characterService, never()).cauntCharacters(any(CharacterText.class));
    }


    @Test
    void getCountedCharacters() throws Exception {
        String expectedOutput = "\"a\": 5, \"c\": 4, \"b\": 1";
        when(characterService.cauntCharacters(any(CharacterText.class))).thenReturn(expectedOutput);

        String actualOutput = mvc.perform(get("/characters/frequency")
                        .param("text", inputText))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expectedOutput, actualOutput);
        verify(characterService, times(1)).cauntCharacters(any(CharacterText.class));
    }

    @Test
    void getCountedCharacters_WhenTextIsEmpty_ThenReturnBadRequest() throws Exception {
        String emptyText = "  ";

         mvc.perform(get("/characters/frequency")
                        .param("text", emptyText))
                .andDo(print())
                .andExpect(status().isBadRequest());

         verify(characterService, never()).cauntCharacters(any(CharacterText.class));
    }

    @Test
    void getCountedCharacters_WhenTextExceedsSize_ThenReturnBadRequest() throws Exception {
        String longText = getText().substring(199750);

        mvc.perform(get("/characters/frequency")
                        .param("text", longText))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(characterService, never()).cauntCharacters(any(CharacterText.class));
    }
}