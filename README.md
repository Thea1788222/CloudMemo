# CloudMemo

## 📌 项目简介

本项目是一个 **具备云端同步功能的记事本 Android 应用**。
用户可以在 APP 中创建、编辑、删除笔记，数据首先存储在本地的 **SQLite 数据库** 中。
当设备连接网络时，APP 会与服务器进行 **双向数据同步**：

* 本地新增/修改的笔记会上传到服务器（Java Web）
* 云端更新的笔记也会下载到本地
* 最终保证“本地 SQLite”与“云端 MySQL”始终保持一致

该项目主要用于学习和实践：

* 移动端本地数据库（SQLite）
* HTTP 网络请求
* 后端接口设计（Java Web）
* 服务器数据库（MySQL）
* 数据同步机制（异步、双向、时间戳增量同步）

---

## 🎯 项目目标

* 掌握 Android 中使用 SQLite 存储数据
* 实现一个可离线使用的记事本功能
* 学习如何通过网络将本地数据同步到云端
* 了解同步机制的设计：上传、下载、冲突处理、时间戳管理

---

## 🧩 项目功能说明

### 📱 Android APP 端

* 新建笔记
* 编辑笔记
* 删除笔记
* 本地 SQLite 数据库存储
* 手动点击“同步”按钮（或自动同步）
* 本地数据与云端双向同步

### ☁ 服务器端（Java Web）

* 提供 REST API

  * `/sync/upload` 上传本地更新数据
  * `/sync/download` 下载云端更新数据
* 使用 MySQL 作为云端数据库
* 保存来自 APP 的笔记内容、更新时间等信息

---

## 🔄 数据同步机制（核心逻辑）

同步流程分为两部分：

### 1️⃣ 上传（Upload）

APP 读取本地 SQLite 中“未同步或最近修改”的笔记，并通过 HTTP POST 上传到服务器。
服务器将这些数据写入 MySQL。

### 2️⃣ 下载（Download）

APP 会携带“上次同步时间 last_sync_time”向服务器发起请求。
服务器返回云端自 last_sync_time 之后更新过的所有笔记。
APP 将这些数据写入本地 SQLite。

### ✔ 最终结果

本地数据库与云端数据库保持一致。

---

## 🗄 数据库设计

### 📱 本地 SQLite（APP）

`note` 表：

| 字段          | 类型                  | 说明      |
| ----------- | ------------------- | ------- |
| id          | INTEGER PRIMARY KEY | 笔记 ID   |
| content     | TEXT                | 笔记内容    |
| update_time | LONG                | 最后修改时间戳 |

### ☁ 云端 MySQL（服务器）

`note` 表（字段与 SQLite 对齐）：

| 字段          | 类型              | 说明    |
| ----------- | --------------- | ----- |
| id          | INT PRIMARY KEY | 笔记 ID |
| content     | VARCHAR         | 笔记内容  |
| update_time | BIGINT          | 更新时间戳 |

---

## 🌐 后端接口设计

### ① 上传接口

```
POST /sync/upload
Body: JSON 数组（笔记列表）
```

### ② 下载接口

```
GET /sync/download?since=timestamp
```

返回自指定 timestamp 之后更新的所有笔记。

---

## 🛠 技术栈

### Android 端

* Java
* SQLite
* Retrofit（网络请求库）
* Gson（JSON 序列化）

### 服务器端

* Java Web / Spring Boot
* MySQL
* MyBatis
* JSON 解析库

---

## 🚀 项目开发计划（阶段划分）

### 💽 **第一阶段：云端 MySQL 和 Java Web 接口搭建**

* 创建服务器数据库
* 实现上传/下载接口

### 📱 **第二阶段：Android 本地数据库**

* 创建 SQLite 表结构
* 实现增删改查

### 🔗 **第三阶段：APP 调用服务器接口**

* Retrofit 请求
* 上传本地变化
* 下载云端更新

### 🔄 **第四阶段：同步逻辑实现**

* 上传 + 下载 + 合并
* 处理最后同步时间

### 🎉 **第五阶段：UI 完成与测试**

* 记事本编辑页面
* 同步按钮
* 测试在线/离线同步


## 📚 **第六阶段：实验报告书写**

* 功能截图
* 架构图
* 同步流程图
* 关键代码
