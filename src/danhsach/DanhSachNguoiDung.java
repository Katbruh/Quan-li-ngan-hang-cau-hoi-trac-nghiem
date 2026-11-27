package danhsach;

import java.io.*;
import java.util.Arrays;
import mohinh.*;
import rangbuoc.IDanhSach;
import tienich.*;

public class DanhSachNguoiDung implements IDanhSach {
    private NguoiDung[] ds;
    private int soLuong;

    public DanhSachNguoiDung() {
        this.ds = new NguoiDung[0];
        this.soLuong = 0;
    }

    public DanhSachNguoiDung(DanhSachNguoiDung x) {
        this.soLuong = x.soLuong;
        this.ds = new NguoiDung[x.soLuong];
        for (int i = 0; i < x.soLuong; i++) {
            this.ds[i] = x.ds[i].saoChep();
        }
    }

    @Override
    public IDanhSach saoChep() { return new DanhSachNguoiDung(this); }

    @Override
    public int getSoLuong() { return soLuong; }

    // ======================================================
    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSND))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) {
                dos.writeInt(ds[i] instanceof SinhVien ? 1 : 2);
                ds[i].ghiFile(dos);
            }
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSND);
        if (!f.exists()) { ds = new NguoiDung[0]; soLuong = 0; return; }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ds = new NguoiDung[n > 0 ? n : 0];
            soLuong = 0;
            for (int i = 0; i < n; i++) {
                int loai = dis.readInt();
                NguoiDung nd = loai == 1 ? new SinhVien() : new GiangVien();
                nd.docFile(dis);
                ds[soLuong++] = nd;
            }
        }
    }

    // ------------------------------------
    private int timViTri(String ma) {
        if (ma == null) return -1;
        String key = ma.trim();
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMa().equalsIgnoreCase(key)) return i;
        }
        return -1;
    }

    private void them(NguoiDung nd) {
        if (nd == null) return;
        ds = Arrays.copyOf(ds, soLuong + 1);
        ds[soLuong++] = nd.saoChep();
    }

    private void xoa(int viTri) {
        for (int i = viTri; i < soLuong - 1; i++) ds[i] = ds[i + 1];
        soLuong--;
        ds[soLuong] = null;
        ds = Arrays.copyOf(ds, soLuong);
    }

    // ======================================================
    public boolean kiemTraMa(String ma) { return timViTri(ma) != -1; }

    public NguoiDung timKiemMa(String ma) {
        int vt = timViTri(ma);
        return vt == -1 ? null : ds[vt].saoChep();
    }

    public NguoiDung docPhanTu(int i) {
        return (i < 0 || i >= soLuong) ? null : ds[i].saoChep();
    }

    public NguoiDung dangNhap(String user, String pass) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getTenDangNhap().equals(user) && ds[i].getMatKhau().equals(pass)) {
                return ds[i].saoChep();
            }
        }
        return null;
    }

    public String taoMaMoi(int loai) {
        String prefix = loai == 1 ? "SV" : "GV";
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMa();
            if (ma.startsWith(prefix)) {
                try {
                    int so = Integer.parseInt(ma.substring(prefix.length()));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {}
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    public void themVaoDanhSach(NguoiDung nd) { them(nd); }

    // ======================================================
    @Override
    public void nhapTimKiem() {
        int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma nguoi dung can tim"));
        if (vt == -1) { System.out.println("Khong tim thay nguoi dung!"); return; }
        DanhSachNguoiDung dsTam = new DanhSachNguoiDung();
        dsTam.them(ds[vt]);
        dsTam.xuatDanhSach();
    }

    @Override
    public void nhapThem() {
        int loai = NhapChuan.docSoNguyenTrongKhoang("Loai nguoi dung (1: Sinh vien, 2: Giang vien)", 1, 2);
        String ma = taoMaMoi(loai);
        if (loai == 1) {
            SinhVien sv = new SinhVien();
            sv.nhap();
            sv.setMaSV(ma);
            sv.setTenDangNhap(ma);
            sv.setMatKhau("123456");
            them(sv);
        } else {
            GiangVien gv = new GiangVien();
            gv.nhap();
            gv.setMaGV(ma);
            gv.setTenDangNhap(ma);
            gv.setMatKhau("123456");
            them(gv);
        }
        System.out.println("Da them nguoi dung: " + ma + " (mat khau mac dinh: 123456)");
    }

    @Override
    public void nhapSua() {
        do {
            int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma nguoi dung can sua"));
            if (vt == -1) { System.out.println("Khong tim thay nguoi dung!"); }
            else {
                ds[vt].nhap();
                System.out.println("Da cap nhat nguoi dung!");
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void nhapXoa() {
        do {
            int vt = timViTri(NhapChuan.docChuoiKhongRong("Nhap ma nguoi dung can xoa"));
            if (vt == -1) { System.out.println("Khong tim thay nguoi dung!"); }
            else {
                String ma = ds[vt].getMa();
                if (ds[vt] instanceof GiangVien && (QuanLyDanhSach.DS_CAUHOI.kiemTraTonTaiGiangVien(ma)
                        || QuanLyDanhSach.DS_DETHI.kiemTraTonTaiGiangVien(ma))) {
                    System.out.println("Khong the xoa! Giang vien dang co cau hoi hoac de thi.");
                } else if (ds[vt] instanceof SinhVien && QuanLyDanhSach.DS_KETQUA.kiemTraTonTaiSinhVien(ma)) {
                    System.out.println("Khong the xoa! Sinh vien da co ket qua thi.");
                } else {
                    xoa(vt);
                    System.out.println("Da xoa nguoi dung!");
                }
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][5];
        bang[0] = new String[]{"Ma ND", "Ho ten", "Ten dang nhap", "Vai tro", "Lop / Mon"};
        for (int i = 0; i < soLuong; i++) bang[i + 1] = ds[i].xuat();
        FormatBang.inBangDuLieu("DANH SACH NGUOI DUNG", bang);
    }
}
