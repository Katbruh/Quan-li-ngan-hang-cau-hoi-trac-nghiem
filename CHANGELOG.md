# Nhật ký chỉnh sửa dự án OOP-QuestionBank

Tài liệu này ghi lại toàn bộ các thay đổi, sửa lỗi và cải tiến kể từ mốc hoàn thiện nền tảng (Đợt 4).

---

## Mốc gốc (Baseline — Đợt 4)
- **Kiến trúc:** 29 file Java thuộc 4 package (`rangbuoc`, `mohinh`, `danhsach`, `tienich`) + `Main.java`, kèm `ConvertData.java` và `run.bat`.
- **Dữ liệu:** Đã tách dữ liệu thô vào `old_data/` (được ignore trong `.gitignore`) và sinh bộ file nhị phân trong `data/`.
- **`.gitignore`:** Bỏ qua `bin/`, `out/`, `out_audit/`, `*.class`, `.idea/`, `*.iml`, `.vscode/`, `.DS_Store`, `old_data/`, `.opencode/`, `opencode.json`.

---

## Danh sách vấn đề ghi nhận tại mốc gốc (cần xem xét/chỉnh sửa tiếp)

### 1. Lỗi hiển thị đỏ trên IntelliJ IDEA (`Package name '...' does not correspond to the file path 'src....'`)
- **Quan sát:** Trên IntelliJ IDEA (`DanhSachNguoiDung.java` và các file trong `src/`), IDE báo `Package name 'danhsach' does not correspond to the file path 'src.danhsach'` và `Cannot resolve symbol 'mohinh'/'tienich'`.
- **Nguyên nhân:** Cấu hình module (`oop.iml`) chưa đặt `src/` làm *Sources Root* (đang nhận thư mục gốc dự án làm root). Đây không phải lỗi cú pháp Java (`javac` vẫn biên dịch bình thường).
- **Hướng xử lý:** Trong IntelliJ IDEA, chuột phải thư mục `src/` → **Mark Directory as** → **Sources Root** (hoặc sửa `<sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />` trong `oop.iml`).

### 2. Cảnh báo code từ IDE (Static Analysis)
- `Result of 'File.mkdirs()' is ignored` trong các hàm `xuatDanhSachRaFile()`.
- `Empty 'catch' block` khi parse hậu tố mã (`NumberFormatException`) trong `DanhSachCauHoi`, `DanhSachNguoiDung`, `DanhSachDeThi`, `DanhSachKetQua`.
- Một số phương thức giao tiếp `public` (`kiemTraMa`, `docPhanTu`, v.v.) có thể có cảnh báo chưa được gọi ở một vài lớp danh sách.

---

## Lịch sử chỉnh sửa (từ đây về sau)

*(Ghi tiếp các mục mới bên dưới theo mẫu: Ngày — Hạng mục — File thay đổi — Nội dung chỉnh sửa — Kết quả kiểm tra.)*
