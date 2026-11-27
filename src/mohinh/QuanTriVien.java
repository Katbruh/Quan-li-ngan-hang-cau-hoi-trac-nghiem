package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class QuanTriVien extends NguoiDung {
    private String maQTV;

    public QuanTriVien() {
        super();
        this.maQTV = "";
    }

    public QuanTriVien(String maQTV, String tenDangNhap, String matKhau, String hoTen) {
        super(tenDangNhap, matKhau, hoTen);
        if (!setMaQTV(maQTV)) this.maQTV = "";
    }

    public QuanTriVien(QuanTriVien x) {
        super(x);
        this.maQTV = x.maQTV;
    }

    @Override
    public NguoiDung saoChep() {
        return new QuanTriVien(this);
    }

    @Override
    public String[] xuat() {
        String[] coBan = super.xuat();
        String[] ketQua = Arrays.copyOf(coBan, coBan.length + 1);
        ketQua[coBan.length] = "Toan quyen";
        return ketQua;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        setMaQTV(NhapChuan.docChuoiKhongRong("Ma quan tri vien"));
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        dos.writeUTF(maQTV == null ? "" : maQTV);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        this.maQTV = dis.readUTF();
    }

    public String getMaQTV() { return maQTV; }

    @Override
    public String getVaiTro() { return "Quan tri vien"; }

    @Override
    public String getMa() { return maQTV; }

    public boolean setMaQTV(String maQTV) {
        if (maQTV == null || maQTV.trim().isEmpty()) return false;
        this.maQTV = maQTV.trim();
        return true;
    }
}
