package danhsach;

import java.io.*;
import mohinh.*;
import rangbuoc.IDanhSach;
import tienich.*;

public class DanhSachDeThi implements IDanhSach {
    private DeThi[] ds;
    private int soLuong;

    public DanhSachDeThi() {
        this.ds = new DeThi[0];
        this.soLuong = 0;
    }

    @Override
    public DanhSachDeThi saoChep() {
        DanhSachDeThi kq = new DanhSachDeThi();
        kq.ds = new DeThi[soLuong];
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
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSDT))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) {
                ds[i].ghiFile(dos);
            }
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSDT);
        if (!f.exists()) {
            this.ds = new DeThi[0];
            this.soLuong = 0;
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            DeThi[] tam = new DeThi[n];
            for (int i = 0; i < n; i++) {
                tam[i] = new DeThi();
                tam[i].docFile(dis);
            }
            this.ds = tam;
            this.soLuong = n;
        }
    }

    private int timViTri(String maDe) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equals(maDe)) return i;
        }
        return -1;
    }

    private void them(DeThi x) {
        DeThi[] tam = new DeThi[soLuong + 1];
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

    private boolean duocPhepSua(DeThi de) {
        if (QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien) {
            return de.getMaGV().equals(((GiangVien) QuanLyDanhSach.nguoiDungHienTai).getMa());
        }
        return QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien;
    }

    public boolean kiemTraMa(String maDe) {
        return timViTri(maDe) != -1;
    }

    public DeThi timKiemMa(String maDe) {
        int viTri = timViTri(maDe);
        return viTri == -1 ? null : ds[viTri];
    }

    public DeThi docPhanTu(int viTri) {
        if (viTri < 0 || viTri >= soLuong) return null;
        return ds[viTri];
    }

    public String taoMaMoi() {
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMaDe();
            if (ma != null && ma.startsWith("DT")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {
                }
            }
        }
        return "DT" + String.format("%03d", max + 1);
    }

    public void themVaoDanhSach(DeThi de) {
        them(de);
    }

    public boolean kiemTraTonTaiGiangVien(String maGV) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaGV().equals(maGV)) return true;
        }
        return false;
    }

    @Override
    public void nhapTimKiem() {
        String ma = NhapChuan.docChuoiKhongRong("Nhap ma de can tim");
        int viTri = timViTri(ma);
        if (viTri == -1) {
            System.out.println("Khong tim thay!");
            return;
        }
        DanhSachDeThi dsTam = new DanhSachDeThi();
        dsTam.them(ds[viTri]);
        dsTam.xuatDanhSach();
    }

    @Override
    public void nhapThem() {
        boolean laGV = QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien;
        boolean laQTV = QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien;
        if (!laGV && !laQTV) {
            System.out.println("Ban khong co quyen!");
            return;
        }
        DeThi de = new DeThi();
        de.nhap();
        de.setMaDe(taoMaMoi());
        if (laGV) {
            de.setMaGV(((GiangVien) QuanLyDanhSach.nguoiDungHienTai).getMa());
        } else {
            while (!de.setMaGV(NhapChuan.docChuoiKhongRong("Ma giang vien"))) {
                System.out.println("Ma giang vien khong hop le!");
            }
        }
        them(de);
        System.out.println("Da them de thi " + de.getMaDe());
    }

    @Override
    public void nhapSua() {
        do {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma de can sua");
            int viTri = timViTri(ma);
            if (viTri == -1) {
                System.out.println("Khong tim thay!");
            } else if (!duocPhepSua(ds[viTri])) {
                System.out.println("Ban khong co quyen sua de thi nay!");
            } else if (QuanLyDanhSach.DS_KETQUA.kiemTraTonTaiDeThi(ma)) {
                System.out.println("Khong the sua: de thi da co ket qua!");
            } else {
                ds[viTri].nhap();
                System.out.println("Da sua de thi " + ma);
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void nhapXoa() {
        do {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma de can xoa");
            int viTri = timViTri(ma);
            if (viTri == -1) {
                System.out.println("Khong tim thay!");
            } else if (!duocPhepSua(ds[viTri])) {
                System.out.println("Ban khong co quyen xoa de thi nay!");
            } else if (QuanLyDanhSach.DS_KETQUA.kiemTraTonTaiDeThi(ma)) {
                System.out.println("Khong the xoa: de thi da co ket qua!");
            } else {
                QuanLyDanhSach.DS_CHITIETDETHI.xoaTheoMa(ma);
                xoa(viTri);
                System.out.println("Da xoa de thi " + ma);
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][10];
        bang[0] = new String[]{"Ma de", "Ten de", "Ma mon", "Loai", "Thoi gian", "Ngay thi", "Ma ky thi", "Loai doi tuong", "Doi tuong", "Ma GV"};
        for (int i = 0; i < soLuong; i++) {
            bang[i + 1] = ds[i].xuat();
        }
        FormatBang.inBangDuLieu("DANH SACH DE THI", bang);
    }
}
