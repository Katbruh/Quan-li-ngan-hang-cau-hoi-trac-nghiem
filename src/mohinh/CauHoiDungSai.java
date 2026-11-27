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
        this.dapAnDung = x.dapAnDung == null ? new boolean[4] : Arrays.copyOf(x.dapAnDung, 4);
    }

    @Override
    public CauHoi saoChep() {
        return new CauHoiDungSai(this);
    }

    @Override
    public String[] xuat() {
        String[] goc = super.xuat();
        String[] kq = Arrays.copyOf(goc, goc.length + 2);
        kq[goc.length] = "Dung/Sai";
        kq[goc.length + 1] = getDapAnChuan();
        return kq;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        boolean[] ds = new boolean[4];
        for (int i = 0; i < 4; i++) {
            char c;
            do {
                c = NhapChuan.docDungSai("Dap an y " + (char) ('A' + i));
            } while (c == '-');
            ds[i] = (c == 'D');
        }
        setDapAnDung(ds);
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
        this.dapAnDung = new boolean[4];
        for (int i = 0; i < 4; i++) {
            this.dapAnDung[i] = dis.readBoolean();
        }
    }

    public boolean[] getDapAnDung() { return Arrays.copyOf(dapAnDung, 4); }
    @Override public int getLoaiCauHoi() { return 3; }

    @Override
    public String getDapAnChuan() {
        String kq = "";
        for (int i = 0; i < 4; i++) {
            kq += dapAnDung[i] ? 'D' : 'S';
        }
        return kq;
    }

    public boolean setDapAnDung(boolean[] dapAnDung) {
        if (dapAnDung == null || dapAnDung.length != 4) return false;
        this.dapAnDung = Arrays.copyOf(dapAnDung, 4);
        return true;
    }

    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (traLoi == null) return 0;
        String s = traLoi.trim().toUpperCase();
        int soKhop = 0;
        int n = Math.min(4, s.length());
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if ((c == 'D' && dapAnDung[i]) || (c == 'S' && !dapAnDung[i])) soKhop++;
        }
        // cham diem theo so y khop: 1 y 10%, 2 y 25%, 3 y 50%, 4 y 100%
        switch (soKhop) {
            case 1: return 0.1 * diemToiDa;
            case 2: return 0.25 * diemToiDa;
            case 3: return 0.5 * diemToiDa;
            case 4: return diemToiDa;
            default: return 0;
        }
    }
}
