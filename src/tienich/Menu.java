package tienich;

import java.util.Arrays;
import mohinh.GiangVien;
import mohinh.KetQuaThi;
import mohinh.NguoiDung;
import mohinh.QuanTriVien;
import mohinh.SinhVien;
import rangbuoc.IChiTietDanhSach;
import rangbuoc.IDanhSach;

public final class Menu {
    private Menu() {
    }

    // khoi dong he thong, nap du lieu va chay vong lap dang nhap
    public static void run() {
        QuanLyDanhSach.taiDuLieuDanhSach();
        while (true) {
            System.out.println("\n=== HE THONG NGAN HANG CAU HOI TRAC NGHIEM ===");
            System.out.println("Goi y tai khoan: admin/123456 | GV48217/123456 | SV48217/123456");
            System.out.println("1. Dang nhap");
            System.out.println("0. Thoat va luu du lieu");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) {
                QuanLyDanhSach.ghiDuLieuDanhSach();
                System.out.println("Da luu du lieu. Tam biet!");
                break;
            }
            if (chon != 1) continue;
            NguoiDung nd = dangNhap();
            if (nd == null) { System.out.println("Sai ten dang nhap hoac mat khau!"); continue; }
            QuanLyDanhSach.nguoiDungHienTai = nd;
            if (nd instanceof QuanTriVien) menuQuanTriVien();
            else if (nd instanceof GiangVien) menuGiangVien((GiangVien) nd);
            else if (nd instanceof SinhVien) menuSinhVien((SinhVien) nd);
            QuanLyDanhSach.nguoiDungHienTai = null;
        }
    }

    private static NguoiDung dangNhap() {
        String user = NhapChuan.docChuoiKhongRong("Ten dang nhap");
        String pass = NhapChuan.docChuoiKhongRong("Mat khau");
        if (user.equals("admin") && pass.equals("123456")) {
            return new QuanTriVien("QTV01", "admin", "123456", "Quan Tri Vien");
        }
        return QuanLyDanhSach.DS_NGUOIDUNG.dangNhap(user, pass);
    }

    // menu danh rieng cho quan tri vien
    private static void menuQuanTriVien() {
        while (true) {
            System.out.println("\n=== MENU QUAN TRI VIEN ===");
            System.out.println("1. Quan ly Nguoi dung");
            System.out.println("2. Quan ly Cau hoi");
            System.out.println("3. Tao de thi");
            System.out.println("4. Quan ly De thi");
            System.out.println("5. Xem ket qua & chi tiet");
            System.out.println("6. Thong ke");
            System.out.println("7. Xem chi tiet de / ket qua");
            System.out.println("0. Dang xuat");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            switch (chon) {
                case 1: menuDanhSach(QuanLyDanhSach.DANHSACH[0]); break;
                case 2: menuDanhSach(QuanLyDanhSach.DANHSACH[1]); break;
                case 3: XuLyThi.taoDeThi(); break;
                case 4: menuDanhSach(QuanLyDanhSach.DANHSACH[2]); break;
                case 5:
                    XuLyThi.xemKetQuaTheoBoLoc("", true);
                    if (NhapChuan.docYesNo("Xem chi tiet mot bai lam", false)) {
                        XuLyThi.xemChiTietBaiLam(NhapChuan.docChuoiKhongRong("Nhap ma KQ"));
                    }
                    break;
                case 6: menuThongKe(); break;
                case 7:
                    int loai = NhapChuan.docSoNguyenTrongKhoang("1. Chi tiet de thi | 2. Chi tiet ket qua", 1, 2);
                    menuChiTiet(QuanLyDanhSach.DANHSACHCHITIET[loai - 1]);
                    break;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private static void menuGiangVien(GiangVien gv) {
        while (true) {
            System.out.println("\n=== MENU GIANG VIEN (" + gv.getMaGV() + ") ===");
            System.out.println("1. Quan ly Cau hoi (cua minh)");
            System.out.println("2. Tao de thi");
            System.out.println("3. Quan ly De thi (cua minh)");
            System.out.println("4. Xem ket qua de thi");
            System.out.println("5. Thong ke");
            System.out.println("0. Dang xuat");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            switch (chon) {
                case 1: menuDanhSach(QuanLyDanhSach.DS_CAUHOI); break;
                case 2: XuLyThi.taoDeThi(); break;
                case 3: menuDanhSach(QuanLyDanhSach.DS_DETHI); break;
                case 4: XuLyThi.xemKetQuaTheoBoLoc(gv.getMaGV(), false); break;
                case 5: menuThongKe(); break;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private static void menuSinhVien(SinhVien sv) {
        while (true) {
            System.out.println("\n=== MENU SINH VIEN (" + sv.getMaSV() + ") ===");
            System.out.println("1. Thi de duoc giao");
            System.out.println("2. Xem lich su ket qua");
            System.out.println("3. Xem chi tiet bai da lam");
            System.out.println("0. Dang xuat");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            switch (chon) {
                case 1: XuLyThi.lamBaiThi(sv); break;
                case 2:
                    String[][] bang = new String[1][7];
                    bang[0] = new String[]{"Ma KQ", "Ma SV", "Ma de", "Ngay thi", "Dung/Tong", "Diem", "Thoi gian"};
                    for (int i = 0; i < QuanLyDanhSach.DS_KETQUA.getSoLuong(); i++) {
                        KetQuaThi kq = QuanLyDanhSach.DS_KETQUA.docPhanTu(i);
                        if (kq.getMaSV().equalsIgnoreCase(sv.getMaSV())) {
                            bang = Arrays.copyOf(bang, bang.length + 1);
                            bang[bang.length - 1] = kq.xuat();
                        }
                    }
                    FormatBang.inBangDuLieu("LICH SU KET QUA", bang);
                    break;
                case 3:
                    String maKQ = NhapChuan.docChuoiKhongRong("Nhap ma KQ");
                    KetQuaThi kq = QuanLyDanhSach.DS_KETQUA.timKiemMa(maKQ);
                    if (kq == null || !kq.getMaSV().equalsIgnoreCase(sv.getMaSV())) {
                        System.out.println("Khong tim thay ket qua cua ban!");
                    } else {
                        XuLyThi.xemChiTietBaiLam(maKQ);
                    }
                    break;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }

    // menu thao tac CRUD chung cho cac danh sach chinh
    private static void menuDanhSach(IDanhSach ds) {
        while (true) {
            System.out.println("\n1. Xem | 2. Them | 3. Sua | 4. Xoa | 5. Tim kiem | 0. Quay lai");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            switch (chon) {
                case 1: ds.xuatDanhSach(); break;
                case 2: ds.nhapThem(); break;
                case 3: ds.nhapSua(); break;
                case 4: ds.nhapXoa(); break;
                case 5: ds.nhapTimKiem(); break;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private static void menuChiTiet(IChiTietDanhSach ds) {
        while (true) {
            System.out.println("\n1. Xem | 2. Tim kiem | 0. Quay lai");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            if (chon == 1) ds.xuatDanhSach();
            else if (chon == 2) ds.nhapTimKiem();
            else System.out.println("Lua chon khong hop le!");
        }
    }

    private static void menuThongKe() {
        while (true) {
            System.out.println("\n=== MENU THONG KE ===");
            System.out.println("1. Cau hoi theo Mon & Loai");
            System.out.println("2. Ket qua theo De thi");
            System.out.println("3. Luot thi theo Quy/Nam");
            System.out.println("0. Quay lai");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            switch (chon) {
                case 1: QuanLyDanhSach.thongKeCauHoiTheoMonVaLoai(); break;
                case 2: QuanLyDanhSach.thongKeKetQuaTheoDeThi(); break;
                case 3: QuanLyDanhSach.thongKeLuotThiTheoQuy(); break;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }
}
