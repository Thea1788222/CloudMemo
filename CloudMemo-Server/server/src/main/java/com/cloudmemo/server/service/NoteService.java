package com.cloudmemo.server.service;

import com.cloudmemo.server.entity.Note;
import com.cloudmemo.server.repository.NoteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotesSince(Long timestamp) {
        return noteRepository.findByUpdatedAtGreaterThan(timestamp);
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> saveNotes(List<Note> notes) {
        return noteRepository.saveAll(notes);
    }
}
