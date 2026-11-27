package danhsach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import mohinh.KetQuaThi;
import rangbuoc.ITruyVanDanhSach;
import tienich.FormatBang;
import tienich.LuuTru;
import tienich.NhapChuan;

public class DanhSachKetQua implements ITruyVanDanhSach {
    private KetQuaThi[] ds;
    private int soLuong;

    public DanhSachKetQua() {
        this.ds = new KetQuaThi[0];
        this.soLuong = 0;
    }

    public DanhSachKetQua(DanhSachKetQua x) {
        this.soLuong = x.soLuong;
        this.ds = new KetQuaThi[x.soLuong];
        for (int i = 0; i < x.soLuong; i++) this.ds[i] = x.ds[i].saoChep();
    }

    @Override
    public ITruyVanDanhSach saoChep() { return new DanhSachKetQua(this); }

    @Override
    public int getSoLuong() { return soLuong; }

    @Override
    public void xuatDanhSachRaFile() throws IOException {
        new File("data").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSKQ))) {
            dos.writeInt(soLuong);
            for (int i = 0; i < soLuong; i++) ds[i].ghiFile(dos);
        }
    }

    @Override
    public void nhapDanhSachTuFile() throws IOException {
        File f = new File(LuuTru.FILEDSKQ);
        if (!f.exists()) { ds = new KetQuaThi[0]; soLuong = 0; return; }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            ds = new KetQuaThi[n > 0 ? n : 0];
            soLuong = 0;
            for (int i = 0; i < n; i++) {
                KetQuaThi kq = new KetQuaThi();
                kq.docFile(dis);
                ds[soLuong++] = kq;
            }
        }
    }

    private int timViTri(String maKQ) {
        if (maKQ == null) return -1;
        String key = maKQ.trim();
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKQ().equalsIgnoreCase(key)) return i;
        }
        return -1;
    }

    private void them(KetQuaThi kq) {
        if (kq == null) return;
        ds = Arrays.copyOf(ds, soLuong + 1);
        ds[soLuong++] = kq.saoChep();
    }

    public boolean kiemTraMa(String maKQ) { return timViTri(maKQ) != -1; }

    public KetQuaThi timKiemMa(String maKQ) {
        int vt = timViTri(maKQ);
        return vt == -1 ? null : ds[vt].saoChep();
    }

    public KetQuaThi docPhanTu(int i) { return (i < 0 || i >= soLuong) ? null : ds[i].saoChep(); }

    public String taoMaMoi() {
        int max = 0;
        for (int i = 0; i < soLuong; i++) {
            String ma = ds[i].getMaKQ();
            if (ma.startsWith("KQ")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException e) {}
            }
        }
        return "KQ" + String.format("%03d", max + 1);
    }

    public void themVaoDanhSach(KetQuaThi kq) { them(kq); }

    public boolean daThi(String maSV, String maDe) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaSV().equalsIgnoreCase(maSV) && ds[i].getMaDe().equalsIgnoreCase(maDe)) return true;
        }
        return false;
    }

    public boolean kiemTraTonTaiDeThi(String maDe) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaDe().equalsIgnoreCase(maDe)) return true;
        }
        return false;
    }

    public boolean kiemTraTonTaiSinhVien(String maSV) {
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaSV().equalsIgnoreCase(maSV)) return true;
        }
        return false;
    }

    // ho tro tim theo ma ket qua, ma de, ma sinh vien hoac diem
    @Override
    public void nhapTimKiem() {
        int chon = NhapChuan.docSoNguyenTrongKhoang("Tim theo (1: Ma KQ, 2: Ma de, 3: Ma SV, 4: Diem)", 1, 4);
        DanhSachKetQua dsTam = new DanhSachKetQua();
        if (chon == 4) {
            String target = String.format(Locale.US, "%.2f", NhapChuan.docSoThucKhongAm("Nhap diem can tim"));
            for (int i = 0; i < soLuong; i++) {
                if (String.format(Locale.US, "%.2f", ds[i].getDiem()).equals(target)) dsTam.them(ds[i]);
            }
        } else {
            String key = NhapChuan.docChuoiKhongRong("Nhap ma can tim");
            for (int i = 0; i < soLuong; i++) {
                String giaTri = chon == 1 ? ds[i].getMaKQ() : (chon == 2 ? ds[i].getMaDe() : ds[i].getMaSV());
                if (giaTri.equalsIgnoreCase(key)) dsTam.them(ds[i]);
            }
        }
        if (dsTam.getSoLuong() == 0) System.out.println("Khong tim thay ket qua!");
        else dsTam.xuatDanhSach();
    }

    @Override
    public void xuatDanhSach() {
        String[][] bang = new String[soLuong + 1][7];
        bang[0] = new String[]{"Ma KQ", "Ma SV", "Ma de", "Ngay thi", "Dung/Tong", "Diem", "Thoi gian"};
        for (int i = 0; i < soLuong; i++) bang[i + 1] = ds[i].xuat();
        FormatBang.inBangDuLieu("DANH SACH KET QUA THI", bang);
    }
}
