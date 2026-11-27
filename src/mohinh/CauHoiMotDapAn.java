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
        String[] goc = super.xuat();
        String[] kq = Arrays.copyOf(goc, goc.length + 2);
        kq[goc.length] = "Mot dap an";
        kq[goc.length + 1] = getDapAnChuan();
        return kq;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        while (true) {
            String s = NhapChuan.docChuoiKhongRong("Dap an dung (A/B/C/D)");
            if (s.length() == 1 && setDapAnDung(s.charAt(0))) break;
            System.out.println("Chi duoc nhap 1 ky tu A, B, C hoac D!");
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
    @Override public int getLoaiCauHoi() { return 1; }
    @Override public String getDapAnChuan() { return String.valueOf(dapAnDung); }

    public boolean setDapAnDung(char dapAnDung) {
        char c = Character.toUpperCase(dapAnDung);
        if (c < 'A' || c > 'D') return false;
        this.dapAnDung = c;
        return true;
    }

    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (traLoi == null) return 0;
        String s = traLoi.trim().toUpperCase();
        return s.equals(String.valueOf(dapAnDung)) ? diemToiDa : 0;
    }
}
