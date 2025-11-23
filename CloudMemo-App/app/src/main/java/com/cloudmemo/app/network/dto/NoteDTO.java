package com.cloudmemo.app.network.dto;

public class NoteDTO {
    private long id;
    private String content;
    private long updatedAt;

    public NoteDTO() {}

    public NoteDTO(long id, String content, long updatedAt) {
        this.id = id;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    // getter & setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
