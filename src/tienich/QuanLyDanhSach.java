package tienich;

import danhsach.DanhSachCauHoi;
import danhsach.DanhSachChiTietDeThi;
import danhsach.DanhSachChiTietKetQua;
import danhsach.DanhSachDeThi;
import danhsach.DanhSachKetQua;
import danhsach.DanhSachNguoiDung;
import java.io.IOException;
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

    public static void thongKeCauHoiTheoMonVaLoai() {
    }

    public static void thongKeKetQuaTheoDeThi() {
    }

    public static void thongKeLuotThiTheoQuy() {
    }
}
