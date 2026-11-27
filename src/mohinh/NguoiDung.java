package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public abstract class NguoiDung implements ITruyXuatMoHinh {
    protected String tenDangNhap;
    protected String matKhau;
    protected String hoTen;

    public NguoiDung() {
        this.tenDangNhap = "";
        this.matKhau = "";
        this.hoTen = "";
    }

    public NguoiDung(String tenDangNhap, String matKhau, String hoTen) {
        if (!setTenDangNhap(tenDangNhap)) this.tenDangNhap = "";
        if (!setMatKhau(matKhau)) this.matKhau = "";
        if (!setHoTen(hoTen)) this.hoTen = "";
    }

    public NguoiDung(NguoiDung x) {
        this.tenDangNhap = x.tenDangNhap;
        this.matKhau = x.matKhau;
        this.hoTen = x.hoTen;
    }

    public abstract NguoiDung saoChep();

    @Override
    public String[] xuat() {
        return new String[]{getMa(), hoTen, tenDangNhap, getVaiTro()};
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        setHoTen(NhapChuan.docChuoiKhongRong("Ho ten"));
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        dos.writeUTF(tenDangNhap == null ? "" : tenDangNhap);
        dos.writeUTF(matKhau == null ? "" : matKhau);
        dos.writeUTF(hoTen == null ? "" : hoTen);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        this.tenDangNhap = dis.readUTF();
        this.matKhau = dis.readUTF();
        this.hoTen = dis.readUTF();
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getHoTen() { return hoTen; }

    public boolean setTenDangNhap(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) return false;
        this.tenDangNhap = tenDangNhap.trim();
        return true;
    }

    public boolean setMatKhau(String matKhau) {
        if (matKhau == null || matKhau.trim().isEmpty()) return false;
        this.matKhau = matKhau.trim();
        return true;
    }

    public boolean setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) return false;
        this.hoTen = hoTen.trim();
        return true;
    }

    public abstract String getVaiTro();
    public abstract String getMa();
}
