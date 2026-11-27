package danhsach;

import java.io.*;
import mohinh.*;
import rangbuoc.IDanhSach;
import tienich.*;

public class DanhSachNguoiDung implements IDanhSach {
    // ======================================================
    private NguoiDung[] ds;
    private int soLuong;

    public DanhSachNguoiDung() {
        this.ds = new NguoiDung[0];
        this.soLuong = 0;
    }

    @Override
    public DanhSachNguoiDung saoChep() {
        DanhSachNguoiDung kq = new DanhSachNguoiDung();
        kq.ds = new NguoiDung[soLuong];
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
        if (!f.exists()) {
            this.ds = new NguoiDung[0];
            this.soLuong = 0;
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            NguoiDung[] tam = new NguoiDung[n];
            for (int i = 0; i < n; i++) {
                int loai = dis.readInt();
                if (loai == 1) tam[i] = new SinhVien();
                else tam[i] = new GiangVien();
                tam[i].docFile(dis);
            }
            this.ds = tam;
            this.soLuong = n;
        }
    }

    // ======================================================
    private int timViTri(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMa().equals(ma)) return i;
        }
        return -1;
    }

    private void them(NguoiDung x) {
        NguoiDung[] tam = new NguoiDung[soLuong + 1];
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

    // ======================================================
    public boolean kiemTraMa(String ma) {
        return timViTri(ma) != -1;
    }
    public NguoiDung timKiemMa(String ma) {
        int viTri = timViTri(ma);
        return viTri == -1 ? null : ds[viTri];
    }
    public NguoiDung docPhanTu(int viTri) {
        if (viTri < 0 || viTri >= soLuong) return null;
        return ds[viTri];
    }
    public String taoMaMoi(int loai) {
        String tienTo = loai == 1 ? "SV" : "GV";
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMa();
            if (ma != null && ma.startsWith(tienTo)) {
                try {
                    int so = Integer.parseInt(ma.substring(tienTo.length()));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {
                }
            }
        }
        return tienTo + String.format("%03d", max + 1);
    }
    public NguoiDung timTheoTenDangNhap(String user) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getTenDangNhap().equals(user)) return ds[i];
        }
        return null;
    }

    public NguoiDung dangNhap(String user, String pass) {
        NguoiDung nd = timTheoTenDangNhap(user);
        if (nd != null && nd.getMatKhau().equals(pass)) return nd;
        return null;
    }
    @Override
    public void nhapTimKiem() {
        String ma = NhapChuan.docChuoiKhongRong("Nhap ma nguoi dung can tim");
        int viTri = timViTri(ma);
        if (viTri == -1) {
            System.out.println("Khong tim thay!");
            return;
        }
        DanhSachNguoiDung dsTam = new DanhSachNguoiDung();
        dsTam.them(ds[viTri]);
        dsTam.xuatDanhSach();
    }

    @Override
    public void nhapThem() {
        if (!(QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien)) {
            System.out.println("Chi quan tri vien duoc thao tac!");
            return;
        }
        int loai = NhapChuan.docSoNguyenTrongKhoang("Loai nguoi dung (1=SV, 2=GV)", 1, 2);
        String ma = taoMaMoi(loai);
        NguoiDung nd;
        if (loai == 1) {
            SinhVien sv = new SinhVien();
            sv.setMaSV(ma);
            sv.setTenDangNhap(ma);
            sv.setMatKhau("123456");
            nd = sv;
        } else {
            GiangVien gv = new GiangVien();
            gv.setMaGV(ma);
            gv.setTenDangNhap(ma);
            gv.setMatKhau("123456");
            nd = gv;
        }
        nd.nhap();
        them(nd);
        System.out.println("Da them nguoi dung " + ma);
    }

    @Override
    public void nhapSua() {
        if (!(QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien)) {
            System.out.println("Chi quan tri vien duoc thao tac!");
            return;
        }
        do {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma nguoi dung can sua");
            int viTri = timViTri(ma);
            if (viTri == -1) {
                System.out.println("Khong tim thay!");
            } else {
                ds[viTri].nhap();
                System.out.println("Da sua nguoi dung " + ma);
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void nhapXoa() {
        if (!(QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien)) {
            System.out.println("Chi quan tri vien duoc thao tac!");
            return;
        }
        do {
            String ma = NhapChuan.docChuoiKhongRong("Nhap ma nguoi dung can xoa");
            int viTri = timViTri(ma);
            if (viTri == -1) {
                System.out.println("Khong tim thay!");
            } else {
                NguoiDung nd = ds[viTri];
                boolean biChan = false;
                if (nd instanceof GiangVien) {
                    biChan = QuanLyDanhSach.DS_CAUHOI.kiemTraTonTaiGiangVien(ma)
                            || QuanLyDanhSach.DS_DETHI.kiemTraTonTaiGiangVien(ma);
                } else if (nd instanceof SinhVien) {
                    biChan = QuanLyDanhSach.DS_KETQUA.kiemTraTonTaiSinhVien(ma);
                }
                if (biChan) {
                    System.out.println("Khong the xoa: nguoi dung dang duoc tham chieu!");
                } else {
                    xoa(viTri);
                    System.out.println("Da xoa nguoi dung " + ma);
                }
            }
        } while (NhapChuan.docYesNo("Co tiep tuc khong", false));
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][5];
        bang[0] = new String[]{"Ma", "Ho ten", "Ten dang nhap", "Vai tro", "Thong tin"};
        for (int i = 0; i < soLuong; i++) {
            bang[i + 1] = ds[i].xuat();
        }
        FormatBang.inBangDuLieu("DANH SACH NGUOI DUNG", bang);
    }
}
