package danhsach;

import java.io.*;
import mohinh.*;
import rangbuoc.IChiTietDanhSach;
import tienich.*;

public class DanhSachChiTietDeThi implements IChiTietDanhSach {
    private ChiTietDeThi[] ds;
    private int soLuong;

    public DanhSachChiTietDeThi() {
        this.ds = new ChiTietDeThi[0];
        this.soLuong = 0;
    }

    @Override
    public DanhSachChiTietDeThi saoChep() {
        DanhSachChiTietDeThi kq = new DanhSachChiTietDeThi();
        kq.ds = new ChiTietDeThi[soLuong];
        for (int i = 0; i < soLuong; i++) {
            kq.ds[i] = ds[i].saoChep();
        }
        kq.soLuong = soLuong;
        return kq;
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    // doc ghi file nhi phan
    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSCTDT))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) {
                ds[i].ghiFile(dos);
            }
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSCTDT);
        if (!f.exists()) {
            this.ds = new ChiTietDeThi[0];
            this.soLuong = 0;
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ChiTietDeThi[] tam = new ChiTietDeThi[n];
            for (int i = 0; i < n; i++) {
                tam[i] = new ChiTietDeThi();
                tam[i].docFile(dis);
            }
            this.ds = tam;
            this.soLuong = n;
        }
    }

    private int timViTri(String maDe, String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equals(maDe) && ds[i].getMaCH().equals(maCH)) return i;
        }
        return -1;
    }

    private void them(ChiTietDeThi x) {
        ChiTietDeThi[] tam = new ChiTietDeThi[soLuong + 1];
        System.arraycopy(ds, 0, tam, 0, soLuong);
        tam[soLuong] = x;
        this.ds = tam;
        this.soLuong++;
    }

    private void xoa(int viTri) {
        for (int i = viTri; i < soLuong - 1; i++) {
            ds[i] = ds[i + 1];
        }
        soLuong--;
        ds[soLuong] = null;
    }

    public void themVaoDanhSach(ChiTietDeThi ct) {
        them(ct);
    }

    @Override
    public void xoaTheoMa(String maDe) {
        for (int i = soLuong - 1; i >= 0; i--) {
            if (ds[i].getMaDe().equals(maDe)) xoa(i);
        }
    }

    @Override
    public int demTheoMa(String maDe) {
        int dem = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equals(maDe)) dem++;
        }
        return dem;
    }

    public boolean kiemTraTonTaiCauHoi(String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaCH().equals(maCH)) return true;
        }
        return false;
    }

    public ChiTietDeThi[] layTheoMaDe(String maDe) {
        ChiTietDeThi[] kq = new ChiTietDeThi[demTheoMa(maDe)];
        int j = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equals(maDe)) kq[j++] = ds[i];
        }
        return kq;
    }

    public ChiTietDeThi timKiemNhieuMa(String maDe, String maCH) {
        int viTri = timViTri(maDe, maCH);
        return viTri == -1 ? null : ds[viTri];
    }

    public ChiTietDeThi docPhanTu(int viTri) {
        if (viTri < 0 || viTri >= soLuong) return null;
        return ds[viTri];
    }

    @Override
    public void nhapTimKiem() {
        String maDe = NhapChuan.docChuoi("Nhap ma de (bo trong de bo qua)");
        String maCH = NhapChuan.docChuoi("Nhap ma cau hoi (bo trong de bo qua)");
        DanhSachChiTietDeThi dsTam = new DanhSachChiTietDeThi();
        for (int i = 0; i < soLuong; i++) {
            boolean khopDe = maDe.isEmpty() || ds[i].getMaDe().equals(maDe);
            boolean khopCH = maCH.isEmpty() || ds[i].getMaCH().equals(maCH);
            if (khopDe && khopCH) dsTam.them(ds[i]);
        }
        if (dsTam.getSoLuong() == 0) System.out.println("Khong tim thay!");
        else dsTam.xuatDanhSach();
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][3];
        bang[0] = new String[]{"Ma de", "Ma CH", "Diem toi da"};
        for (int i = 0; i < soLuong; i++) {
            bang[i + 1] = ds[i].xuat();
        }
        FormatBang.inBangDuLieu("DANH SACH CHI TIET DE THI", bang);
    }
}
