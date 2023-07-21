package com.example.demo.controller.request.V2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNoteRequest extends NoteRequest {

    public UpdateNoteRequest () {
    }

    public UpdateNoteRequest (String title, String content) {
        super(title, content);
    }
}
