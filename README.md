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
- GUI gửi các lệnh SMTP tới server qua socket.  
- Server nhận lệnh, kiểm tra receiver đang lắng nghe, gửi email tới receiver.
- Email được lưu vào file chỉ khi receiver thực sự nhận.
- GUI nhận phản hồi trạng thái gửi thành công hoặc thất bại.

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
    <img width="1920" height="1017" alt="Screenshot (289)" src="https://github.com/user-attachments/assets/d61ca3e1-0921-4e68-8f41-21e70cda7027" />
    <em>Giao diện người gửi và người nhận</em><br/>
</p>



<p align="center">
    <img width="1920" height="1009" alt="Screenshot (290)" src="https://github.com/user-attachments/assets/900731a2-7833-4ac3-af6d-55cc1e570fe1" />
    <em>Giao diện console Server (hiển thị log và lưu email)</em><br/>
</p>



<p align="center">
    <img width="1920" height="1009" alt="Screenshot (291)" src="https://github.com/user-attachments/assets/163c32d5-6a98-4da6-a735-0e7662ea9154" />
    <em>Hiển thị email và lưu trữ email trong file txt /emails</em><br/>
</p>

---

## 🛠️ 4. Các bước cài đặt
1. **Clone source code**  
   ```bash
   git clone https://github.com/vuhaiduc/LTM-1604-D13-Gui-email-mo-phong-SMTP-qua-Socket.git
   cd LTM-1604-D13-Gui-email-mo-phong-SMTP-qua-Socket

2. **Mở project trong IDE**
   - Dùng IntelliJ IDEA, Eclipse hoặc NetBeans.
   - Import project Java bình thường.
3. **Cấu hình JDK**
   - Chọn JDK 17 hoặc cao hơn.
4. **Chạy Server**
   - Mở file SMTPServer.java trong IDE.
   - Run as Java Application → Server lắng nghe port 2525.
5. **Chạy Client GUI**
   - Mở file SMTPClientGUI.java trong IDE.
   - Run as Java Application → mở giao diện người dùng.
6. **Kết nối và gửi email**
   - Nhập Host (IP của server thường là localhost) và Port (2525).
   - Nhập Người gửi, Người nhận, Tiêu đề, Nội dung.
   - Bấm Kết nối để mở socket, sau đó bấm Gửi Email.

  Lưu ý:
   - Nếu receiver chưa lắng nghe, sender sẽ nhận thông báo lỗi ``` "Không có receiver đang lắng nghe!" ```
   - Receiver phải nhấn Bắt đầu lắng nghe thì mới nhận được email.
7. **Kiểm tra file email**
   - Vào thư mục emails/ của server.
   - Email được lưu chỉ khi receiver nhận
   - Email sẽ được lưu dưới dạng ``` bash email_yyyyMMdd_HHmmss.txt ```.
  
---

## 💬 5. Liên hệ
- 📧 Email: Ducbeohd1000@gmail.com
- 📞 Hotline: 0963453615

---

<div align="center">

Thực hiện bởi Vũ Hải Đức - CNTT 16-04, trường Đại học Đại Nam

Website • GitHub • Contact Me

</div>


























