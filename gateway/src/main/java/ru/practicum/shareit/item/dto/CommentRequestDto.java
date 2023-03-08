package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotBlank;

public class CommentRequestDto {

    @NotBlank
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
