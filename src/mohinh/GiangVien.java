package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class GiangVien extends NguoiDung {
    private String maGV;
    private String maMonHoc;

    public GiangVien() {
        super();
        this.maGV = "";
        this.maMonHoc = "";
    }

    public GiangVien(String maGV, String tenDangNhap, String matKhau, String hoTen, String maMonHoc) {
        super(tenDangNhap, matKhau, hoTen);
        if (!setMaGV(maGV)) this.maGV = "";
        if (!setMaMonHoc(maMonHoc)) this.maMonHoc = "";
    }

    public GiangVien(GiangVien x) {
        super(x);
        this.maGV = x.maGV;
        this.maMonHoc = x.maMonHoc;
    }

    @Override
    public NguoiDung saoChep() {
        return new GiangVien(this);
    }

    @Override
    public String[] xuat() {
        String[] goc = super.xuat();
        String[] kq = Arrays.copyOf(goc, goc.length + 1);
        kq[goc.length] = maMonHoc;
        return kq;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        setMaMonHoc(NhapChuan.docChuoiKhongRong("Ma mon hoc"));
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        dos.writeUTF(maGV == null ? "" : maGV);
        dos.writeUTF(maMonHoc == null ? "" : maMonHoc);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        this.maGV = dis.readUTF();
        this.maMonHoc = dis.readUTF();
    }

    public String getMaGV() { return maGV; }
    public String getMaMonHoc() { return maMonHoc; }
    @Override public String getVaiTro() { return "Giang vien"; }
    @Override public String getMa() { return maGV; }

    public boolean setMaGV(String maGV) {
        if (maGV == null || maGV.trim().isEmpty()) return false;
        this.maGV = maGV.trim();
        return true;
    }

    public boolean setMaMonHoc(String maMonHoc) {
        if (maMonHoc == null || maMonHoc.trim().isEmpty()) return false;
        this.maMonHoc = maMonHoc.trim();
        return true;
    }
}
