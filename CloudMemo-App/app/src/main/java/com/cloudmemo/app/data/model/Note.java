package com.cloudmemo.app.data.model;

public class Note {

    private long id;
    private String content;
    private long createdAt;
    private long updatedAt;
    private boolean synced;

    public Note() {
    }


    public Note(long id, String content, long createdAt, long updatedAt, boolean synced) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.synced = synced;
    }

    public Note(String content, long createdAt, long updatedAt, boolean synced) {
        this(-1, content, createdAt, updatedAt, synced);
    }

    // getter & setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
