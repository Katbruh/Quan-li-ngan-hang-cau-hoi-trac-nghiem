package danhsach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import mohinh.ChiTietKetQua;
import rangbuoc.IChiTietDanhSach;
import tienich.FormatBang;
import tienich.LuuTru;
import tienich.NhapChuan;

public class DanhSachChiTietKetQua implements IChiTietDanhSach {
    private ChiTietKetQua[] ds;
    private int soLuong;

    public DanhSachChiTietKetQua() {
        this.ds = new ChiTietKetQua[0];
        this.soLuong = 0;
    }

    public DanhSachChiTietKetQua(DanhSachChiTietKetQua x) {
        this.soLuong = x.soLuong;
        this.ds = new ChiTietKetQua[x.soLuong];
        for (int i = 0; i < x.soLuong; i++) this.ds[i] = x.ds[i].saoChep();
    }

    @Override
    public IChiTietDanhSach saoChep() { return new DanhSachChiTietKetQua(this); }

    @Override
    public int getSoLuong() { return soLuong; }

    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSCTKQ))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) ds[i].ghiFile(dos);
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSCTKQ);
        if (!f.exists()) { ds = new ChiTietKetQua[0]; soLuong = 0; return; }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ds = new ChiTietKetQua[n > 0 ? n : 0];
            soLuong = 0;
            for (int i = 0; i < n; i++) {
                ChiTietKetQua ct = new ChiTietKetQua();
                ct.docFile(dis);
                ds[soLuong++] = ct;
            }
        }
    }

    private void them(ChiTietKetQua ct) {
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

    public ChiTietKetQua docPhanTu(int i) { return (i < 0 || i >= soLuong) ? null : ds[i].saoChep(); }

    public void themVaoDanhSach(ChiTietKetQua ct) { them(ct); }

    // xoa toan bo chi tiet cua mot ma ket qua
    @Override
    public void xoaTheoMa(String maKQ) {
        for (int i = 0; i < soLuong; ) {
            if (ds[i].getMaKQ().equalsIgnoreCase(maKQ)) xoa(i);
            else i++;
        }
    }

    @Override
    public int demTheoMa(String maKQ) {
        int dem = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equalsIgnoreCase(maKQ)) dem++;
        }
        return dem;
    }

    public ChiTietKetQua[] layTheoMaKQ(String maKQ) {
        int n = demTheoMa(maKQ);
        ChiTietKetQua[] kq = new ChiTietKetQua[n];
        int idx = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equalsIgnoreCase(maKQ)) kq[idx++] = ds[i].saoChep();
        }
        return kq;
    }

    public ChiTietKetQua timKiemNhieuMa(String maKQ, String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equalsIgnoreCase(maKQ) && ds[i].getMaCH().equalsIgnoreCase(maCH)) {
                return ds[i].saoChep();
            }
        }
        return null;
    }

    @Override
    public void nhapTimKiem() {
        String maKQ = NhapChuan.docChuoiKhongRong("Nhap ma ket qua");
        String maCH = NhapChuan.docChuoi("Nhap ma cau hoi (Enter de xem tat ca)");
        DanhSachChiTietKetQua dsTam = new DanhSachChiTietKetQua();
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equalsIgnoreCase(maKQ) && (maCH.isEmpty() || ds[i].getMaCH().equalsIgnoreCase(maCH))) {
                dsTam.them(ds[i]);
            }
        }
        if (dsTam.getSoLuong() == 0) System.out.println("Khong tim thay chi tiet ket qua!");
        else dsTam.xuatDanhSach();
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][5];
        bang[0] = new String[]{"Ma KQ", "Ma cau hoi", "Tra loi", "Dap an dung", "Diem dat"};
        for (int i = 0; i < soLuong; i++) bang[i + 1] = ds[i].xuat();
        FormatBang.inBangDuLieu("CHI TIET KET QUA", bang);
    }
}
