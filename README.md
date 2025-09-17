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


# Giới thiệu hệ thống
# 📧 Ứng dụng Gửi Email Qua Socket – Mô phỏng SMTP

<div align="center">

<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Logo_DAI_NAM.png/1200px-Logo_DAI_NAM.png" alt="DaiNam University Logo" width="200">
</p>

<p>
<a href="https://fit.dainam.edu.vn"><img src="https://img.shields.io/badge/Made%20by%20AIoTLab-blue?style=for-the-badge"></a>
<a href="https://fit.dainam.edu.vn"><img src="https://img.shields.io/badge/Faculty%20of%20Information%20Technology-green?style=for-the-badge"></a>
<a href="https://dainam.edu.vn"><img src="https://img.shields.io/badge/DaiNam%20University-red?style=for-the-badge"></a>
</p>
</div>

<p align="center">
  <a href="#-architecture">Kiến trúc hệ thống</a> •
  <a href="#-features">Tính năng</a> •
  <a href="#-tech-stack">Công cụ</a> •
  <a href="#-installation">Cài đặt</a> •
  <a href="#-usage">Triển khai</a>
</p>

## 🏗️ Kiến trúc hệ thống

<p align="center">
  <img src="images/system_architecture.png" alt="System Architecture" width="800"/>
</p>

Hệ thống gồm 2 thành phần chính:

1. **💻 Client GUI**  
   - Ứng dụng Java Swing để người dùng nhập Host, Port, Người gửi, Người nhận, Tiêu đề, Nội dung.  
   - Gửi lệnh SMTP cơ bản tới server qua Socket.

2. **🖥️ SMTP Server**  
   - Chạy trên Java, lắng nghe port (mặc định 2525).  
   - Xử lý các lệnh `HELO`, `MAIL FROM`, `RCPT TO`, `DATA`, `QUIT`.  
   - Lưu email nhận được vào file hệ thống.

Luồng hoạt động:

- Người dùng mở GUI, kết nối server.  
- GUI gửi các lệnh SMTP → server nhận, phản hồi và ghi nội dung email vào file.  
- Toàn bộ truyền thông dựa trên giao thức TCP Socket.

## ✨ Tính năng chính

### 👨‍💻 Phía Client (GUI)

- Giao diện hiện đại với Nimbus Look&Feel, bo góc nút.  
- Nhập Host, Port, Người gửi, Người nhận, Tiêu đề, Nội dung.  
- Nút “Kết nối” để mở socket tới server.  
- Nút “Gửi Email” để gửi lệnh SMTP và nội dung email.  

### 🖥️ Phía Server

- Đa luồng: mỗi client là một `Thread` riêng.  
- Lắng nghe port, xử lý lệnh SMTP cơ bản.  
- Lưu nội dung email vào thư mục `emails/` theo timestamp.  

### 📂 Lưu trữ

- Mỗi email được ghi ra file `email_yyyyMMdd_HHmmss.txt`.  
- Nội dung gồm phần header (người gửi, người nhận, tiêu đề) và body.

## 🖼️ Giao diện
Một số ảnh minh họa giao diện Client GUI và Server Console:

<p align="center">
<img width="1220" height="517" alt="image" src="https://github.com/user-attachments/assets/8dcd1ea8-59f9-47c5-888d-b7b70302babe" />

  <br>
  <em>Hình 1. Giao diện ứng dụng Client (nhập Host, Port, Người gửi, Người nhận, Tiêu đề, Nội dung…)</em>
</p>

<p align="center">
  <img src="images/server_console.png" alt="Giao diện Server Console" width="600"/>
  <br>
  <em>Hình 2. Giao diện console Server (hiển thị lệnh SMTP nhận được và thông báo lưu email)</em>
</p>

<p align="center">
  <img src="images/email_file.png" alt="File email lưu trên server" width="600"/>
  <br>
  <em>Hình 3. File email đã lưu trên máy chủ</em>
</p>

## 🔧 Công cụ

<div align="center">

[![Java](https://img.shields.io/badge/Java%2017-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)  
[![Swing](https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logoColor=white)]()  
[![Socket](https://img.shields.io/badge/TCP%20Socket-000000?style=for-the-badge)]()

</div>

## 📥 Cài đặt

### 🛠️ Yêu cầu tiên quyết

- **Java JDK 17+**  
- IDE hỗ trợ (IntelliJ, NetBeans, Eclipse)  

### ⚙️ Thiết lập dự án

1. **Clone kho lưu trữ**
   ```bash
   git clone https://github.com/yourusername/smtp-socket-demo.git
   cd smtp-socket-demo


