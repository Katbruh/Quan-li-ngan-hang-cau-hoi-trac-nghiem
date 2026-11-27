package danhsach;

import java.io.*;
import mohinh.*;
import rangbuoc.ITruyVanDanhSach;
import tienich.*;

public class DanhSachKetQua implements ITruyVanDanhSach {
    private KetQuaThi[] ds;
    private int soLuong;

    public DanhSachKetQua() {
        this.ds = new KetQuaThi[0];
        this.soLuong = 0;
    }

    @Override
    public DanhSachKetQua saoChep() {
        DanhSachKetQua kq = new DanhSachKetQua();
        kq.ds = new KetQuaThi[soLuong];
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
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSKQ))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) {
                ds[i].ghiFile(dos);
            }
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSKQ);
        if (!f.exists()) {
            this.ds = new KetQuaThi[0];
            this.soLuong = 0;
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            KetQuaThi[] tam = new KetQuaThi[n];
            for (int i = 0; i < n; i++) {
                tam[i] = new KetQuaThi();
                tam[i].docFile(dis);
            }
            this.ds = tam;
            this.soLuong = n;
        }
    }

    private int timViTri(String maKQ) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equals(maKQ)) return i;
        }
        return -1;
    }

    private void them(KetQuaThi x) {
        KetQuaThi[] tam = new KetQuaThi[soLuong + 1];
        System.arraycopy(ds, 0, tam, 0, soLuong);
        tam[soLuong] = x;
        this.ds = tam;
        this.soLuong++;
    }

    public boolean kiemTraMa(String maKQ) {
        return timViTri(maKQ) != -1;
    }

    public KetQuaThi timKiemMa(String maKQ) {
        int viTri = timViTri(maKQ);
        return viTri == -1 ? null : ds[viTri];
    }

    public KetQuaThi docPhanTu(int viTri) {
        if (viTri < 0 || viTri >= soLuong) return null;
        return ds[viTri];
    }

    public String taoMaMoi() {
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMaKQ();
            if (ma != null && ma.startsWith("KQ")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {
                }
            }
        }
        return "KQ" + String.format("%03d", max + 1);
    }

    public void themVaoDanhSach(KetQuaThi kq) {
        them(kq);
    }

    public boolean daThi(String maSV, String maDe) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaSV().equals(maSV) && ds[i].getMaDe().equals(maDe)) return true;
        }
        return false;
    }

    public boolean kiemTraTonTaiDeThi(String maDe) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equals(maDe)) return true;
        }
        return false;
    }

    public boolean kiemTraTonTaiSinhVien(String maSV) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaSV().equals(maSV)) return true;
        }
        return false;
    }

    @Override
    public void nhapTimKiem() {
        System.out.println("1. Theo ma de");
        System.out.println("2. Theo ma sinh vien");
        System.out.println("3. Theo ma ket qua");
        System.out.println("4. Theo diem");
        int chon = NhapChuan.docSoNguyenTrongKhoang("Chon kieu tim", 1, 4);
        DanhSachKetQua dsTam = new DanhSachKetQua();
        if (chon == 1) {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma de");
            for (int i = 0; i < soLuong; i++) {
                if (ds[i].getMaDe().equals(ma)) dsTam.them(ds[i]);
            }
        } else if (chon == 2) {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma sinh vien");
            for (int i = 0; i < soLuong; i++) {
                if (ds[i].getMaSV().equals(ma)) dsTam.them(ds[i]);
            }
        } else if (chon == 3) {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma ket qua");
            for (int i = 0; i < soLuong; i++) {
                if (ds[i].getMaKQ().equals(ma)) dsTam.them(ds[i]);
            }
        } else {
            double diem = NhapChuan.docSoThucKhongAm("Nhap diem");
            String diemChuan = String.format("%.2f", diem);
            for (int i = 0; i < soLuong; i++) {
                if (String.format("%.2f", ds[i].getDiem()).equals(diemChuan)) dsTam.them(ds[i]);
            }
        }
        if (dsTam.getSoLuong() == 0) System.out.println("Khong tim thay!");
        else dsTam.xuatDanhSach();
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][8];
        bang[0] = new String[]{"Ma KQ", "Ma SV", "Ma de", "Ngay thi", "So cau dung", "Tong so cau", "Diem", "Thoi gian giay"};
        for (int i = 0; i < soLuong; i++) {
            bang[i + 1] = ds[i].xuat();
        }
        FormatBang.inBangDuLieu("DANH SACH KET QUA", bang);
    }
}
