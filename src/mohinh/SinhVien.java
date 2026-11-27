package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class SinhVien extends NguoiDung {
    private String maSV;
    private String lop;

    public SinhVien() {
        super();
        this.maSV = "";
        this.lop = "";
    }

    public SinhVien(String maSV, String tenDangNhap, String matKhau, String hoTen, String lop) {
        super(tenDangNhap, matKhau, hoTen);
        if (!setMaSV(maSV)) this.maSV = "";
        if (!setLop(lop)) this.lop = "";
    }

    public SinhVien(SinhVien x) {
        super(x);
        this.maSV = x.maSV;
        this.lop = x.lop;
    }

    @Override
    public NguoiDung saoChep() {
        return new SinhVien(this);
    }

    @Override
    public String[] xuat() {
        String[] goc = super.xuat();
        String[] kq = Arrays.copyOf(goc, goc.length + 1);
        kq[goc.length] = lop;
        return kq;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        setLop(NhapChuan.docChuoiKhongRong("Lop"));
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        dos.writeUTF(maSV == null ? "" : maSV);
        dos.writeUTF(lop == null ? "" : lop);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        this.maSV = dis.readUTF();
        this.lop = dis.readUTF();
    }

    public String getMaSV() { return maSV; }
    public String getLop() { return lop; }
    @Override public String getVaiTro() { return "Sinh vien"; }
    @Override public String getMa() { return maSV; }

    public boolean setMaSV(String maSV) {
        if (maSV == null || maSV.trim().isEmpty()) return false;
        this.maSV = maSV.trim();
        return true;
    }

    public boolean setLop(String lop) {
        if (lop == null || lop.trim().isEmpty()) return false;
        this.lop = lop.trim();
        return true;
    }
}
