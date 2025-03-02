Hướng Dẫn Cài Đặt

* Yêu Cầu Môi Trường
  - IDE: IntelliJ IDEA  
  - Build Tool: Maven  
  - Server: Tomcat 10  
  - JDK: 21  
  - Database: MySQL
  
* Các Bước Cài Đặt:

  - Bước 1: Clone mã nguồn về máy
  - Bước 2: Import Database
      1. Mở MySQL Workbench  
      2. Chọn Administration → Data Import/Restor*.  
      3. Chọn file `dump.sql` trong thư mục `Database` của dự án.  
      4. Nhấn Start Import để hoàn tất.  
  - Bước 3: Cấu hình kết nối Database
      Mở file `persistence.xml` và cập nhật thông tin database.
  - Bước 4: Chạy chương trình
      1. Mở dự án bằng IntelliJ IDEA.  
      2. Chọn môi trường như đã yêu cầu.  
      3. Chạy chương trình.
