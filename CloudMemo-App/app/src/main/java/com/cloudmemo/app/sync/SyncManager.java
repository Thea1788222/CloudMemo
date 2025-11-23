package com.cloudmemo.app.sync;

import android.content.Context;
import android.util.Log;

import com.cloudmemo.app.data.db.DatabaseHelper;
import com.cloudmemo.app.data.model.Note;
import com.cloudmemo.app.network.ApiService;
import com.cloudmemo.app.network.RetrofitClient;
import com.cloudmemo.app.network.dto.NoteDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncManager {

    private final Context context;
    private final DatabaseHelper db;
    private final ApiService api;

    public SyncManager(Context context) {
        this.context = context;
        this.db = new DatabaseHelper(context);
        this.api = RetrofitClient.getApiService();
    }

    // -------------------------------------
    // 触发同步：上传 + 下载
    // -------------------------------------
    public void sync() {
        uploadLocalNotes();
    }

    // -------------------------------------
    // Step 1：上传本地未同步的笔记
    // -------------------------------------
    private void uploadLocalNotes() {
        List<Note> unsyncedNotes = db.getUnsyncedNotes();
        if (unsyncedNotes.isEmpty()) {
            Log.d("Sync", "没有未同步的笔记，直接下载服务器数据");
            downloadServerNotes();
            return;
        }

        // 转换成 NoteDTO
        List<NoteDTO> dtos = new ArrayList<>();
        for (Note n : unsyncedNotes) {
            dtos.add(new NoteDTO(n.getId(), n.getContent(), n.getUpdatedAt()));
        }

        Log.d("Sync", "准备上传笔记: " + dtos.size());
        api.uploadNotes(dtos).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.d("Sync", "上传完成, 数量=" + response.body());

                    // 上传成功 → 标记本地为已同步
                    db.markNotesAsSynced(unsyncedNotes);

                    // 上传完后执行下载
                    downloadServerNotes();
                } else {
                    Log.e("Sync", "上传失败: 响应码=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Sync", "上传失败", t);
            }
        });
    }

    // -------------------------------------
    // Step 2：下载服务器的增量更新
    // -------------------------------------
    private void downloadServerNotes() {
        long lastSync = SyncPreferences.getLastSyncTime(context);

        Log.d("Sync", "开始下载服务器笔记, since=" + lastSync);
        api.downloadNotes(lastSync).enqueue(new Callback<List<NoteDTO>>() {
            @Override
            public void onResponse(Call<List<NoteDTO>> call, Response<List<NoteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NoteDTO> notes = response.body();
                    Log.d("Sync", "下载笔记数量: " + notes.size());

                    // 写入 SQLite
                    for (NoteDTO dto : notes) {
                        Note n = new Note(dto.getId(), dto.getContent(),
                                System.currentTimeMillis(), dto.getUpdatedAt(), true);
                        db.insertOrUpdate(n);
                    }

                    // 更新 lastSyncTime
                    SyncPreferences.setLastSyncTime(context, System.currentTimeMillis());
                    Log.d("Sync", "同步完成");
                } else {
                    Log.e("Sync", "下载失败: 响应码=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NoteDTO>> call, Throwable t) {
                Log.e("Sync", "下载失败", t);
            }
        });
    }
}
