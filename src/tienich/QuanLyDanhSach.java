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
import mohinh.DeThi;
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

    public static final ITruyVanDanhSach[] DANHSACHTRUYCAPFILE = {DS_CAUHOI, DS_NGUOIDUNG, DS_DETHI, DS_CHITIETDETHI, DS_KETQUA, DS_CHITIETKETQUA};
    public static final IDanhSach[] DANHSACH = {DS_NGUOIDUNG, DS_CAUHOI, DS_DETHI};
    public static final IChiTietDanhSach[] DANHSACHCHITIET = {DS_CHITIETDETHI, DS_CHITIETKETQUA};

    private QuanLyDanhSach() {
    }

    // nap va ghi du lieu cho ca 6 danh sach
    public static void taiDuLieuDanhSach() {
        for (int i = 0; i < DANHSACHTRUYCAPFILE.length; i++) {
            try {
                DANHSACHTRUYCAPFILE[i].nhapDanhSachTuFile();
            } catch (IOException e) {
                System.out.println("Khong the doc du lieu: " + e.getMessage());
            }
        }
    }

    public static void ghiDuLieuDanhSach() {
        for (int i = 0; i < DANHSACHTRUYCAPFILE.length; i++) {
            try {
                DANHSACHTRUYCAPFILE[i].xuatDanhSachRaFile();
            } catch (IOException e) {
                System.out.println("Khong the ghi du lieu: " + e.getMessage());
            }
        }
    }

    // 3 ham thong ke dung chung khuon 3 buoc
    public static void thongKeCauHoiTheoMonVaLoai() {
        String[] mon = new String[DS_CAUHOI.getSoLuong()];
        int soMon = 0;
        for (int i = 0; i < DS_CAUHOI.getSoLuong(); i++) {
            CauHoi ch = DS_CAUHOI.docPhanTu(i);
            boolean daCo = false;
            for (int j = 0; j < soMon; j++) {
                if (mon[j].equals(ch.getMaMonHoc())) {
                    daCo = true;
                    break;
                }
            }
            if (!daCo) mon[soMon++] = ch.getMaMonHoc();
        }
        String[] tieuDeDong = Arrays.copyOf(mon, soMon);
        double[][] bangSoLieu = new double[soMon][3];
        for (int i = 0; i < DS_CAUHOI.getSoLuong(); i++) {
            CauHoi ch = DS_CAUHOI.docPhanTu(i);
            for (int j = 0; j < soMon; j++) {
                if (mon[j].equals(ch.getMaMonHoc())) {
                    if (ch.getLoaiCauHoi() == 1) bangSoLieu[j][0]++;
                    else if (ch.getLoaiCauHoi() == 2) bangSoLieu[j][1]++;
                    else bangSoLieu[j][2]++;
                    break;
                }
            }
        }
        FormatBang.inBangThongKe(tieuDeDong, new String[]{"Mot dap an", "Nhieu dap an", "Dung/Sai", "Tong cong"}, bangSoLieu);
    }

    public static void thongKeKetQuaTheoDeThi() {
        String[] tieuDeDong = new String[DS_DETHI.getSoLuong()];
        double[][] bangSoLieu = new double[DS_DETHI.getSoLuong()][4];
        for (int i = 0; i < DS_DETHI.getSoLuong(); i++) {
            DeThi de = DS_DETHI.docPhanTu(i);
            tieuDeDong[i] = de.getMaDe();
            double tong = 0;
            double cao = 0;
            double thap = 0;
            int dem = 0;
            for (int j = 0; j < DS_KETQUA.getSoLuong(); j++) {
                KetQuaThi kq = DS_KETQUA.docPhanTu(j);
                if (kq.getMaDe().equals(de.getMaDe())) {
                    double diem = kq.getDiem();
                    if (dem == 0) {
                        cao = diem;
                        thap = diem;
                    } else {
                        if (diem > cao) cao = diem;
                        if (diem < thap) thap = diem;
                    }
                    tong += diem;
                    dem++;
                }
            }
            bangSoLieu[i][0] = dem;
            bangSoLieu[i][1] = dem == 0 ? 0 : tong / dem;
            bangSoLieu[i][2] = cao;
            bangSoLieu[i][3] = thap;
        }
        FormatBang.inBangThongKe(tieuDeDong, new String[]{"So SV da thi", "Diem TB", "Cao nhat", "Thap nhat"}, bangSoLieu);
    }

    public static void thongKeLuotThiTheoQuy() {
        int nam = NhapChuan.docNam("Nhap nam thong ke");
        String[] tieuDeDong = {"Nam " + nam};
        double[][] bangSoLieu = new double[1][4];
        for (int i = 0; i < DS_KETQUA.getSoLuong(); i++) {
            KetQuaThi kq = DS_KETQUA.docPhanTu(i);
            if (kq.getNgayThi().getYear() == nam) {
                int quy = (kq.getNgayThi().getMonthValue() - 1) / 3;
                bangSoLieu[0][quy]++;
            }
        }
        FormatBang.inBangThongKe(tieuDeDong, new String[]{"Quy 1", "Quy 2", "Quy 3", "Quy 4", "Tong cong"}, bangSoLieu);
    }
}
