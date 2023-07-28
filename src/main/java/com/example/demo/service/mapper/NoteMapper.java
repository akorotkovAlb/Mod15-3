package com.example.demo.service.mapper;

import com.example.demo.controller.request.V2.CreateNoteRequest;
import com.example.demo.controller.request.V2.UpdateNoteRequest;
import com.example.demo.controller.response.NoteResponse;
import com.example.demo.data.entity.NoteEntity;
import com.example.demo.data.entity.UserEntity;
import com.example.demo.data.projection.NoteWithUserNameProj;
import com.example.demo.service.dto.NoteDto;
import com.example.demo.service.dto.NoteWithUsernameDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    public List<NoteEntity> toNoteEntities(Collection<NoteDto> dtos) {
        return dtos.stream()
                .map(this::toNoteEntity)
                .collect(Collectors.toList());
    }

    public NoteEntity toNoteEntity(NoteDto dto) {
        NoteEntity entity = new NoteEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setUser(new UserEntity(dto.getUserId()));
        entity.setLastUpdatedDate(dto.getLastUpdatedDate());
        return entity;
    }

    public List<NoteDto> toNoteDtos(Collection<NoteEntity> entities) {
        return entities.stream()
                .map(this::toNoteDto)
                .collect(Collectors.toList());
    }

    public NoteDto toNoteDto(NoteEntity entity) {
        NoteDto dto = new NoteDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setUserId(entity.getUser().getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastUpdatedDate(entity.getLastUpdatedDate());
        return dto;
    }

    public List<NoteResponse> toNoteResponses(Collection<NoteDto> dtos) {
        return dtos.stream()
                .map(this::toNoteResponse)
                .collect(Collectors.toList());
    }

    public NoteResponse toNoteResponse(NoteDto dto) {
        NoteResponse response = new NoteResponse();
        response.setId(dto.getId());
        response.setTitle(dto.getTitle());
        response.setContent(dto.getContent());
        return response;
    }

    public List<NoteDto> requestsToNoteDtos(Collection<CreateNoteRequest> requests) {
        return requests.stream()
                .map(this::toNoteDto)
                .collect(Collectors.toList());
    }

    public NoteDto toNoteDto(CreateNoteRequest request) {
        NoteDto dto = new NoteDto();
        dto.setTitle(request.getTitle());
        dto.setContent(request.getContent());
        return dto;
    }

    public NoteDto toNoteDto(UUID id, UpdateNoteRequest request) {
        NoteDto dto = new NoteDto();
        dto.setId(id);
        dto.setTitle(request.getTitle());
        dto.setContent(request.getContent());
        return dto;
    }

    public NoteWithUsernameDto toNoteWithUsernameDto(NoteWithUserNameProj proj) {
        NoteWithUsernameDto dto = new NoteWithUsernameDto();
        dto.setId(proj.getNoteId());
        dto.setTitle(proj.getTitle());
        dto.setContent(proj.getContent());
        dto.setUsername(proj.getUsername());
        return dto;
    }

    public List<NoteWithUsernameDto> toNoteWithUsernameDtoList(Collection<NoteWithUserNameProj> projs) {
        return projs.stream().map(this::toNoteWithUsernameDto).toList();
    }

    public NoteWithUsernameDto toNoteWithUsernameDto(NoteEntity entity) {
        NoteWithUsernameDto dto = new NoteWithUsernameDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setUsername(entity.getUser().getUsername());
        return dto;
    }

    public List<NoteWithUsernameDto> toNoteWithUsernameDtoList1(Collection<NoteEntity> projs) {
        return projs.stream().map(this::toNoteWithUsernameDto).toList();
    }
}
