# Hệ thống Ngân hàng Câu hỏi & Thi Trắc nghiệm (OOP-QuestionBank)

Ứng dụng console viết bằng **Java thuần** (không dùng thư viện ngoài), mô phỏng hệ thống quản lý ngân hàng câu hỏi trắc nghiệm, tạo đề thi ngẫu nhiên, tổ chức thi tính giờ, tự động chấm điểm và thống kê kết quả. Dữ liệu được lưu trữ dưới dạng file nhị phân (`.dat`).

---

## 1. Yêu cầu & Cách chạy

- **Yêu cầu:** Java JDK 8 trở lên (`javac`, `java`).

### Chạy nhanh trên Windows
Chạy trực tiếp file [`run.bat`](run.bat) (tự biên dịch toàn bộ `src/*.java` với encoding UTF-8 vào thư mục `bin/` và khởi chạy `Main`).

### Chạy bằng dòng lệnh
- **Windows (PowerShell):**
  ```powershell
  mkdir bin -Force
  javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
  java -cp bin Main
  ```
- **Linux / macOS (Bash):**
  ```bash
  mkdir -p bin
  javac -encoding UTF-8 -d bin $(find src -name "*.java")
  java -cp bin Main
  ```

---

## 2. Tài khoản dùng thử

Khi khởi động, hệ thống tự nạp dữ liệu mẫu từ thư mục `data/`:

| Vai trò | Tên đăng nhập | Mật khẩu | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Quản trị viên** | `admin` | `123456` | Tài khoản mặc định hệ thống |
| **Giảng viên** | `GV48217` | `123456` | Tài khoản mẫu trong `DS_NguoiDung.dat` |
| **Sinh viên** | `SV48217` | `123456` | Tài khoản mẫu trong `DS_NguoiDung.dat` |

---

## 3. Chức năng chính theo phân quyền

### Quản trị viên (`QuanTriVien`)
- Quản lý Người dùng (Xem / Thêm / Sửa / Xóa / Tìm kiếm).
- Quản lý Ngân hàng Câu hỏi & Quản lý Đề thi.
- Tạo đề thi mới (chọn ngẫu nhiên câu hỏi theo môn học và loại câu hỏi, giao theo `LOP` hoặc danh sách `SINH_VIEN`).
- Xem kết quả thi, chi tiết đề thi và chi tiết bài làm.
- Thống kê: số lượng câu hỏi theo môn & loại; điểm trung bình/cao/thấp theo đề thi; lượt thi theo quý/năm.

### Giảng viên (`GiangVien`)
- Quản lý Câu hỏi của mình (tự động gán `maMonHoc` theo môn giảng viên phụ trách).
- Tạo đề thi từ ngân hàng câu hỏi của mình và quản lý Đề thi của mình.
- Xem kết quả các đề thi do mình tạo và xem bảng thống kê.

### Sinh viên (`SinhVien`)
- Xem danh sách đề được giao (theo lớp hoặc mã SV) và làm bài thi trực tiếp có giới hạn thời gian.
- Hệ thống tự động chấm điểm ngay khi nộp bài hoặc hết giờ.
- Xem lịch sử kết quả thi và chi tiết từng câu đã làm (đáp án đã chọn, đáp án chuẩn, điểm đạt được).

---

## 4. Ba dạng câu hỏi & Quy tắc chấm điểm

Điểm tối đa mỗi câu trong đề thi là `diemToiDa = 10.0 / soCau`:

1. **Một đáp án (`CauHoiMotDapAn`):** Chọn 1 ký tự `A–D`. Đúng được `diemToiDa`, sai được `0`.
2. **Nhiều đáp án (`CauHoiNhieuDapAn`):** Chọn tổ hợp `A–D` (VD: `ABC`). Nếu chọn thừa hoặc sai bất kỳ đáp án nào thì `0` điểm; chọn đúng một phần được `(soDung / tongDapAnDung) * diemToiDa`.
3. **Đúng/Sai 4 ý (`CauHoiDungSai`):** Trả lời từng ý `A, B, C, D` là Đúng (`D`) hoặc Sai (`S`). Tính điểm theo số ý khớp: `1 ý = 10%`, `2 ý = 25%`, `3 ý = 50%`, `4 ý = 100%` của `diemToiDa`.

---

## 5. Cấu trúc mã nguồn

```text
oop/
├── run.bat                  # Script biên dịch & chạy trên Windows
├── data/                    # 6 file dữ liệu nhị phân (DS_*.dat)
└── src/
    ├── Main.java            # Điểm vào chương trình
    ├── rangbuoc/            # 4 interface (ITruyXuatMoHinh, ITruyVanDanhSach, IDanhSach, IChiTietDanhSach)
    ├── mohinh/              # 12 lớp thực thể (CauHoi + 3 lớp con, NguoiDung + 3 lớp con, DeThi, ChiTietDeThi, KetQuaThi, ChiTietKetQua)
    ├── danhsach/            # 6 lớp quản lý mảng động, CRUD & đọc/ghi file nhị phân
    └── tienich/             # Menu, QuanLyDanhSach, XuLyThi, NhapChuan, FormatBang, LuuTru
```

---

## 6. Điểm nhấn thiết kế OOP

- **Đóng gói & Kiểm tra dữ liệu:** Các thuộc tính thực thể để `protected`/`private`, kiểm tra tính hợp lệ qua `setter` và `NhapChuan`.
- **Kế thừa & Trừu tượng:** Phân cấp `CauHoi` (`CauHoiMotDapAn`, `CauHoiNhieuDapAn`, `CauHoiDungSai`) và `NguoiDung` (`QuanTriVien`, `GiangVien`, `SinhVien`).
- **Đa hình:**
  - Giao diện `IDanhSach` / `IChiTietDanhSach` cho phép `Menu` thao tác thống nhất trên các danh sách khác nhau.
  - `DanhSachCauHoi` và `XuLyThi` lưu trữ, đọc/ghi file, sao chép (`saoChep()`) và chấm điểm (`chamDiem()`) thông qua tham chiếu cha `CauHoi` mà không cần rẽ nhánh tính điểm theo từng loại.
