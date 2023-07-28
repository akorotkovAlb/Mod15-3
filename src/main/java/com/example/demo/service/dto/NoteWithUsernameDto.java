package com.example.demo.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NoteWithUsernameDto {

    private UUID id;
    private String title;
    private String content;
    private String username;
}
