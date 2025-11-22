package com.cloudmemo.server.controller;

import com.cloudmemo.server.dto.NoteDTO;
import com.cloudmemo.server.entity.Note;
import com.cloudmemo.server.service.NoteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // 上传接口
    @PostMapping("/upload")
    public int uploadNotes(@RequestBody List<NoteDTO> noteDTOs) {
        List<Note> notes = noteDTOs.stream().map(dto -> {
            Note note = new Note();
            note.setId(dto.getId());
            note.setContent(dto.getContent());
            note.setUpdatedAt(dto.getUpdatedAt());
            note.setCreatedAt(System.currentTimeMillis());
            return note;
        }).collect(Collectors.toList());
        return noteService.saveNotes(notes).size();
    }

    // 下载接口
    @GetMapping("/download")
    public List<NoteDTO> downloadNotes(@RequestParam Long since) {
        return noteService.getNotesSince(since).stream().map(note -> {
            NoteDTO dto = new NoteDTO();
            dto.setId(note.getId());
            dto.setContent(note.getContent());
            dto.setUpdatedAt(note.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
