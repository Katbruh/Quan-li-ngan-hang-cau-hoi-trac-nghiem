package danhsach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import mohinh.ChiTietDeThi;
import rangbuoc.IChiTietDanhSach;
import tienich.FormatBang;
import tienich.LuuTru;
import tienich.NhapChuan;

public class DanhSachChiTietDeThi implements IChiTietDanhSach {
    private ChiTietDeThi[] ds;
    private int soLuong;

    public DanhSachChiTietDeThi() {
        this.ds = new ChiTietDeThi[0];
        this.soLuong = 0;
    }

    public DanhSachChiTietDeThi(DanhSachChiTietDeThi x) {
        this.soLuong = x.soLuong;
        this.ds = new ChiTietDeThi[x.soLuong];
        for (int i = 0; i < x.soLuong; i++) this.ds[i] = x.ds[i].saoChep();
    }

    @Override
    public IChiTietDanhSach saoChep() { return new DanhSachChiTietDeThi(this); }

    @Override
    public int getSoLuong() { return soLuong; }

    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSCTDT))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) ds[i].ghiFile(dos);
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSCTDT);
        if (!f.exists()) { ds = new ChiTietDeThi[0]; soLuong = 0; return; }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ds = new ChiTietDeThi[n > 0 ? n : 0];
            soLuong = 0;
            for (int i = 0; i < n; i++) {
                ChiTietDeThi ct = new ChiTietDeThi();
                ct.docFile(dis);
                ds[soLuong++] = ct;
            }
        }
    }

    private void them(ChiTietDeThi ct) {
        if (ct == null) return;
        ds = Arrays.copyOf(ds, soLuong + 1);
        ds[soLuong++] = ct.saoChep();
    }

    private void xoa(int viTri) {
        for (int i = viTri; i < soLuong - 1; i++) ds[i] = ds[i + 1];
        soLuong--;
        ds[soLuong] = null;
        ds = Arrays.copyOf(ds, soLuong);
    }

    public ChiTietDeThi docPhanTu(int i) { return (i < 0 || i >= soLuong) ? null : ds[i].saoChep(); }

    public void themVaoDanhSach(ChiTietDeThi ct) { them(ct); }

    // xoa toan bo chi tiet cua mot ma de thi
    @Override
    public void xoaTheoMa(String maDe) {
        for (int i = 0; i < soLuong; ) {
            if (ds[i].getMaDe().equalsIgnoreCase(maDe)) xoa(i);
            else i++;
        }
    }

    @Override
    public int demTheoMa(String maDe) {
        int dem = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equalsIgnoreCase(maDe)) dem++;
        }
        return dem;
    }

    public boolean kiemTraTonTaiCauHoi(String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaCH().equalsIgnoreCase(maCH)) return true;
        }
        return false;
    }

    public ChiTietDeThi[] layTheoMaDe(String maDe) {
        int n = demTheoMa(maDe);
        ChiTietDeThi[] kq = new ChiTietDeThi[n];
        int idx = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equalsIgnoreCase(maDe)) kq[idx++] = ds[i].saoChep();
        }
        return kq;
    }

    public ChiTietDeThi timKiemNhieuMa(String maDe, String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equalsIgnoreCase(maDe) && ds[i].getMaCH().equalsIgnoreCase(maCH)) {
                return ds[i].saoChep();
            }
        }
        return null;
    }

    @Override
    public void nhapTimKiem() {
        String maDe = NhapChuan.docChuoiKhongRong("Nhap ma de thi");
        String maCH = NhapChuan.docChuoi("Nhap ma cau hoi (Enter de xem tat ca)");
        DanhSachChiTietDeThi dsTam = new DanhSachChiTietDeThi();
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equalsIgnoreCase(maDe) && (maCH.isEmpty() || ds[i].getMaCH().equalsIgnoreCase(maCH))) {
                dsTam.them(ds[i]);
            }
        }
        if (dsTam.getSoLuong() == 0) System.out.println("Khong tim thay chi tiet de thi!");
        else dsTam.xuatDanhSach();
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][3];
        bang[0] = new String[]{"Ma de", "Ma cau hoi", "Diem toi da"};
        for (int i = 0; i < soLuong; i++) bang[i + 1] = ds[i].xuat();
        FormatBang.inBangDuLieu("CHI TIET DE THI", bang);
    }
}
