package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class ChiTietKetQua implements ITruyXuatMoHinh {
    private String maKQ;
    private String maCH;
    private String traLoi;
    private String dapAnDung;
    private double diemDatDuoc;

    public ChiTietKetQua() {
        this.maKQ = "";
        this.maCH = "";
        this.traLoi = "";
        this.dapAnDung = "";
        this.diemDatDuoc = 0;
    }

    public ChiTietKetQua(String maKQ, String maCH, String traLoi, String dapAnDung, double diemDatDuoc) {
        this();
        setMaKQ(maKQ);
        setMaCH(maCH);
        setTraLoi(traLoi);
        setDapAnDung(dapAnDung);
        setDiemDatDuoc(diemDatDuoc);
    }

    public ChiTietKetQua(ChiTietKetQua x) {
        this.maKQ = x.maKQ;
        this.maCH = x.maCH;
        this.traLoi = x.traLoi;
        this.dapAnDung = x.dapAnDung;
        this.diemDatDuoc = x.diemDatDuoc;
    }

    public ChiTietKetQua saoChep() {
        return new ChiTietKetQua(this);
    }

    @Override
    public String[] xuat() {
        return new String[]{maKQ, maCH, traLoi, dapAnDung, String.format("%.2f", diemDatDuoc)};
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        setMaKQ(NhapChuan.docChuoiKhongRong("Ma ket qua"));
        setMaCH(NhapChuan.docChuoiKhongRong("Ma cau hoi"));
        setTraLoi(NhapChuan.docChuoi("Tra loi"));
        setDapAnDung(NhapChuan.docChuoiKhongRong("Dap an dung"));
        while (!setDiemDatDuoc(NhapChuan.docSoThuc("Diem dat duoc"))) {
            System.out.println("Diem dat duoc phai lon hon hoac bang 0!");
        }
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        dos.writeUTF(maKQ == null ? "" : maKQ);
        dos.writeUTF(maCH == null ? "" : maCH);
        dos.writeUTF(traLoi == null ? "" : traLoi);
        dos.writeUTF(dapAnDung == null ? "" : dapAnDung);
        dos.writeDouble(diemDatDuoc);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        this.maKQ = dis.readUTF();
        this.maCH = dis.readUTF();
        this.traLoi = dis.readUTF();
        this.dapAnDung = dis.readUTF();
        this.diemDatDuoc = dis.readDouble();
    }

    public String getMaKQ() { return maKQ; }
    public String getMaCH() { return maCH; }
    public String getTraLoi() { return traLoi; }
    public String getDapAnDung() { return dapAnDung; }
    public double getDiemDatDuoc() { return diemDatDuoc; }

    public boolean setMaKQ(String maKQ) {
        if (maKQ == null || maKQ.trim().isEmpty()) return false;
        this.maKQ = maKQ.trim();
        return true;
    }

    public boolean setMaCH(String maCH) {
        if (maCH == null || maCH.trim().isEmpty()) return false;
        this.maCH = maCH.trim();
        return true;
    }

    public boolean setTraLoi(String traLoi) {
        if (traLoi == null) return false;
        this.traLoi = traLoi.trim();
        return true;
    }

    public boolean setDapAnDung(String dapAnDung) {
        if (dapAnDung == null || dapAnDung.trim().isEmpty()) return false;
        this.dapAnDung = dapAnDung.trim();
        return true;
    }

    public boolean setDiemDatDuoc(double diemDatDuoc) {
        if (diemDatDuoc < 0 || Double.isNaN(diemDatDuoc)) return false;
        this.diemDatDuoc = diemDatDuoc;
        return true;
    }
}
