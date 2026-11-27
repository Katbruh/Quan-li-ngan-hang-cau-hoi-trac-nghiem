package tienich;

import danhsach.DanhSachCauHoi;
import danhsach.DanhSachChiTietDeThi;
import danhsach.DanhSachChiTietKetQua;
import danhsach.DanhSachDeThi;
import danhsach.DanhSachKetQua;
import danhsach.DanhSachNguoiDung;
import java.io.IOException;
import java.util.Arrays;
import mohinh.CauHoi;
import mohinh.KetQuaThi;
import mohinh.NguoiDung;
import rangbuoc.IChiTietDanhSach;
import rangbuoc.IDanhSach;
import rangbuoc.ITruyVanDanhSach;

public final class QuanLyDanhSach {
    public static NguoiDung nguoiDungHienTai = null;

    public static final DanhSachCauHoi DS_CAUHOI = new DanhSachCauHoi();
    public static final DanhSachNguoiDung DS_NGUOIDUNG = new DanhSachNguoiDung();
    public static final DanhSachDeThi DS_DETHI = new DanhSachDeThi();
    public static final DanhSachChiTietDeThi DS_CHITIETDETHI = new DanhSachChiTietDeThi();
    public static final DanhSachKetQua DS_KETQUA = new DanhSachKetQua();
    public static final DanhSachChiTietKetQua DS_CHITIETKETQUA = new DanhSachChiTietKetQua();

    public static final ITruyVanDanhSach[] DANHSACHTRUYCAPFILE = {
        DS_CAUHOI, DS_NGUOIDUNG, DS_DETHI, DS_CHITIETDETHI, DS_KETQUA, DS_CHITIETKETQUA
    };
    public static final IDanhSach[] DANHSACH = {DS_NGUOIDUNG, DS_CAUHOI, DS_DETHI};
    public static final IChiTietDanhSach[] DANHSACHCHITIET = {DS_CHITIETDETHI, DS_CHITIETKETQUA};

    private QuanLyDanhSach() {
    }

    // nap du lieu tu 6 file nhi phan khi khoi dong
    public static void taiDuLieuDanhSach() {
        for (int i = 0; i < DANHSACHTRUYCAPFILE.length; i++) {
            try {
                DANHSACHTRUYCAPFILE[i].nhapDanhSachTuFile();
            } catch (IOException e) {
                System.out.println("Loi doc file du lieu: " + e.getMessage());
            }
        }
    }

    // ghi toan bo du lieu ra 6 file nhi phan khi thoat
    public static void ghiDuLieuDanhSach() {
        for (int i = 0; i < DANHSACHTRUYCAPFILE.length; i++) {
            try {
                DANHSACHTRUYCAPFILE[i].xuatDanhSachRaFile();
            } catch (IOException e) {
                System.out.println("Loi ghi file du lieu: " + e.getMessage());
            }
        }
    }

    // thong ke so luong cau hoi theo tung mon hoc va loai cau hoi
    public static void thongKeCauHoiTheoMonVaLoai() {
        String[] dsMon = new String[0];
        for (int i = 0; i < DS_CAUHOI.getSoLuong(); i++) {
            String mon = DS_CAUHOI.docPhanTu(i).getMaMonHoc();
            boolean co = false;
            for (int j = 0; j < dsMon.length; j++) {
                if (dsMon[j].equalsIgnoreCase(mon)) { co = true; break; }
            }
            if (!co) {
                dsMon = Arrays.copyOf(dsMon, dsMon.length + 1);
                dsMon[dsMon.length - 1] = mon;
            }
        }
        if (dsMon.length == 0) { System.out.println("(Khong co du lieu thong ke)"); return; }
        double[][] bang = new double[dsMon.length][3];
        for (int i = 0; i < DS_CAUHOI.getSoLuong(); i++) {
            CauHoi ch = DS_CAUHOI.docPhanTu(i);
            for (int j = 0; j < dsMon.length; j++) {
                if (dsMon[j].equalsIgnoreCase(ch.getMaMonHoc())) {
                    bang[j][ch.getLoaiCauHoi() - 1] += 1;
                    break;
                }
            }
        }
        String[] cot = {"Mot dap an", "Nhieu dap an", "Dung/Sai", "Tong cong"};
        FormatBang.inBangThongKe(dsMon, cot, bang);
    }

    public static void thongKeKetQuaTheoDeThi() {
        int n = DS_DETHI.getSoLuong();
        if (n == 0) { System.out.println("(Khong co du lieu thong ke)"); return; }
        String[] dsMaDe = new String[n];
        double[][] bang = new double[n][4];
        for (int i = 0; i < n; i++) {
            String maDe = DS_DETHI.docPhanTu(i).getMaDe();
            dsMaDe[i] = maDe;
            int dem = 0;
            double tong = 0, max = 0, min = 0;
            for (int j = 0; j < DS_KETQUA.getSoLuong(); j++) {
                KetQuaThi kq = DS_KETQUA.docPhanTu(j);
                if (kq.getMaDe().equalsIgnoreCase(maDe)) {
                    double d = kq.getDiem();
                    if (dem == 0) { max = d; min = d; }
                    else { if (d > max) max = d; if (d < min) min = d; }
                    tong += d;
                    dem++;
                }
            }
            if (dem > 0) {
                bang[i][0] = dem;
                bang[i][1] = tong / dem;
                bang[i][2] = max;
                bang[i][3] = min;
            }
        }
        String[] cot = {"So SV da thi", "Diem TB", "Cao nhat", "Thap nhat"};
        FormatBang.inBangThongKe(dsMaDe, cot, bang);
    }

    public static void thongKeLuotThiTheoQuy() {
        int nam = NhapChuan.docNam("Nhap nam thong ke");
        String[] dong = {"Nam " + nam};
        String[] cot = {"Quy 1", "Quy 2", "Quy 3", "Quy 4", "Tong cong"};
        double[][] bang = new double[1][4];
        for (int i = 0; i < DS_KETQUA.getSoLuong(); i++) {
            KetQuaThi kq = DS_KETQUA.docPhanTu(i);
            if (kq.getNgayThi() != null && kq.getNgayThi().getYear() == nam) {
                int quy = (kq.getNgayThi().getMonthValue() - 1) / 3;
                bang[0][quy] += 1;
            }
        }
        FormatBang.inBangThongKe(dong, cot, bang);
    }
}
