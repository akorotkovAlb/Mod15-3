package com.example.demo.controller.controller;

import com.example.demo.controller.config.jwt.UserDetailsImpl;
import com.example.demo.controller.request.V2.note.CreateNoteRequest;
import com.example.demo.controller.request.V2.note.UpdateNoteRequest;
import com.example.demo.controller.response.NoteResponse;
import com.example.demo.service.dto.NoteDto;
import com.example.demo.service.dto.NoteWithUsernameDto;
import com.example.demo.service.exception.NoteNotFoundException;
import com.example.demo.service.mapper.NoteMapper;
import com.example.demo.service.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/V2/notes")
public class NoteControllerV2 {

    private static final String EXTENSION = "json";
    private static final String UPLOAD_FILE_EXCEPTION_TEXT =
            "Can not upload file with name %s. File must have .json extension.";

    @Autowired private NoteService noteService;
    @Autowired private NoteMapper noteMapper;
    @Autowired private ObjectMapper objectMapper;

    @GetMapping("/list")
    public ResponseEntity<List<NoteResponse>> noteList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteMapper.toNoteResponses(noteService.listAll()));
    }

    @GetMapping("/user/list")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<NoteWithUsernameDto>> getAllUserNotes() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteService.listAllUserNotesWithProjection(authentication.getId()));
    }

    @GetMapping("/user/graph/list")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<NoteWithUsernameDto>> getAllUserNotesWithEntityGraph() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteService.listAllUserNotesWithEntityGraph(authentication.getId()));
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<NoteResponse> createNote(@Valid @NotNull @RequestBody CreateNoteRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        NoteDto noteDto = noteMapper.toNoteDto(request);
        noteDto.setUserId(authentication.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteMapper.toNoteResponse(noteService.add(noteDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public void updateNote(
            @PathVariable("id") UUID id,
            @RequestBody @Valid @NotNull UpdateNoteRequest request) throws NoteNotFoundException {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        NoteDto noteDto = noteMapper.toNoteDto(id, request);
        noteDto.setUserId(authentication.getId());
        noteService.update(noteDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable("id") UUID id) throws NoteNotFoundException {
        NoteDto noteDto = noteService.getById(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(noteMapper.toNoteResponse(noteDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public void deleteNoteById(@PathVariable("id") UUID id) throws NoteNotFoundException {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        noteService.deleteById(id, authentication.getId());
    }

    @PostMapping("/upload")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<NoteResponse>> uploadFromFile(@RequestPart("file") MultipartFile file)
            throws IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();

        String extension = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        if (Objects.isNull(extension) || extension.isBlank() || !EXTENSION.equalsIgnoreCase(extension)) {
            throw new FileUploadException(String.format(UPLOAD_FILE_EXCEPTION_TEXT, file.getOriginalFilename()));
        }
        byte[] bytes = file.getBytes();
        List<NoteDto> notes = noteMapper
                .requestsToNoteDtos(Arrays.asList(objectMapper.readValue(bytes, CreateNoteRequest[].class)));
        notes.forEach(note -> note.setUserId(authentication.getId()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteMapper.toNoteResponses(noteService.addAll(notes)));
    }
}
