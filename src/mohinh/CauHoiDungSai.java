package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class CauHoiDungSai extends CauHoi {
    private boolean[] dapAnDung;

    public CauHoiDungSai() {
        super();
        this.dapAnDung = new boolean[4];
    }

    public CauHoiDungSai(String maCH, String maMonHoc, String noiDung, String ndA, String ndB, String ndC, String ndD, String maGV, boolean[] dapAnDung) {
        super(maCH, maMonHoc, noiDung, ndA, ndB, ndC, ndD, maGV);
        if (!setDapAnDung(dapAnDung)) this.dapAnDung = new boolean[4];
    }

    public CauHoiDungSai(CauHoiDungSai x) {
        super(x);
        this.dapAnDung = Arrays.copyOf(x.dapAnDung, 4);
    }

    @Override
    public CauHoi saoChep() {
        return new CauHoiDungSai(this);
    }

    @Override
    public String[] xuat() {
        String[] coBan = super.xuat();
        String[] ketQua = Arrays.copyOf(coBan, coBan.length + 2);
        ketQua[coBan.length] = "Dung/Sai";
        ketQua[coBan.length + 1] = getDapAnChuan();
        return ketQua;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        boolean[] mang = new boolean[4];
        for (int i = 0; i < 4; i++) {
            while (true) {
                String s = NhapChuan.docChuoi("Y " + (char) ('A' + i) + " (D/S)").toUpperCase();
                if (s.equals("D")) {
                    mang[i] = true;
                    break;
                }
                if (s.equals("S")) {
                    mang[i] = false;
                    break;
                }
                System.out.println("Chi nhap D hoac S!");
            }
        }
        setDapAnDung(mang);
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        for (int i = 0; i < 4; i++) {
            dos.writeBoolean(dapAnDung[i]);
        }
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        for (int i = 0; i < 4; i++) {
            this.dapAnDung[i] = dis.readBoolean();
        }
    }

    public boolean[] getDapAnDung() { return Arrays.copyOf(dapAnDung, 4); }

    @Override
    public int getLoaiCauHoi() { return 3; }

    @Override
    public String getDapAnChuan() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(dapAnDung[i] ? 'D' : 'S');
        }
        return sb.toString();
    }

    public boolean setDapAnDung(boolean[] dapAnDung) {
        if (dapAnDung == null || dapAnDung.length != 4) return false;
        this.dapAnDung = Arrays.copyOf(dapAnDung, 4);
        return true;
    }

    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (traLoi == null) return 0;
        String chuan = traLoi.trim().toUpperCase();
        int soKhop = 0;
        for (int i = 0; i < 4; i++) {
            char c = i < chuan.length() ? chuan.charAt(i) : '-';
            if ((c == 'D' && dapAnDung[i]) || (c == 'S' && !dapAnDung[i])) {
                soKhop++;
            }
        }
        // so y khop: 1 -> 0.1, 2 -> 0.25, 3 -> 0.5, 4 -> 1.0
        if (soKhop == 1) return 0.1 * diemToiDa;
        if (soKhop == 2) return 0.25 * diemToiDa;
        if (soKhop == 3) return 0.5 * diemToiDa;
        if (soKhop == 4) return 1.0 * diemToiDa;
        return 0;
    }
}
