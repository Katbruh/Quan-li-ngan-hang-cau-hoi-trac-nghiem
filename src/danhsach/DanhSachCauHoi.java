package danhsach;

import java.io.*;
import mohinh.*;
import rangbuoc.IDanhSach;
import tienich.*;

public class DanhSachCauHoi implements IDanhSach {
    // ======================================================
    private CauHoi[] ds;
    private int soLuong;

    public DanhSachCauHoi() {
        this.ds = new CauHoi[0];
        this.soLuong = 0;
    }

    @Override
    public DanhSachCauHoi saoChep() {
        DanhSachCauHoi kq = new DanhSachCauHoi();
        kq.ds = new CauHoi[soLuong];
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

    // ------------------------------------
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
        if (!f.exists()) {
            this.ds = new CauHoi[0];
            this.soLuong = 0;
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            CauHoi[] tam = new CauHoi[n];
            for (int i = 0; i < n; i++) {
                int loai = dis.readInt();
                if (loai == 1) tam[i] = new CauHoiMotDapAn();
                else if (loai == 2) tam[i] = new CauHoiNhieuDapAn();
                else tam[i] = new CauHoiDungSai();
                tam[i].docFile(dis);
            }
            this.ds = tam;
            this.soLuong = n;
        }
    }

    // ======================================================
    private int timViTri(String maCH) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaCH().equals(maCH)) return i;
        }
        return -1;
    }

    private void them(CauHoi x) {
        CauHoi[] tam = new CauHoi[soLuong + 1];
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

    private boolean duocPhepSua(CauHoi ch) {
        if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien) {
            return ch.getMaGV().equals(((GiangVien) QuanLyDanhSach.nguoiDungHienTai).getMa());
        }
        return QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien;
    }

    // ======================================================
    public boolean kiemTraMa(String maCH) {
        return timViTri(maCH) != -1;
    }

    public CauHoi timKiemMa(String maCH) {
        int viTri = timViTri(maCH);
        return viTri == -1 ? null : ds[viTri];
    }

    public CauHoi docPhanTu(int viTri) {
        if (viTri < 0 || viTri >= soLuong) return null;
        return ds[viTri];
    }

    public String taoMaMoi(int loai) {
        String tienTo = loai == 1 ? "M" : (loai == 2 ? "N" : "DS");
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getLoaiCauHoi() == loai) {
                String ma = ds[i].getMaCH();
                if (ma != null && ma.startsWith(tienTo)) {
                    try {
                        int so = Integer.parseInt(ma.substring(tienTo.length()));
                        if (so > max) max = so;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return tienTo + String.format("%03d", max + 1);
    }

    public boolean kiemTraTonTaiGiangVien(String maGV) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaGV().equals(maGV)) return true;
        }
        return false;
    }

    public void nhap() {
        nhapThem();
    }

    @Override
    public void nhapTimKiem() {
        String ma = NhapChuan.docChuoiKhongRong("Nhap ma cau hoi can tim");
        int viTri = timViTri(ma);
        if (viTri == -1) {
            System.out.println("Khong tim thay!");
            return;
        }
        DanhSachCauHoi dsTam = new DanhSachCauHoi();
        dsTam.them(ds[viTri]);
        dsTam.xuatDanhSach();
        ds[viTri].xuatChiTiet();
    }

    @Override
    public void nhapThem() {
        boolean laGV = QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien;
        boolean laQTV = QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien;
        if (!laGV && !laQTV) {
            System.out.println("Ban khong co quyen!");
            return;
        }
        int loai = NhapChuan.docSoNguyenTrongKhoang("Loai cau hoi (1/2/3)", 1, 3);
        CauHoi ch;
        if (loai == 1) ch = new CauHoiMotDapAn();
        else if (loai == 2) ch = new CauHoiNhieuDapAn();
        else ch = new CauHoiDungSai();
        ch.nhap();
        ch.setMaCH(taoMaMoi(loai));
        if (laGV) {
            ch.setMaGV(((GiangVien) QuanLyDanhSach.nguoiDungHienTai).getMa());
        } else {
            while (!ch.setMaGV(NhapChuan.docChuoiKhongRong("Ma giang vien"))) {
                System.out.println("Ma giang vien khong hop le!");
            }
        }
        them(ch);
        System.out.println("Da them cau hoi " + ch.getMaCH());
    }

    @Override
    public void nhapSua() {
        do {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma cau hoi can sua");
            int viTri = timViTri(ma);
            if (viTri == -1) {
                System.out.println("Khong tim thay!");
            } else if (!duocPhepSua(ds[viTri])) {
                System.out.println("Ban khong co quyen sua cau hoi nay!");
            } else {
                ds[viTri].nhap();
                System.out.println("Da sua cau hoi " + ma);
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void nhapXoa() {
        do {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma cau hoi can xoa");
            int viTri = timViTri(ma);
            if (viTri == -1) {
                System.out.println("Khong tim thay!");
            } else if (!duocPhepSua(ds[viTri])) {
                System.out.println("Ban khong co quyen xoa cau hoi nay!");
            } else if (QuanLyDanhSach.DS_CHITIETDETHI.kiemTraTonTaiCauHoi(ma)) {
                System.out.println("Khong the xoa: cau hoi dang nam trong de thi!");
            } else {
                xoa(viTri);
                System.out.println("Da xoa cau hoi " + ma);
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][6];
        bang[0] = new String[]{"Ma CH", "Ma mon", "Noi dung", "Ma GV", "Loai", "Dap an"};
        for (int i = 0; i < soLuong; i++) {
            bang[i + 1] = ds[i].xuat();
        }
        FormatBang.inBangDuLieu("DANH SACH CAU HOI", bang);
    }
}
