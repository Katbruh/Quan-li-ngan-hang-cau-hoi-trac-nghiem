package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class CauHoiMotDapAn extends CauHoi {
    private char dapAnDung;

    public CauHoiMotDapAn() {
        super();
        this.dapAnDung = 'A';
    }

    public CauHoiMotDapAn(String maCH, String maMonHoc, String noiDung, String ndA, String ndB, String ndC, String ndD, String maGV, char dapAnDung) {
        super(maCH, maMonHoc, noiDung, ndA, ndB, ndC, ndD, maGV);
        if (!setDapAnDung(dapAnDung)) this.dapAnDung = 'A';
    }

    public CauHoiMotDapAn(CauHoiMotDapAn x) {
        super(x);
        this.dapAnDung = x.dapAnDung;
    }

    @Override
    public CauHoi saoChep() {
        return new CauHoiMotDapAn(this);
    }

    @Override
    public String[] xuat() {
        String[] coBan = super.xuat();
        String[] ketQua = Arrays.copyOf(coBan, coBan.length + 2);
        ketQua[coBan.length] = "Mot dap an";
        ketQua[coBan.length + 1] = getDapAnChuan();
        return ketQua;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        while (true) {
            String s = NhapChuan.docChuoi("Dap an dung (A/B/C/D)").toUpperCase();
            if (s.length() == 1 && setDapAnDung(s.charAt(0))) {
                break;
            }
            System.out.println("Vui long nhap A, B, C hoac D!");
        }
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        dos.writeChar(dapAnDung);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        this.dapAnDung = dis.readChar();
    }

    public char getDapAnDung() { return dapAnDung; }

    @Override
    public int getLoaiCauHoi() { return 1; }

    @Override
    public String getDapAnChuan() { return String.valueOf(dapAnDung); }

    public boolean setDapAnDung(char dapAnDung) {
        char chuan = Character.toUpperCase(dapAnDung);
        if (chuan < 'A' || chuan > 'D') return false;
        this.dapAnDung = chuan;
        return true;
    }

    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (traLoi == null) return 0;
        String chuan = traLoi.trim().toUpperCase();
        if (chuan.equals(String.valueOf(dapAnDung))) return diemToiDa;
        return 0;
    }
}
