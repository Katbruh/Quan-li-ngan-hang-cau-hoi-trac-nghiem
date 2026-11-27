package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class ChiTietDeThi implements ITruyXuatMoHinh {
    private String maDe;
    private String maCH;
    private double diemToiDa;

    public ChiTietDeThi() {
        this.maDe = "";
        this.maCH = "";
        this.diemToiDa = 0;
    }

    public ChiTietDeThi(String maDe, String maCH, double diemToiDa) {
        if (!setMaDe(maDe)) this.maDe = "";
        if (!setMaCH(maCH)) this.maCH = "";
        if (!setDiemToiDa(diemToiDa)) this.diemToiDa = 0;
    }

    public ChiTietDeThi(ChiTietDeThi x) {
        this.maDe = x.maDe;
        this.maCH = x.maCH;
        this.diemToiDa = x.diemToiDa;
    }

    public ChiTietDeThi saoChep() {
        return new ChiTietDeThi(this);
    }

    @Override
    public String[] xuat() {
        return new String[]{maDe, maCH, String.format(Locale.US, "%.2f", diemToiDa)};
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        setMaDe(NhapChuan.docChuoiKhongRong("Ma de thi"));
        setMaCH(NhapChuan.docChuoiKhongRong("Ma cau hoi"));
        while (!setDiemToiDa(NhapChuan.docSoThuc("Diem toi da"))) {
            System.out.println("Diem toi da phai lon hon 0!");
        }
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        dos.writeUTF(maDe == null ? "" : maDe);
        dos.writeUTF(maCH == null ? "" : maCH);
        dos.writeDouble(diemToiDa);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        this.maDe = dis.readUTF();
        this.maCH = dis.readUTF();
        this.diemToiDa = dis.readDouble();
    }

    public String getMaDe() { return maDe; }
    public String getMaCH() { return maCH; }
    public double getDiemToiDa() { return diemToiDa; }

    public boolean setMaDe(String maDe) {
        if (maDe == null || maDe.trim().isEmpty()) return false;
        this.maDe = maDe.trim();
        return true;
    }

    public boolean setMaCH(String maCH) {
        if (maCH == null || maCH.trim().isEmpty()) return false;
        this.maCH = maCH.trim();
        return true;
    }

    public boolean setDiemToiDa(double diemToiDa) {
        if (diemToiDa <= 0 || Double.isNaN(diemToiDa)) return false;
        this.diemToiDa = diemToiDa;
        return true;
    }
}
