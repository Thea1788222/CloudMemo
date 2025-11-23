package com.cloudmemo.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.cloudmemo.app.data.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cloudmemo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_UPDATED_AT = "updatedAt";
    public static final String COLUMN_SYNCED = "synced";

    private static final String CREATE_TABLE_NOTES =
            "CREATE TABLE " + TABLE_NOTES + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CONTENT + " TEXT, "
                    + COLUMN_CREATED_AT + " INTEGER, "
                    + COLUMN_UPDATED_AT + " INTEGER, "
                    + COLUMN_SYNCED + " INTEGER"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
    // ------------------------
// 🔥 将数据库一行转成 Note 对象
// ------------------------
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();

        note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
        note.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
        note.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT)));

        // 从数据库 int 0/1 转换为 boolean
        int syncedInt = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYNCED));
        note.setSynced(syncedInt != 0);

        return note;
    }

    public List<Note> getUnsyncedNotes() {
        List<Note> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE synced = 0", null);

        while (cursor.moveToNext()) {
            Note n = cursorToNote(cursor);
            list.add(n);
        }
        cursor.close();
        return list;
    }

    public void markNotesAsSynced(List<Note> notes) {
        SQLiteDatabase db = getWritableDatabase();
        for (Note n : notes) {
            db.execSQL("UPDATE notes SET synced = 1 WHERE id = ?", new Object[]{n.getId()});
        }
    }

    public void insertOrUpdate(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(
                "INSERT OR REPLACE INTO notes(id, content, createdAt, updatedAt, synced) VALUES(?, ?, ?, ?, ?)",
                new Object[]{
                        note.getId(),
                        note.getContent(),
                        note.getCreatedAt(),
                        note.getUpdatedAt(),
                        note.isSynced() ? 1 : 0
                }
        );
    }


}
