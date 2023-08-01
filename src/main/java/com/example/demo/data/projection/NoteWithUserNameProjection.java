package com.example.demo.data.projection;

import java.util.UUID;

public interface NoteWithUserNameProjection {

    UUID getNoteId();
    String getTitle();
    String getContent();
    String getUsername();
}
