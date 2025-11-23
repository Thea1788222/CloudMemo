package com.cloudmemo.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudmemo.app.R;
import com.cloudmemo.app.data.model.Note;
import com.cloudmemo.app.data.repository.NoteRepository;
import com.cloudmemo.app.ui.edit.EditNoteActivity;
import com.cloudmemo.app.sync.SyncManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private NoteRepository repository;
    private Button buttonAddNote;
    private Button buttonSync;
    private SyncManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化本地数据库
        repository = new NoteRepository(this);

        // 初始化同步管理器
        syncManager = new SyncManager(this);

        // RecyclerView 初始化
        recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(loadNotes());
        recyclerView.setAdapter(adapter);

        // 点击笔记，进入编辑页面
        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            intent.putExtra("noteId", note.getId());
            startActivity(intent);
        });

        // 新建笔记按钮
        buttonAddNote = findViewById(R.id.buttonAddNote);
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivity(intent);
        });

        // 同步按钮
        buttonSync = findViewById(R.id.buttonSync);
        buttonSync.setOnClickListener(v -> {
            syncManager.sync();
            Toast.makeText(this, "正在同步...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setNotes(loadNotes());  // 返回界面刷新列表
    }

    private List<Note> loadNotes() {
        return repository.getAllNotes();
    }
}
