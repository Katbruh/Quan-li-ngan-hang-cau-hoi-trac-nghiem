package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public abstract class CauHoi implements ITruyXuatMoHinh {
    protected String maCH;
    protected String maMonHoc;
    protected String noiDung;
    protected String ndA;
    protected String ndB;
    protected String ndC;
    protected String ndD;
    protected String maGV;

    public CauHoi() {
        this.maCH = "";
        this.maMonHoc = "";
        this.noiDung = "";
        this.ndA = "";
        this.ndB = "";
        this.ndC = "";
        this.ndD = "";
        this.maGV = "";
    }

    public CauHoi(String maCH, String maMonHoc, String noiDung, String ndA, String ndB, String ndC, String ndD, String maGV) {
        if (!setMaCH(maCH)) this.maCH = "";
        if (!setMaMonHoc(maMonHoc)) this.maMonHoc = "";
        if (!setNoiDung(noiDung)) this.noiDung = "";
        if (!setNdA(ndA)) this.ndA = "";
        if (!setNdB(ndB)) this.ndB = "";
        if (!setNdC(ndC)) this.ndC = "";
        if (!setNdD(ndD)) this.ndD = "";
        if (!setMaGV(maGV)) this.maGV = "";
    }

    public CauHoi(CauHoi x) {
        this.maCH = x.maCH;
        this.maMonHoc = x.maMonHoc;
        this.noiDung = x.noiDung;
        this.ndA = x.ndA;
        this.ndB = x.ndB;
        this.ndC = x.ndC;
        this.ndD = x.ndD;
        this.maGV = x.maGV;
    }

    public abstract CauHoi saoChep();

    @Override
    public String[] xuat() {
        return new String[]{maCH, maMonHoc, catChuoi(noiDung, 35), maGV};
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        setMaMonHoc(NhapChuan.docChuoiKhongRong("Ma mon hoc"));
        setNoiDung(NhapChuan.docChuoiKhongRong("Noi dung cau hoi"));
        setNdA(NhapChuan.docChuoiKhongRong("Noi dung A"));
        setNdB(NhapChuan.docChuoiKhongRong("Noi dung B"));
        setNdC(NhapChuan.docChuoiKhongRong("Noi dung C"));
        setNdD(NhapChuan.docChuoiKhongRong("Noi dung D"));
        return this;
    }

    public void xuatChiTiet() {
        System.out.println("[" + maCH + "] (" + maMonHoc + ") " + noiDung);
        System.out.println("  A. " + ndA);
        System.out.println("  B. " + ndB);
        System.out.println("  C. " + ndC);
        System.out.println("  D. " + ndD);
    }

    private String catChuoi(String s, int maxLen) {
        if (s == null) return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 3) + "...";
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        dos.writeUTF(maCH == null ? "" : maCH);
        dos.writeUTF(maMonHoc == null ? "" : maMonHoc);
        dos.writeUTF(noiDung == null ? "" : noiDung);
        dos.writeUTF(ndA == null ? "" : ndA);
        dos.writeUTF(ndB == null ? "" : ndB);
        dos.writeUTF(ndC == null ? "" : ndC);
        dos.writeUTF(ndD == null ? "" : ndD);
        dos.writeUTF(maGV == null ? "" : maGV);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        this.maCH = dis.readUTF();
        this.maMonHoc = dis.readUTF();
        this.noiDung = dis.readUTF();
        this.ndA = dis.readUTF();
        this.ndB = dis.readUTF();
        this.ndC = dis.readUTF();
        this.ndD = dis.readUTF();
        this.maGV = dis.readUTF();
    }

    public String getMaCH() { return maCH; }
    public String getMaMonHoc() { return maMonHoc; }
    public String getNoiDung() { return noiDung; }
    public String getNdA() { return ndA; }
    public String getNdB() { return ndB; }
    public String getNdC() { return ndC; }
    public String getNdD() { return ndD; }
    public String getMaGV() { return maGV; }

    public boolean setMaCH(String maCH) {
        if (maCH == null || maCH.trim().isEmpty()) return false;
        this.maCH = maCH.trim();
        return true;
    }

    public boolean setMaMonHoc(String maMonHoc) {
        if (maMonHoc == null || maMonHoc.trim().isEmpty()) return false;
        this.maMonHoc = maMonHoc.trim();
        return true;
    }

    public boolean setNoiDung(String noiDung) {
        if (noiDung == null || noiDung.trim().isEmpty()) return false;
        this.noiDung = noiDung.trim();
        return true;
    }

    public boolean setNdA(String ndA) {
        if (ndA == null || ndA.trim().isEmpty()) return false;
        this.ndA = ndA.trim();
        return true;
    }

    public boolean setNdB(String ndB) {
        if (ndB == null || ndB.trim().isEmpty()) return false;
        this.ndB = ndB.trim();
        return true;
    }

    public boolean setNdC(String ndC) {
        if (ndC == null || ndC.trim().isEmpty()) return false;
        this.ndC = ndC.trim();
        return true;
    }

    public boolean setNdD(String ndD) {
        if (ndD == null || ndD.trim().isEmpty()) return false;
        this.ndD = ndD.trim();
        return true;
    }

    public boolean setMaGV(String maGV) {
        if (maGV == null || maGV.trim().isEmpty()) return false;
        this.maGV = maGV.trim();
        return true;
    }

    public abstract int getLoaiCauHoi();
    public abstract String getDapAnChuan();
    public abstract double chamDiem(String traLoi, double diemToiDa);
}
