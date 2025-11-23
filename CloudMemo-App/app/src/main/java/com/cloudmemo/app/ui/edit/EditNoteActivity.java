package com.cloudmemo.app.ui.edit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudmemo.app.R;
import com.cloudmemo.app.data.model.Note;
import com.cloudmemo.app.data.repository.NoteRepository;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editTextContent;
    private Button buttonSave;
    private NoteRepository repository;
    private long noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        repository = new NoteRepository(this);

        noteId = getIntent().getLongExtra("noteId", -1);
        if (noteId != -1) {
            Note note = repository.getAllNotes().stream()
                    .filter(n -> n.getId() == noteId)
                    .findFirst().orElse(null);
            if (note != null) {
                editTextContent.setText(note.getContent());
            }
        }

        buttonSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String content = editTextContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();
        if (noteId == -1) {
            Note note = new Note(content, now, now, false);
            repository.insertNote(note);
        } else {
            Note note = new Note(noteId, content, now, now, false);
            repository.updateNote(note);
        }
        finish();
    }
}
