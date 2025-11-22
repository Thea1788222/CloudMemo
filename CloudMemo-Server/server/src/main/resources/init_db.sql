-- 创建 notes 表
CREATE TABLE IF NOT EXISTS notes (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT,
                                     content TEXT NOT NULL,
                                     updated_at BIGINT NOT NULL,
                                     created_at BIGINT NOT NULL
);
