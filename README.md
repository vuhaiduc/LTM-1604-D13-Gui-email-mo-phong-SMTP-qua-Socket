<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   Gửi email mô phỏng SMTP qua Socket
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---

## 📖 1. Giới thiệu hệ thống
Đề tài xây dựng ứng dụng **gửi email mô phỏng giao thức SMTP qua TCP Socket**.  
Hệ thống gồm 2 thành phần:

- **Client GUI (Java Swing)**: nhập Host, Port, Người gửi, Người nhận, Tiêu đề, Nội dung; gửi lệnh SMTP qua Socket.  
- **SMTP Server (Java)**: lắng nghe port (mặc định 2525), xử lý các lệnh `HELO`, `MAIL FROM`, `RCPT TO`, `DATA`, `QUIT` và lưu email nhận được vào file hệ thống.

Kiến trúc hệ thống:

```txt
+-----------------+                     +------------------+
|  CLIENT GUI     |  TCP Socket / SMTP  |   SMTP SERVER    |
| (Người dùng)    |-------------------->| (Nhận, phân tích)|
| Nhập Email      |<--------------------| (Xử lý SMTP)     |
| To/From/Subject |  Phản hồi SMTP      |                  |
+-----------------+                     +------------------+
        |                                       |
        |                                       |
        v                                       v
+---------------------+                +-----------------------+
| Thông báo trạng thái|                |   Bộ ghi File Email   |
| gửi thành công/     |                | (.eml / .txt / log)   |
| thất bại            |                +-----------------------+
+---------------------+

```
Luồng hoạt động:

- Người dùng mở GUI, kết nối server.  
- GUI gửi các lệnh SMTP → server nhận, phản hồi và ghi nội dung email vào file.  
- Toàn bộ truyền thông dựa trên giao thức TCP Socket.

---

## ⚙️ 2. Công nghệ sử dụng
Ứng dụng được phát triển bằng:
- **Ngôn ngữ lập trình**: <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"></a>
- **Thư viện UI**: <a href="https://docs.oracle.com/javase/tutorial/uiswing/"><img src="https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white" alt="Swing"></a>
- **Socket**: <img src="https://img.shields.io/badge/TCP%20Socket-000000?style=for-the-badge" alt="Socket">

- **Kiến trúc**:  
  - `Client`: giao diện người dùng (SMTPClientGUI).  
  - `Server`: lắng nghe socket, xử lý SMTP, lưu email ra file.  

---

## 🖼️ 3. Một số hình ảnh hệ thống 

<p align="center">
    <em>Giao diện ứng dụng Client (nhập Host, Port, Người gửi, Người nhận, Tiêu đề, Nội dung…)</em><br/>
    <img width="1220" height="517" alt="UI Client" src="https://github.com/user-attachments/assets/8dcd1ea8-59f9-47c5-888d-b7b70302babe" />
</p>

<p align="center">
    <em>Giao diện console Server (hiển thị log và lưu email)</em><br/>
    <img width="1920" height="1017" alt="UI Server" src="https://github.com/user-attachments/assets/166e9262-6add-4697-94e7-4aa06f6675d9" />
</p>

<p align="center">
    <em>Email được lưu ra file trong thư mục /emails</em><br/>
    <img width="1514" height="777" alt="File Email" src="https://github.com/user-attachments/assets/9a712b83-8481-4d91-a6e3-59414e9c9bc1" />
</p>

---

## 🛠️ 4. Các bước cài đặt
1. **Clone source code**  
   ```bash
   git clone https://github.com/yourusername/smtp-socket-demo.git
   cd smtp-socket-demo

2. **Mở project trong IDE**
3. **Cấu hình JDK**
4. **Chạy Server**
5. **Chạy Client GUI**
6. **Kết nối và gửi email**
7. **Kiểm tra file email**















