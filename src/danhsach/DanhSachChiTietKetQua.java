package danhsach;

import java.io.*;
import mohinh.*;
import rangbuoc.IChiTietDanhSach;
import tienich.*;

public class DanhSachChiTietKetQua implements IChiTietDanhSach {
    private ChiTietKetQua[] ds;
    private int soLuong;

    public DanhSachChiTietKetQua() {
        this.ds = new ChiTietKetQua[0];
        this.soLuong = 0;
    }

    @Override
    public DanhSachChiTietKetQua saoChep() {
        DanhSachChiTietKetQua kq = new DanhSachChiTietKetQua();
        kq.ds = new ChiTietKetQua[soLuong];
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
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSCTKQ))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) {
                ds[i].ghiFile(dos);
            }
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSCTKQ);
        if (!f.exists()) {
            this.ds = new ChiTietKetQua[0];
            this.soLuong = 0;
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ChiTietKetQua[] tam = new ChiTietKetQua[n];
            for (int i = 0; i < n; i++) {
                tam[i] = new ChiTietKetQua();
                tam[i].docFile(dis);
            }
            this.ds = tam;
            this.soLuong = n;
        }
    }

    private int timViTri(String maKQ, String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equals(maKQ) && ds[i].getMaCH().equals(maCH)) return i;
        }
        return -1;
    }

    private void them(ChiTietKetQua x) {
        ChiTietKetQua[] tam = new ChiTietKetQua[soLuong + 1];
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

    public void themVaoDanhSach(ChiTietKetQua ct) {
        them(ct);
    }

    @Override
    public void xoaTheoMa(String maKQ) {
        for (int i = soLuong - 1; i >= 0; i--) {
            if (ds[i].getMaKQ().equals(maKQ)) xoa(i);
        }
    }

    @Override
    public int demTheoMa(String maKQ) {
        int dem = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equals(maKQ)) dem++;
        }
        return dem;
    }

    public ChiTietKetQua[] layTheoMaKQ(String maKQ) {
        ChiTietKetQua[] kq = new ChiTietKetQua[demTheoMa(maKQ)];
        int j = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equals(maKQ)) kq[j++] = ds[i];
        }
        return kq;
    }

    public ChiTietKetQua timKiemNhieuMa(String maKQ, String maCH) {
        int viTri = timViTri(maKQ, maCH);
        return viTri == -1 ? null : ds[viTri];
    }

    public ChiTietKetQua docPhanTu(int viTri) {
        if (viTri < 0 || viTri >= soLuong) return null;
        return ds[viTri];
    }

    @Override
    public void nhapTimKiem() {
        String maKQ = NhapChuan.docChuoi("Nhap ma ket qua (bo trong de bo qua)");
        String maCH = NhapChuan.docChuoi("Nhap ma cau hoi (bo trong de bo qua)");
        DanhSachChiTietKetQua dsTam = new DanhSachChiTietKetQua();
        for (int i = 0; i < soLuong; i++) {
            boolean khopKQ = maKQ.isEmpty() || ds[i].getMaKQ().equals(maKQ);
            boolean khopCH = maCH.isEmpty() || ds[i].getMaCH().equals(maCH);
            if (khopKQ && khopCH) dsTam.them(ds[i]);
        }
        if (dsTam.getSoLuong() == 0) System.out.println("Khong tim thay!");
        else dsTam.xuatDanhSach();
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][5];
        bang[0] = new String[]{"Ma KQ", "Ma CH", "Tra loi", "Dap an dung", "Diem dat duoc"};
        for (int i = 0; i < soLuong; i++) {
            bang[i + 1] = ds[i].xuat();
        }
        FormatBang.inBangDuLieu("DANH SACH CHI TIET KET QUA", bang);
    }
}
