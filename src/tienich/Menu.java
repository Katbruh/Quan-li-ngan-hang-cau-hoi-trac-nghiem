package tienich;

import mohinh.*;
import rangbuoc.IChiTietDanhSach;
import rangbuoc.IDanhSach;

public final class Menu {
    private Menu() {
    }

    // vong lap chinh cua chuong trinh
    public static void run() {
        QuanLyDanhSach.taiDuLieuDanhSach();
        while (true) {
            System.out.println("\n=== HE THONG NGAN HANG CAU HOI ===");
            System.out.println("Goi y: admin/123456 | GV48217/123456 | SV48217/123456");
            System.out.println("1. Dang nhap");
            System.out.println("0. Thoat chuong trinh");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) {
                QuanLyDanhSach.ghiDuLieuDanhSach();
                System.out.println("Da luu du lieu. Tam biet!");
                break;
            }
            if (chon != 1) continue;
            NguoiDung nd = dangNhap();
            if (nd == null) {
                System.out.println("Sai ten dang nhap hoac mat khau!");
                continue;
            }
            QuanLyDanhSach.nguoiDungHienTai = nd;
            System.out.println("Xin chao " + nd.getHoTen() + " (" + nd.getVaiTro() + ")");
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
            System.out.println("\n--- MENU QUAN TRI VIEN ---");
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
            if (chon == 1) menuDanhSach(QuanLyDanhSach.DANHSACH[0]);
            else if (chon == 2) menuDanhSach(QuanLyDanhSach.DANHSACH[1]);
            else if (chon == 3) XuLyThi.taoDeThi();
            else if (chon == 4) menuDanhSach(QuanLyDanhSach.DANHSACH[2]);
            else if (chon == 5) menuXemKetQuaQTV();
            else if (chon == 6) menuThongKe();
            else if (chon == 7) menuChonChiTiet();
        }
    }

    private static void menuGiangVien(GiangVien gv) {
        while (true) {
            System.out.println("\n--- MENU GIANG VIEN ---");
            System.out.println("1. Quan ly Cau hoi (cua minh)");
            System.out.println("2. Tao de thi");
            System.out.println("3. Quan ly De thi (cua minh)");
            System.out.println("4. Xem ket qua de thi");
            System.out.println("5. Thong ke");
            System.out.println("0. Dang xuat");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            if (chon == 1) menuDanhSach(QuanLyDanhSach.DS_CAUHOI);
            else if (chon == 2) XuLyThi.taoDeThi();
            else if (chon == 3) menuDanhSach(QuanLyDanhSach.DS_DETHI);
            else if (chon == 4) XuLyThi.xemKetQuaTheoBoLoc(gv.getMaGV(), false);
            else if (chon == 5) menuThongKe();
        }
    }

    private static void menuSinhVien(SinhVien sv) {
        while (true) {
            System.out.println("\n--- MENU SINH VIEN ---");
            System.out.println("1. Thi de duoc giao");
            System.out.println("2. Xem lich su ket qua");
            System.out.println("3. Xem chi tiet bai da lam");
            System.out.println("0. Dang xuat");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            if (chon == 1) XuLyThi.lamBaiThi(sv);
            else if (chon == 2) xemLichSuSV(sv);
            else if (chon == 3) xemChiTietCuaSV(sv);
        }
    }

    // menu thao tac dung chung qua interface
    private static void menuDanhSach(IDanhSach ds) {
        while (true) {
            System.out.println("\n1. Xem | 2. Them | 3. Sua | 4. Xoa | 5. Tim kiem | 0. Quay lai");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            if (chon == 1) ds.xuatDanhSach();
            else if (chon == 2) ds.nhapThem();
            else if (chon == 3) ds.nhapSua();
            else if (chon == 4) ds.nhapXoa();
            else if (chon == 5) ds.nhapTimKiem();
        }
    }

    private static void menuChiTiet(IChiTietDanhSach ds) {
        while (true) {
            System.out.println("\n1. Xem | 2. Tim kiem | 0. Quay lai");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            if (chon == 1) ds.xuatDanhSach();
            else if (chon == 2) ds.nhapTimKiem();
        }
    }

    private static void menuThongKe() {
        while (true) {
            System.out.println("\n--- MENU THONG KE ---");
            System.out.println("1. Cau hoi theo Mon & Loai");
            System.out.println("2. Ket qua theo De thi");
            System.out.println("3. Luot thi theo Quy/Nam");
            System.out.println("0. Quay lai");
            int chon = NhapChuan.nhapLuaChonMenu();
            if (chon == 0) break;
            if (chon == 1) QuanLyDanhSach.thongKeCauHoiTheoMonVaLoai();
            else if (chon == 2) QuanLyDanhSach.thongKeKetQuaTheoDeThi();
            else if (chon == 3) QuanLyDanhSach.thongKeLuotThiTheoQuy();
        }
    }

    private static void menuXemKetQuaQTV() {
        System.out.println("1. Loc ket qua | 2. Xem chi tiet bai lam theo maKQ");
        int chon = NhapChuan.docSoNguyenTrongKhoang("Chon", 1, 2);
        if (chon == 1) XuLyThi.xemKetQuaTheoBoLoc("", true);
        else XuLyThi.xemChiTietBaiLam(NhapChuan.docChuoiKhongRong("Nhap maKQ"));
    }

    private static void menuChonChiTiet() {
        System.out.println("1. Chi tiet de thi | 2. Chi tiet ket qua");
        int chon = NhapChuan.docSoNguyenTrongKhoang("Chon", 1, 2);
        menuChiTiet(QuanLyDanhSach.DANHSACHCHITIET[chon - 1]);
    }

    private static void xemLichSuSV(SinhVien sv) {
        int n = QuanLyDanhSach.DS_KETQUA.getSoLuong();
        KetQuaThi[] tam = new KetQuaThi[n];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            KetQuaThi kq = QuanLyDanhSach.DS_KETQUA.docPhanTu(i);
            if (kq.getMaSV().equals(sv.getMaSV())) tam[dem++] = kq;
        }
        String[][] bang = new String[dem + 1][8];
        bang[0] = new String[]{"Ma KQ", "Ma SV", "Ma de", "Ngay thi", "So dung", "Tong cau", "Diem", "Thoi gian (s)"};
        for (int i = 0; i < dem; i++) bang[i + 1] = tam[i].xuat();
        FormatBang.inBangDuLieu("LICH SU KET QUA", bang);
    }

    private static void xemChiTietCuaSV(SinhVien sv) {
        String maKQ = NhapChuan.docChuoiKhongRong("Nhap ma KQ muon xem");
        KetQuaThi kq = QuanLyDanhSach.DS_KETQUA.timKiemMa(maKQ);
        if (kq == null || !kq.getMaSV().equals(sv.getMaSV())) {
            System.out.println("Khong tim thay ket qua cua ban!");
            return;
        }
        XuLyThi.xemChiTietBaiLam(maKQ);
    }
}
