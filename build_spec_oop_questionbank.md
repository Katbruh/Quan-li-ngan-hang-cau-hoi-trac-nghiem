# BUILD SPEC — Hệ Thống Quản Lý Ngân Hàng Câu Hỏi Trắc Nghiệm (`OOP-QuestionBank`)

Tài liệu đặc tả kỹ thuật khép kín để triển khai toàn bộ dự án `OOP-QuestionBank` bằng Java thuần theo kiến trúc hướng đối tượng 4 package.

## 1. Ràng buộc kỹ thuật bắt buộc (Hard Constraints)

* **Ngôn ngữ & Môi trường:** Java 8+, không dùng thư viện ngoài.
* **Cấm tuyệt đối:**
  * Không dùng `List`, `ArrayList`, `LinkedList`, `HashMap`, `Set`, `Stream`, `Optional`.
  * Không dùng `MangDong<E>`, Generics (`<T>`, `<? extends ...>`), `Iterable`/`Iterator`.
  * Không dùng `Thread`, `BlockingQueue`, `Runnable`, đa luồng.
  * Không truyền `Scanner` vào tham số của bất kỳ hàm nào.
  * Không viết inner class hoặc anonymous class — **mỗi file `.java` chứa duy nhất 1 class hoặc 1 interface**.
  * Không dùng Javadoc (`/** ... */`, `@param`, `@return`).
* **Quy ước đặt tên biến:**
  * Dùng `ndA`, `ndB`, `ndC`, `ndD` cho 4 lựa chọn A/B/C/D; dùng mã viết tắt gọn `maCH`, `maDe`, `maKQ`, `maSV`, `maGV`, `maQTV`.
  * Dùng `traLoi` cho câu trả lời sinh viên và `dapAnDung` cho đáp án chuẩn.
  * Trong `IChiTietDanhSach`: tên hàm là `xoaTheoMa(String maCha)` và `demTheoMa(String maCha)` kèm 1 dòng comment giải thích `maCha`.
* **Tính đồng nhất giữa các lớp trong cùng package:**
  * Toàn bộ 12 lớp `mohinh/` dùng chung 1 kiểu setter `public boolean set...(val)` và chung 6 khối thứ tự.
  * Toàn bộ 6 lớp con (`CauHoi...`, `NguoiDung...`) cùng override `xuat()` gọi `super.xuat()` rồi `Arrays.copyOf` nối thêm cột riêng.
  * Toàn bộ 6 lớp `danhsach/` dùng chung 1 khung xương sống (`saoChep`, `getSoLuong`, `xuatDanhSachRaFile`, `nhapDanhSachTuFile`, `timViTri`, `them`, `kiemTraMa`, `timKiemMa`, `docPhanTu`, `nhapTimKiem`, `xuatDanhSach`).
* **Lưu trữ:** Chỉ dùng file nhị phân `.dat` trong thư mục `data/` qua `DataOutputStream` / `DataInputStream`. Tự gọi `new File("data").mkdirs();` trước khi ghi file; nếu file chưa tồn tại khi đọc thì giữ danh sách rỗng, không crash.
* **Điểm khởi chạy:** `src/Main.java` ở default package, thân hàm `main` chỉ gọi `Menu.run();`.
* **Quy mô:** 29 file Java trong đúng **4 package (`rangbuoc`, `mohinh`, `danhsach`, `tienich`) + `Main.java`**. Giữ hầu hết file dưới 200 dòng.

## 2. Cây thư mục dự án

```text
OOP-QuestionBank/
├── .gitignore
├── run.bat
├── data/
│   ├── DS_CauHoi.dat
│   ├── DS_NguoiDung.dat
│   ├── DS_DeThi.dat
│   ├── DS_ChiTietDeThi.dat
│   ├── DS_KetQua.dat
│   └── DS_ChiTietKetQua.dat
└── src/
    ├── Main.java
    ├── rangbuoc/
    │   ├── ITruyXuatMoHinh.java
    │   ├── ITruyVanDanhSach.java
    │   ├── IDanhSach.java
    │   └── IChiTietDanhSach.java
    ├── mohinh/
    │   ├── CauHoi.java
    │   ├── CauHoiMotDapAn.java
    │   ├── CauHoiNhieuDapAn.java
    │   ├── CauHoiDungSai.java
    │   ├── NguoiDung.java
    │   ├── SinhVien.java
    │   ├── GiangVien.java
    │   ├── QuanTriVien.java
    │   ├── DeThi.java
    │   ├── ChiTietDeThi.java
    │   ├── KetQuaThi.java
    │   └── ChiTietKetQua.java
    ├── danhsach/
    │   ├── DanhSachCauHoi.java
    │   ├── DanhSachNguoiDung.java
    │   ├── DanhSachDeThi.java
    │   ├── DanhSachChiTietDeThi.java
    │   ├── DanhSachKetQua.java
    │   └── DanhSachChiTietKetQua.java
    └── tienich/
        ├── LuuTru.java
        ├── NhapChuan.java
        ├── FormatBang.java
        ├── QuanLyDanhSach.java
        ├── XuLyThi.java
        └── Menu.java
```

### `.gitignore` chuẩn
```gitignore
bin/
out/
*.class
.idea/
*.iml
.vscode/
.DS_Store
```
*(Không ignore `src/Main.java` và thư mục `data/`.)*

## 3. Đặc tả tầng `rangbuoc/` (4 Interface)

```java
// ITruyXuatMoHinh.java
package rangbuoc;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface ITruyXuatMoHinh {
    ITruyXuatMoHinh nhap();
    String[] xuat();
    void ghiFile(DataOutputStream dos) throws IOException;
    void docFile(DataInputStream dis) throws IOException;
}
```

```java
// ITruyVanDanhSach.java
package rangbuoc;
import java.io.IOException;

public interface ITruyVanDanhSach {
    ITruyVanDanhSach saoChep();
    int getSoLuong();
    void nhapTimKiem();
    void xuatDanhSach();
    void xuatDanhSachRaFile() throws IOException;
    void nhapDanhSachTuFile() throws IOException;
}
```

```java
// IDanhSach.java
package rangbuoc;

public interface IDanhSach extends ITruyVanDanhSach {
    void nhapThem();
    void nhapSua();
    void nhapXoa();
}
```

```java
// IChiTietDanhSach.java
package rangbuoc;

public interface IChiTietDanhSach extends ITruyVanDanhSach {
    // maCha co the la maDe (chi tiet de thi) hoac maKQ (chi tiet ket qua)
    void xoaTheoMa(String maCha);
    int demTheoMa(String maCha);
}
```

## 4. Đặc tả tầng `mohinh/` (12 Class)

**Quy tắc chung bắt buộc cho toàn bộ 12 lớp trong `mohinh/`:**
* 100% `implements ITruyXuatMoHinh`.
* Đầy đủ 3 constructor: không tham số, đầy đủ tham số (gọi qua `if (!set...(val)) ...`), sao chép `ClassName(ClassName x)`, kèm `saoChep()`.
* Đầy đủ `get...()` (viết 1 dòng/hàm) và `public boolean set...(val)` (kiểm tra rỗng/âm, hợp lệ mới gán và trả `true`).
* Lớp con bắt buộc gọi `super(...)`, `super.nhap()`, `super.xuat()`, `super.ghiFile(dos)`, `super.docFile(dis)`.
* **Nguyên tắc đối xứng I/O nhị phân:** `ghiFile(dos)` ghi bao nhiêu trường theo thứ tự nào thì `docFile(dis)` trong chính lớp đó đọc y hệt bấy nhiêu trường theo đúng thứ tự đó (lớp `DanhSach` chỉ ghi/đọc thêm `int loai` ở đầu mỗi phần tử đa hình).
* Lưu `LocalDateTime` trong `.dat` bằng `dos.writeUTF(dt.format(DINH_DANG))` và đọc bằng `LocalDateTime.parse(dis.readUTF(), DINH_DANG)` với chuẩn `"dd/MM/yyyy HH:mm"`.

### Nhánh kế thừa 1 — `CauHoi` (Abstract)
* **`CauHoi` (`abstract`, `implements ITruyXuatMoHinh`):**
  * Thuộc tính (`private`/`protected`): `String maCH`, `String maMonHoc`, `String noiDung`, `String ndA`, `String ndB`, `String ndC`, `String ndD`, `String maGV`.
  * Hàm trừu tượng:
    * `public abstract CauHoi saoChep();`
    * `public abstract int getLoaiCauHoi();` (`1` = Một đáp án, `2` = Nhiều đáp án, `3` = Đúng/Sai)
    * `public abstract String getDapAnChuan();` (trả chuỗi chuẩn hóa: `"C"`, `"ABC"`, `"DDSD"`)
    * `public abstract double chamDiem(String traLoi, double diemToiDa);`
  * Hàm hiển thị:
    * `xuat()` trả **4 cột chung**: `{maCH, maMonHoc, catChuoi(noiDung, 35), maGV}`. Hàm cắt chuỗi 35 ký tự + `"..."` chỉ dùng khi in bảng danh sách tổng quan (`DanhSachCauHoi.xuatDanhSach()`) qua `FormatBang` để không tràn khung console.
    * `public void xuatChiTiet()`: in dọc đầy đủ 100% nội dung câu hỏi (không cắt chuỗi) cùng 4 lựa chọn `ndA..ndD` — dùng khi xem chi tiết 1 câu sau tìm kiếm và lúc sinh viên làm bài thi.
* **`CauHoiMotDapAn extends CauHoi`:**
  * Thuộc tính riêng: `char dapAnDung` (`'A'`, `'B'`, `'C'` hoặc `'D'`).
  * `xuat()`: gọi `super.xuat()`, dùng `Arrays.copyOf` nối thêm 2 cột `{"Mot dap an", getDapAnChuan()}` (tổng 6 cột).
  * `chamDiem(traLoi, diemToiDa)`: chuẩn hóa `trim().toUpperCase()`, khớp `String.valueOf(dapAnDung)` trả `diemToiDa`, sai/bỏ qua trả `0`.
  * `ghiFile`/`docFile`: gọi `super`, sau đó `dos.writeChar(dapAnDung)` / `dis.readChar()`.
* **`CauHoiNhieuDapAn extends CauHoi`:**
  * Thuộc tính riêng: `String dapAnDung` (chuỗi viết hoa, không trùng, đã sắp xếp A→D, vd `"ABC"`).
  * `xuat()`: gọi `super.xuat()`, nối thêm `{"Nhieu dap an", getDapAnChuan()}` (tổng 6 cột).
  * `chamDiem(traLoi, diemToiDa)`: đếm số ký tự `A..D` không trùng trong `traLoi` khớp với `dapAnDung` (`soDung`), trả `((double) soDung / dapAnDung.length()) * diemToiDa` (chọn thêm đáp án sai không trừ điểm).
  * `ghiFile`/`docFile`: gọi `super`, sau đó `dos.writeUTF(dapAnDung)` / `dis.readUTF()`.
* **`CauHoiDungSai extends CauHoi`:**
  * Thuộc tính riêng: `boolean[] dapAnDung` (mảng 4 phần tử cho 4 phát biểu A, B, C, D; `true` = Đúng (`D`), `false` = Sai (`S`)).
  * `xuat()`: gọi `super.xuat()`, nối thêm `{"Dung/Sai", getDapAnChuan()}` (tổng 6 cột).
  * `getDapAnChuan()`: trả chuỗi 4 ký tự `'D'`/`'S'` (vd `"DDSD"`).
  * `chamDiem(traLoi, diemToiDa)`: so từng vị trí `0..3` giữa `traLoi` (`'D'`/`'S'`/`'-'`) và `dapAnDung`; đếm số ý khớp (`soKhop`):
    * `1` ý → `0.1 * diemToiDa`; `2` ý → `0.25 * diemToiDa`; `3` ý → `0.5 * diemToiDa`; `4` ý → `1.0 * diemToiDa`; `0` ý → `0`.
  * `ghiFile`/`docFile`: gọi `super`, sau đó vòng `for (int i = 0; i < 4; i++)` ghi/đọc `boolean`.

### Nhánh kế thừa 2 — `NguoiDung` (Abstract)
* **`NguoiDung` (`abstract`, `implements ITruyXuatMoHinh`):**
  * Thuộc tính: `String tenDangNhap`, `String matKhau`, `String hoTen`.
  * Hàm trừu tượng: `public abstract NguoiDung saoChep();`, `public abstract String getVaiTro();`, `public abstract String getMa();`.
  * `xuat()` trả **4 cột chung**: `{getMa(), hoTen, tenDangNhap, getVaiTro()}`.
* **`SinhVien extends NguoiDung`:**
  * Thuộc tính riêng: `String maSV`, `String lop`.
  * `xuat()`: gọi `super.xuat()`, dùng `Arrays.copyOf` nối thêm `lop` (tổng 5 cột).
  * Quy ước tạo mới: mã `maSV` tự sinh (`SV001`, `SV002`...), `tenDangNhap = maSV`, `matKhau = "123456"`. `getVaiTro()` trả `"Sinh vien"`. `getMa()` trả `maSV`.
* **`GiangVien extends NguoiDung`:**
  * Thuộc tính riêng: `String maGV`, `String maMonHoc`.
  * `xuat()`: gọi `super.xuat()`, nối thêm `maMonHoc` (tổng 5 cột).
  * Quy ước tạo mới: mã `maGV` tự sinh (`GV001`, `GV002`...), `tenDangNhap = maGV`, `matKhau = "123456"`. `getVaiTro()` trả `"Giang vien"`. `getMa()` trả `maGV`.
* **`QuanTriVien extends NguoiDung`:**
  * Thuộc tính riêng: `String maQTV`.
  * `xuat()`: gọi `super.xuat()`, nối thêm `"Toan quyen"` (tổng 5 cột).
  * Tài khoản hệ thống cố định: `("QTV01", "admin", "123456", "Quan Tri Vien")` (xác thực trực tiếp lúc đăng nhập, không lưu trong file `.dat`).

### 4 Thực thể nghiệp vụ (liên kết bằng khóa ngoại `String`)
* **`DeThi`:** `String maDe`, `String tenDe`, `String maMonHoc`, `int loaiCauHoi` (`1/2/3`), `int thoiGian` (phút), `LocalDateTime ngayThi`, `LocalDateTime ngayTao` (tự gán `LocalDateTime.now()`), `String maKyThi`, `String loaiDoiTuong` (`"LOP"` hoặc `"SINH_VIEN"`), `String doiTuong`, `String maGV`.
* **`ChiTietDeThi`:** `String maDe`, `String maCH`, `double diemToiDa`.
* **`KetQuaThi`:** `String maKQ`, `String maSV`, `String maDe`, `LocalDateTime ngayThi`, `int soCauDung` *(chỉ cộng khi `Math.abs(diemDatDuoc - diemToiDa) < 1e-6`)*, `int tongSoCau`, `double diem`, `long thoiGianGiay`.
* **`ChiTietKetQua`:** `String maKQ`, `String maCH`, `String traLoi`, `String dapAnDung`, `double diemDatDuoc`.

## 5. Đặc tả tầng `danhsach/` (6 Class)

**Khuôn lặp lại đồng nhất cho cả 6 lớp `danhsach/`:**
* Mảng thuần `Model[] ds = new Model[0]; int soLuong = 0;`, deep copy qua `.saoChep()`.
* `nhapTimKiem()` cùng 1 khuôn: nhập mã → tìm vị trí → nếu `-1` báo không thấy → tạo `dsTam`, `dsTam.them(...)`, `dsTam.xuatDanhSach()` *(riêng `DanhSachCauHoi` gọi thêm `xuatChiTiet()` để xem đủ nội dung và 4 lựa chọn)*.
* `nhapSua()` và `nhapXoa()` cùng 1 khuôn `do { ... } while (NhapChuan.docYesNo("Co tiep tuc khong", false));`.
* `xuatDanhSachRaFile()` / `nhapDanhSachTuFile()` cùng 1 khuôn 7 dòng (đóng stream rõ ràng).

| Lớp | Interface | Mảng & Đa hình | Quy ước mã (tự sinh) | Ràng buộc khóa ngoại & Phân quyền |
| :--- | :--- | :--- | :--- | :--- |
| `DanhSachCauHoi` | `IDanhSach` | `CauHoi[] ds` (ghi `int loai` `1/2/3` trước mỗi phần tử). Có thêm `public void nhap() { nhapThem(); }`. | `M001`, `N001`, `DS001` | Qua `QuanLyDanhSach.nguoiDungHienTai`: nếu là `GiangVien` tự gán `maGV`, chỉ sửa/xóa câu của mình. **Chặn xóa** nếu `maCH` có trong `DS_CHITIETDETHI`. |
| `DanhSachNguoiDung` | `IDanhSach` | `NguoiDung[] ds` (chứa `SinhVien`, `GiangVien`; ghi `int loai` `1/2` trước mỗi phần tử). | `SV001`, `GV001`... | Chỉ QTV thao tác. **Chặn xóa** `GiangVien` nếu `maGV` có trong `DS_CAUHOI`/`DS_DETHI`; **chặn xóa** `SinhVien` nếu `maSV` có trong `DS_KETQUA`. |
| `DanhSachDeThi` | `IDanhSach` | `DeThi[] ds` | `DT001`, `DT002`... | Nếu là GV: tự gán `maGV`, chỉ sửa/xóa đề của mình. **Chặn sửa/xóa** nếu `maDe` đã có trong `DS_KETQUA`. Nếu chưa có ai thi và xóa đề → **tự gọi `DS_CHITIETDETHI.xoaTheoMa(maDe)`**. |
| `DanhSachKetQua` | `ITruyVanDanhSach` | `KetQuaThi[] ds` | `KQ001`, `KQ002`... | Chỉ xem/tìm kiếm; thêm nội bộ qua `themVaoDanhSach(...)` từ `XuLyThi`; hỗ trợ tìm theo `maDe`, `maSV`, hoặc theo `diem` (2 chữ số thập phân). |
| `DanhSachChiTietDeThi` | `IChiTietDanhSach` | `ChiTietDeThi[] ds` | Khóa ghép `(maDe, maCH)` | Có `xoaTheoMa(maCha)`, `demTheoMa(maCha)`, `kiemTraTonTaiCauHoi(maCH)`, `layTheoMaDe(maDe)`, `timKiemNhieuMa(maDe, maCH)`. |
| `DanhSachChiTietKetQua` | `IChiTietDanhSach` | `ChiTietKetQua[] ds` | Khóa ghép `(maKQ, maCH)` | Có `xoaTheoMa(maCha)`, `demTheoMa(maCha)`, `layTheoMaKQ(maKQ)`, `timKiemNhieuMa(maKQ, maCH)`. |

## 6. Đặc tả tầng `tienich/` (6 Class `static`) & `Main.java`

Mọi lớp trong `tienich/` khai báo `private ClassName() {}` để chặn khởi tạo đối tượng.

1. **`LuuTru.java`:** Khai báo 6 hằng `public static final String`: `FILEDSCH`, `FILEDSND`, `FILEDSDT`, `FILEDSCTDT`, `FILEDSKQ`, `FILEDSCTKQ` (trỏ tới `data/DS_*.dat`).
2. **`NhapChuan.java` (`final`):** 1 `private static final Scanner SCANNER = new Scanner(System.in, "UTF-8");`. Các hàm: `docChuoi`, `docChuoiKhongRong`, `docSoNguyen`, `docSoNguyenKhongAm`, `docSoNguyenTrongKhoang(prompt, min, max)`, `docSoThuc`, `docSoThucKhongAm`, `docNgayGio` (`dd/MM/yyyy HH:mm`), `docNam` (`1000–9999`), `docYesNo(prompt, defaultValue)`, `docDungSai(prompt)` (nhận `D`/`S` hoặc Enter rỗng trả `'-'`), `nhapLuaChonMenu()`.
3. **`FormatBang.java`:** Tiện ích vẽ bảng console căn cột tự động bằng ký tự khung Unicode (`┌`, `─`, `┐`, `│`, `├`, `┼`, `┤`, `└`, `┴`, `┘`), cung cấp 2 hàm chính: `inBangDuLieu(String tieuDe, String[][] bangDuLieu)` và `inBangThongKe(String[] tieuDeDong, String[] tieuDeCot, double[][] bangSoLieu)` (tự động cộng tổng từng dòng ở cột cuối).
4. **`QuanLyDanhSach.java` (`final`):**
   * `public static NguoiDung nguoiDungHienTai = null;`
   * 6 danh sách `static`: `DS_CAUHOI`, `DS_NGUOIDUNG`, `DS_DETHI`, `DS_CHITIETDETHI`, `DS_KETQUA`, `DS_CHITIETKETQUA`.
   * 3 mảng đa hình:
     * `ITruyVanDanhSach[] DANHSACHTRUYCAPFILE` (cả 6 danh sách → nạp/ghi file 1 vòng `for`).
     * `IDanhSach[] DANHSACH` (3 danh sách CRUD chính: `DS_NGUOIDUNG`, `DS_CAUHOI`, `DS_DETHI`).
     * `IChiTietDanhSach[] DANHSACHCHITIET` (2 danh sách chi tiết: `DS_CHITIETDETHI`, `DS_CHITIETKETQUA`).
   * **3 hàm Thống kê viết theo đúng 1 khuôn 3 bước** (tạo mảng `double[][]` → vòng `for` cộng dồn → gọi `FormatBang.inBangThongKe(...)`):
     1. `thongKeCauHoiTheoMonVaLoai()`: dòng = các `maMonHoc`, cột = `Mot dap an`, `Nhieu dap an`, `Dung/Sai`, `Tong cong`.
     2. `thongKeKetQuaTheoDeThi()`: bảng tổng hợp từng `DeThi` gồm: Số SV đã thi, Điểm TB, Cao nhất, Thấp nhất.
     3. `thongKeLuotThiTheoQuy()`: nhập năm `yyyy`, thống kê số lượt thi theo `Quy 1`, `Quy 2`, `Quy 3`, `Quy 4`, `Tong cong`.
5. **`XuLyThi.java` (`final`):**
   * `taoDeThi()`: chọn loại đề (`1/2/3`), nhập thông tin đề (`tenDe`, `maMonHoc`, `thoiGian`, `ngayThi`, `maKyThi`, `loaiDoiTuong`, `doiTuong`), nhập số câu (`1..50`, kiểm tra đủ câu trong `DS_CAUHOI` theo `maMonHoc` + `loaiCauHoi`; GV chỉ lọc câu của mình). Chọn ngẫu nhiên không trùng bằng mảng đánh dấu `boolean[] daChon = new boolean[m]` (lặp `while (daLay < soCau)` bốc `idx = rand.nextInt(m)`, nếu `!daChon[idx]` thì gán `daChon[idx] = true` và lấy câu đó). Chia đều `diemToiDa = 10.0 / soCau`, lưu `DeThi` và các `ChiTietDeThi`.
   * `lamBaiThi(SinhVien sv)`: liệt kê đề được giao cho `sv` (`LOP` khớp `sv.getLop()` hoặc `SINH_VIEN` chứa `sv.getMaSV()`) kèm `[DA THI]` / `[CHUA THI]`; chặn thi lại. Lưu `long batDau = System.currentTimeMillis();`. Duyệt từng câu trong `ChiTietDeThi`, gọi `ch.xuatChiTiet()`, nhận `traLoi` (Một đáp án: `A–D` hoặc rỗng; Nhiều đáp án: chuỗi `A–D`; Đúng/Sai: hỏi lần lượt từng ý A→B→C→D bằng `NhapChuan.docDungSai`), gọi đa hình `ch.chamDiem(traLoi, ct.getDiemToiDa())`. Sau mỗi câu kiểm tra `(System.currentTimeMillis() - batDau) >= deThi.getThoiGian() * 60000L` → hết giờ tự chốt nộp bài. Lưu `KetQuaThi` + `ChiTietKetQua` và in tổng kết.
   * `xemChiTietBaiLam(String maKQ)`: in từng câu của lượt thi (`maCH`, `traLoi`, `dapAnDung`, `diemDatDuoc`).
   * `xemKetQuaTheoBoLoc(String maGV, boolean laQTV)`: xem theo đề, theo MSSV, tất cả kết quả (QTV: toàn hệ thống; GV: đề của mình), hoặc tìm theo điểm (2 chữ số thập phân).
6. **`Menu.java`:** Giữ gọn trong 1 file (~170–190 dòng), chỉ in menu & gọi hàm, không chứa logic xử lý:
   * `run()`: gọi `QuanLyDanhSach.taiDuLieuDanhSach()`, vòng lặp đăng nhập (in gợi ý `admin/123456`, `GV48217/123456`, `SV48217/123456`), khi thoát gọi `QuanLyDanhSach.ghiDuLieuDanhSach()`.
   * `dangNhap()`: nếu `user.equals("admin") && pass.equals("123456")` trả `new QuanTriVien("QTV01", "admin", "123456", "Quan Tri Vien")`; ngược lại tìm trong `DS_NGUOIDUNG`. Rẽ nhánh theo `instanceof`.
   * `menuQuanTriVien()`: `1. Quan ly Nguoi dung`, `2. Quan ly Cau hoi`, `3. Tao de thi`, `4. Quan ly De thi`, `5. Xem ket qua & chi tiet`, `6. Thong ke`, `7. Xem chi tiet de / ket qua` *(gọi `menuChiTiet(DS_CHITIETDETHI)` / `menuChiTiet(DS_CHITIETKETQUA)`)*, `0. Dang xuat`.
   * `menuGiangVien(GiangVien gv)`: `1. Quan ly Cau hoi (cua minh)`, `2. Tao de thi`, `3. Quan ly De thi (cua minh)`, `4. Xem ket qua de thi`, `5. Thong ke`, `0. Dang xuat`.
   * `menuSinhVien(SinhVien sv)`: `1. Thi de duoc giao`, `2. Xem lich su ket qua`, `3. Xem chi tiet bai da lam`, `0. Dang xuat`.
   * `menuDanhSach(IDanhSach ds)`: `1. Xem`, `2. Them`, `3. Sua`, `4. Xoa`, `5. Tim kiem`, `0. Quay lai`.
   * `menuChiTiet(IChiTietDanhSach ds)`: `1. Xem`, `2. Tim kiem`, `0. Quay lai`.
   * `menuThongKe()`: `1. Cau hoi theo Mon & Loai`, `2. Ket qua theo De thi`, `3. Luot thi theo Quy/Nam`, `0. Quay lai`.
7. **`Main.java`:**
   ```java
   import tienich.Menu;

   public class Main {
       public static void main(String[] args) {
           Menu.run();
       }
   }
   ```

## 7. Quy chuẩn trình bày code & Comment

* **Thứ tự 6 khối trong `mohinh/`:** Thuộc tính → 3 Constructor + `saoChep()` → `xuat()` / `nhap()` / `xuatChiTiet()` → `ghiFile()` / `docFile()` → Getter (1 dòng/hàm) → Setter (`boolean`) + hàm nghiệp vụ (`chamDiem()`).
* **Thứ tự 5 khu vực trong `danhsach/`:** Thuộc tính + Constructor + `saoChep()` + `getSoLuong()` → File I/O → Phương thức nội bộ `private` (`timViTri`, `them`, `sua`, `xoa`, `khoiTao...`) → Giao tiếp bên ngoài `public` (`kiemTraMa`, `timKiemMa`, `docPhanTu`, `taoMaMoi`) → Giao diện console `public` (`nhapTimKiem`, `nhapThem`, `nhapSua`, `nhapXoa`, `xuatDanhSach`).
* **Quy tắc Comment (tối giản, không bừa bãi):**
  * Chỉ dùng `// ...` 1 dòng, tiếng Việt không dấu, chữ thường. Không Javadoc, không thuyết minh lý thuyết OOP.
  * `rangbuoc/`: 0 comment (riêng `IChiTietDanhSach` có 1 dòng giải thích `maCha`).
  * `mohinh/`: **0 comment** (trừ 1 dòng công thức chấm điểm trong `CauHoiDungSai`).
  * `danhsach/`: Chỉ dùng 3 thanh `// ======================================================` + 1 thanh `// ------------------------------------` ở 2 file đa hình dài (`DanhSachCauHoi`, `DanhSachNguoiDung`); 4 file `DanhSach` còn lại chỉ đặt 0–3 dòng nhãn ngắn.
  * `tienich/`: `LuuTru` 0 dòng; `NhapChuan`, `Menu`, `QuanLyDanhSach`, `XuLyThi` mỗi file 2–3 dòng; `FormatBang` ~10 dòng giải thích thuật toán vẽ bảng.

## 8. Đặc tả ánh xạ dữ liệu từ 9 file `.txt` cũ → 6 file `.dat` mới (cho script convert riêng)

Bộ dữ liệu cũ gồm 514 dòng trong 9 file `.txt` cần được chuyển đổi theo quy tắc sau để khớp 100% với 6 file `.dat` mới:

| File `.txt` cũ | Biến đổi bắt buộc | File `.dat` đích |
| :--- | :--- | :--- |
| `cauhoimotdapan.txt` (160)<br>`cauhoinhieudapan.txt` (90)<br>`cauhoidungsai.txt` (110) | Đổi mã `Qxxx` → `Mxxx` (loại 1), `Nxxx` (loại 2), `DSxxx` (loại 3); bỏ cột `doKho` cuối; gom các cột đáp án biến thiên (11–14 cột) của `cauhoinhieudapan.txt` thành 1 chuỗi `"ABC"`; đổi `"Đúng"`/`"Sai"` thành 4 `boolean`. | `data/DS_CauHoi.dat` |
| `DSSV.txt` (30)<br>`DSGV.txt` (11) | `trim()` khoảng trắng quanh dấu `\|`; gán `tenDangNhap = ma`, `matKhau = "123456"`; ghi `int loai = 1` (SV) hoặc `2` (GV) trước mỗi phần tử. | `data/DS_NguoiDung.dat` |
| `dethi.txt` (6) | Parse `LocalDateTime.parse(...)` cho `ngayThi`, `ngayTao`; suy ra `int loaiCauHoi` từ prefix mã đề (`DT` → 1, `NDA` → 2, `DS` → 3). | `data/DS_DeThi.dat` |
| `chitietcauhoibaithi.txt` (72) | Bỏ cột ID đầu và cột đáp án dư; ánh xạ đổi `maCH` (`Qxxx` → `Mxxx`/`Nxxx`/`DSxxx`) theo loại đề tương ứng; giữ `(maDe, maCH, diemToiDa)`. | `data/DS_ChiTietDeThi.dat` |
| `ketqua.txt` (7) | Tự cấp `maKQ` (`KQ001..KQ007`); tách `maDe` trong dấu `[DT002]`; parse `ngayThi` dạng `yyyy-MM-dd HH:mm:ss`. | `data/DS_KetQua.dat` |
| `chitietketquabaithi.txt` (28) | Nối `maKQ` theo bộ 3 `(maSV, maDe, ngayThi)` từ `ketqua.txt`; đổi `maCH` sang `M`/`N`/`DS`; tính lại `diemDatDuoc` bằng `ch.chamDiem(...)`. | `data/DS_ChiTietKetQua.dat` |

## 9. Checklist kiểm thử sau khi build

* Chạy `run.bat` phải biên dịch sạch 0 lỗi, 0 warning nghiêm trọng.
* Đăng nhập được ngay bằng `admin / 123456` kể cả khi thư mục `data/` đang trống hoàn toàn.
* Thêm/sửa/xóa/tìm kiếm thành công cả 3 loại `CauHoi`, 2 loại `NguoiDung`, `DeThi`; thoát ra mở lại dữ liệu `.dat` đọc lên khớp 100% (không `EOFException`).
* Kiểm tra chặn xóa: không xóa được `CauHoi` đang có trong đề, `GiangVien` có câu hỏi/đề, `SinhVien` có kết quả, và không sửa/xóa được `DeThi` đã có SV thi.
* Test GV đăng nhập: chỉ sửa/xóa được câu hỏi và đề do `maGV` mình tạo.
* Test SV làm đủ 3 loại đề (Một đáp án, Nhiều đáp án, Đúng/Sai), kiểm tra điểm chấm tự động, chặn thi lại đề đã thi, và kiểm tra cả 3 bảng trong `Menu Thong Ke` + mục xem chi tiết đề/kết quả của QTV.
