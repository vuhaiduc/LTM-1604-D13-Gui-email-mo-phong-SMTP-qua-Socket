<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   GỬI EMAIL MÔ PHỎNG SMTP QUA SOCKET
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

- **Client GUI (Java Swing)**: Cung cấp giao diện đồ hoạ cho người dùng (trang đăng ký, đăng nhập, trang soạn mail và hộp thư đến ).  
- **SMTP Server (Java)**: Chạy nền và lắng nghe các kết nối TCP từ nhiều client trên port 2525 và 2626, xử lý các lệnh SMTP chuẩn, quản lý danh sách người dùng và gửi mail tới người nhận tương ứng.

Luồng hoạt động:

- Người dùng mở ra giao diện đăng ký, đăng nhập.
- Tạo ra 2 tài khoản là người gửi và người nhận.
- Client người nhận sẽ nhận mail từ người gửi và email sẽ hiển thị trực tiếp trên giao diện hộp thư.
- Email được lưu vào file /emails/ kèm thời gian gửi.

---

## ⚙️ 2. Công nghệ sử dụng
Ứng dụng được phát triển bằng:
- **Ngôn ngữ lập trình**: <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"></a>
- **Giao diện người dùng**: <a href="https://docs.oracle.com/javase/tutorial/uiswing/"><img src="https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white" alt="Swing"></a>
- **Socket**: <img src="https://img.shields.io/badge/TCP%20Socket-000000?style=for-the-badge" alt="Socket">

- **Kiến trúc**:  
  - `Client`: giao diện người dùng (SMTPClientGUI).  
  - `Server`: lắng nghe socket, xử lý SMTP, lưu email ra file.  

---

## 🖼️ 3. Một số hình ảnh hệ thống 

<p align="center">
    <img width="1920" height="1009" alt="Screenshot (299)" src="https://github.com/user-attachments/assets/e5e644ee-94f6-411f-9389-36cf7f3c18ea" />
    <em>Giao diện người gửi và người nhận</em><br/>
</p>



<p align="center">
    <img width="1920" height="1009" alt="Screenshot (301)" src="https://github.com/user-attachments/assets/40fb1a3d-71e8-4383-913f-be278bb8ecfb" />
    <em>Giao diện console Server (hiển thị log và lưu email)</em><br/>
</p>



<p align="center">
    <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/48d9d9ec-7e85-480c-935f-e951babdddb9" />
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
   - Mở file smtpserver/ServerMain.java.
   - Run as Java Application → Server lắng nghe port 2525.
   - Console sẽ hiển thị log kết nối khi có client đăng nhập hoặc gửi email.
5. **Chạy Client GUI**
   - Mở file smtpclient/ClientLoginUI.java (giao diện người gửi).
   - Mở file smtpclient/ReceiverLoginUI.java (giao diện người nhận).
   - Run as Java Application → mở giao diện người dùng.
6. **Kết nối và gửi email**
   - Nhập email người nhận để gửi.
   - Nhập Người nhận, Tiêu đề, Nội dung, File kèm theo.
   - Người nhận với tài khoản đăng nhập khác sẽ nhận Mail ngay lập tức và xem trực tiếp trong giao diện.

7. **Kiểm tra file email**
   - Vào thư mục emails/ của server.
   - Email được lưu chỉ khi receiver nhận
   - Email sẽ được lưu dưới dạng ``` bash email_yyyyMMdd_HHmmss.txt ```.
  
---

## 💬 5. Liên hệ
📧 Email: Ducbeohd1000@gmail.com

---

<div align="center">

Thực hiện bởi Vũ Hải Đức - CNTT 16-04, trường Đại học Đại Nam

Website • GitHub • Contact Me

</div>
































