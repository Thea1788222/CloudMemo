package com.cloudmemo.app.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cloudmemo.app.data.db.DatabaseHelper;
import com.cloudmemo.app.data.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    private final DatabaseHelper dbHelper;

    public NoteRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTENT, note.getContent());
        values.put(DatabaseHelper.COLUMN_CREATED_AT, note.getCreatedAt());
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, note.getUpdatedAt());
        values.put(DatabaseHelper.COLUMN_SYNCED, note.isSynced() ? 1 : 0);

        long id = db.insert(DatabaseHelper.TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTENT, note.getContent());
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, note.getUpdatedAt());
        values.put(DatabaseHelper.COLUMN_SYNCED, note.isSynced() ? 1 : 0);

        db.update(DatabaseHelper.TABLE_NOTES, values, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NOTES, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NOTES,
                null, null, null, null, null,
                DatabaseHelper.COLUMN_UPDATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                notes.add(cursorToNote(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public List<Note> getNotesSince(long timestamp) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NOTES,
                null,
                DatabaseHelper.COLUMN_UPDATED_AT + ">?",
                new String[]{String.valueOf(timestamp)},
                null, null,
                DatabaseHelper.COLUMN_UPDATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                notes.add(cursorToNote(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT));
        long updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_AT));
        boolean synced = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SYNCED)) == 1;
        return new Note(id, content, createdAt, updatedAt, synced);
    }
}
