package com.cloudmemo.server.repository;

import com.cloudmemo.server.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUpdatedAtGreaterThan(Long timestamp);
}
