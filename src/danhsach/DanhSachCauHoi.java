package danhsach;

import java.io.*;
import java.util.Arrays;
import mohinh.*;
import rangbuoc.IDanhSach;
import tienich.*;

public class DanhSachCauHoi implements IDanhSach {
    private CauHoi[] ds;
    private int soLuong;

    public DanhSachCauHoi() {
        this.ds = new CauHoi[0];
        this.soLuong = 0;
    }

    public DanhSachCauHoi(DanhSachCauHoi x) {
        this.soLuong = x.soLuong;
        this.ds = new CauHoi[x.soLuong];
        for (int i = 0; i < x.soLuong; i++) {
            this.ds[i] = x.ds[i].saoChep();
        }
    }

    @Override
    public IDanhSach saoChep() { return new DanhSachCauHoi(this); }

    @Override
    public int getSoLuong() { return soLuong; }

    // ======================================================
    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSCH))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) {
                dos.writeInt(ds[i].getLoaiCauHoi());
                ds[i].ghiFile(dos);
            }
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSCH);
        if (!f.exists()) { ds = new CauHoi[0]; soLuong = 0; return; }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ds = new CauHoi[n > 0 ? n : 0];
            soLuong = 0;
            for (int i = 0; i < n; i++) {
                int loai = dis.readInt();
                CauHoi ch = loai == 1 ? new CauHoiMotDapAn() : (loai == 2 ? new CauHoiNhieuDapAn() : new CauHoiDungSai());
                ch.docFile(dis);
                ds[soLuong++] = ch;
            }
        }
    }

    // ------------------------------------
    private int timViTri(String maCH) {
        if (maCH == null) return -1;
        String key = maCH.trim();
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaCH().equalsIgnoreCase(key)) return i;
        }
        return -1;
    }

    private void them(CauHoi ch) {
        if (ch == null) return;
        ds = Arrays.copyOf(ds, soLuong + 1);
        ds[soLuong++] = ch.saoChep();
    }

    private void xoa(int viTri) {
        for (int i = viTri; i < soLuong - 1; i++) ds[i] = ds[i + 1];
        soLuong--;
        ds[soLuong] = null;
        ds = Arrays.copyOf(ds, soLuong);
    }

    // ======================================================
    public boolean kiemTraMa(String maCH) { return timViTri(maCH) != -1; }

    public CauHoi timKiemMa(String maCH) {
        int vt = timViTri(maCH);
        return vt == -1 ? null : ds[vt].saoChep();
    }

    public CauHoi docPhanTu(int i) {
        return (i < 0 || i >= soLuong) ? null : ds[i].saoChep();
    }

    public boolean kiemTraTonTaiGiangVien(String maGV) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaGV().equalsIgnoreCase(maGV)) return true;
        }
        return false;
    }

    public String taoMaMoi(int loai) {
        String prefix = loai == 1 ? "M" : (loai == 2 ? "N" : "DS");
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMaCH();
            if (ma.startsWith(prefix)) {
                try {
                    int so = Integer.parseInt(ma.substring(prefix.length()));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {}
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    public void themVaoDanhSach(CauHoi ch) { them(ch); }

    public void nhap() { nhapThem(); }

    // ======================================================
    @Override
    public void nhapTimKiem() {
        String ma = NhapChuan.docChuoiKhongRong("Nhap ma cau hoi can tim");
        int vt = timViTri(ma);
        if (vt == -1) { System.out.println("Khong tim thay cau hoi!"); return; }
        DanhSachCauHoi dsTam = new DanhSachCauHoi();
        dsTam.them(ds[vt]);
        dsTam.xuatDanhSach();
        ds[vt].xuatChiTiet();
    }

    @Override
    public void nhapThem() {
        int loai = NhapChuan.docSoNguyenTrongKhoang("Loai cau hoi (1: Mot DA, 2: Nhieu DA, 3: Dung/Sai)", 1, 3);
        CauHoi ch = loai == 1 ? new CauHoiMotDapAn() : (loai == 2 ? new CauHoiNhieuDapAn() : new CauHoiDungSai());
        ch.nhap();
        ch.setMaCH(taoMaMoi(loai));
        if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien) {
            GiangVien gv = (GiangVien) QuanLyDanhSach.nguoiDungHienTai;
            ch.setMaGV(gv.getMa());
            ch.setMaMonHoc(gv.getMaMonHoc());
        } else {
            ch.setMaGV(NhapChuan.docChuoiKhongRong("Ma giang vien"));
        }
        them(ch);
        System.out.println("Da them cau hoi: " + ch.getMaCH());
    }

    @Override
    public void nhapSua() {
        do {
            int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma cau hoi can sua"));
            if (vt == -1) { System.out.println("Khong tim thay cau hoi!"); }
            else if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien
                    && !ds[vt].getMaGV().equalsIgnoreCase(QuanLyDanhSach.nguoiDungHienTai.getMa())) {
                System.out.println("Ban chi duoc sua cau hoi cua minh!");
            } else {
                ds[vt].nhap();
                System.out.println("Da cap nhat cau hoi!");
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void nhapXoa() {
        do {
            int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma cau hoi can xoa"));
            if (vt == -1) { System.out.println("Khong tim thay cau hoi!"); }
            else if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien
                    && !ds[vt].getMaGV().equalsIgnoreCase(QuanLyDanhSach.nguoiDungHienTai.getMa())) {
                System.out.println("Ban chi duoc xoa cau hoi cua minh!");
            } else if (QuanLyDanhSach.DS_CHITIETDETHI.kiemTraTonTaiCauHoi(ds[vt].getMaCH())) {
                System.out.println("Khong the xoa! Cau hoi dang co trong de thi.");
            } else {
                xoa(vt);
                System.out.println("Da xoa cau hoi!");
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][6];
        bang[0] = new String[]{"Ma CH", "Ma mon", "Noi dung", "Ma GV", "Loai", "Dap an"};
        for (int i = 0; i < soLuong; i++) bang[i + 1] = ds[i].xuat();
        FormatBang.inBangDuLieu("DANH SACH CAU HOI", bang);
    }
}
