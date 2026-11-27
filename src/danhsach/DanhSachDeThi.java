package danhsach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import mohinh.DeThi;
import mohinh.GiangVien;
import rangbuoc.IDanhSach;
import tienich.FormatBang;
import tienich.LuuTru;
import tienich.NhapChuan;
import tienich.QuanLyDanhSach;
import tienich.XuLyThi;

public class DanhSachDeThi implements IDanhSach {
    private DeThi[] ds;
    private int soLuong;

    public DanhSachDeThi() {
        this.ds = new DeThi[0];
        this.soLuong = 0;
    }

    public DanhSachDeThi(DanhSachDeThi x) {
        this.soLuong = x.soLuong;
        this.ds = new DeThi[x.soLuong];
        for (int i = 0; i < x.soLuong; i++) this.ds[i] = x.ds[i].saoChep();
    }

    @Override
    public IDanhSach saoChep() { return new DanhSachDeThi(this); }

    @Override
    public int getSoLuong() { return soLuong; }

    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSDT))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) ds[i].ghiFile(dos);
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSDT);
        if (!f.exists()) { ds = new DeThi[0]; soLuong = 0; return; }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ds = new DeThi[n > 0 ? n : 0];
            soLuong = 0;
            for (int i = 0; i < n; i++) {
                DeThi dt = new DeThi();
                dt.docFile(dis);
                ds[soLuong++] = dt;
            }
        }
    }

    private int timViTri(String maDe) {
        if (maDe == null) return -1;
        String key = maDe.trim();
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equalsIgnoreCase(key)) return i;
        }
        return -1;
    }

    private void them(DeThi dt) {
        if (dt == null) return;
        ds = Arrays.copyOf(ds, soLuong + 1);
        ds[soLuong++] = dt.saoChep();
    }

    private void xoa(int viTri) {
        for (int i = viTri; i < soLuong - 1; i++) ds[i] = ds[i + 1];
        soLuong--;
        ds[soLuong] = null;
        ds = Arrays.copyOf(ds, soLuong);
    }

    public boolean kiemTraMa(String maDe) { return timViTri(maDe) != -1; }

    public DeThi timKiemMa(String maDe) {
        int vt = timViTri(maDe);
        return vt == -1 ? null : ds[vt].saoChep();
    }

    public DeThi docPhanTu(int i) { return (i < 0 || i >= soLuong) ? null : ds[i].saoChep(); }

    public boolean kiemTraTonTaiGiangVien(String maGV) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaGV().equalsIgnoreCase(maGV)) return true;
        }
        return false;
    }

    public String taoMaMoi() {
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMaDe();
            if (ma.startsWith("DT")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {}
            }
        }
        return "DT" + String.format("%03d", max + 1);
    }

    public void themVaoDanhSach(DeThi dt) { them(dt); }

    @Override
    public void nhapTimKiem() {
        int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma de thi can tim"));
        if (vt == -1) { System.out.println("Khong tim thay de thi!"); return; }
        DanhSachDeThi dsTam = new DanhSachDeThi();
        dsTam.them(ds[vt]);
        dsTam.xuatDanhSach();
    }

    @Override
    public void nhapThem() {
        XuLyThi.taoDeThi();
    }

    @Override
    public void nhapSua() {
        do {
            int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma de thi can sua"));
            if (vt == -1) { System.out.println("Khong tim thay de thi!"); }
            else if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien
                    && !ds[vt].getMaGV().equalsIgnoreCase(QuanLyDanhSach.nguoiDungHienTai.getMa())) {
                System.out.println("Ban chi duoc sua de thi cua minh!");
            } else if (QuanLyDanhSach.DS_KETQUA.kiemTraTonTaiDeThi(ds[vt].getMaDe())) {
                System.out.println("Khong the sua! De thi da co sinh vien thi.");
            } else {
                ds[vt].nhap();
                System.out.println("Da cap nhat de thi!");
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    // xoa de thi dong thoi xoa chi tiet de thi neu chua co ai thi
    @Override
    public void nhapXoa() {
        do {
            int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma de thi can xoa"));
            if (vt == -1) { System.out.println("Khong tim thay de thi!"); }
            else if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien
                    && !ds[vt].getMaGV().equalsIgnoreCase(QuanLyDanhSach.nguoiDungHienTai.getMa())) {
                System.out.println("Ban chi duoc xoa de thi cua minh!");
            } else if (QuanLyDanhSach.DS_KETQUA.kiemTraTonTaiDeThi(ds[vt].getMaDe())) {
                System.out.println("Khong the xoa! De thi da co sinh vien thi.");
            } else {
                String maDe = ds[vt].getMaDe();
                xoa(vt);
                QuanLyDanhSach.DS_CHITIETDETHI.xoaTheoMa(maDe);
                System.out.println("Da xoa de thi va chi tiet kem theo!");
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][9];
        bang[0] = new String[]{"Ma de", "Ten de", "Ma mon", "Loai", "Thoi gian", "Ngay thi", "Ky thi", "Doi tuong", "Ma GV"};
        for (int i = 0; i < soLuong; i++) bang[i + 1] = ds[i].xuat();
        FormatBang.inBangDuLieu("DANH SACH DE THI", bang);
    }
}
