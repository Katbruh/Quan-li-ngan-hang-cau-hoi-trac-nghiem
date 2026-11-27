package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class CauHoiNhieuDapAn extends CauHoi {
    private String dapAnDung;

    public CauHoiNhieuDapAn() {
        super();
        this.dapAnDung = "";
    }

    public CauHoiNhieuDapAn(String maCH, String maMonHoc, String noiDung, String ndA, String ndB, String ndC, String ndD, String maGV, String dapAnDung) {
        super(maCH, maMonHoc, noiDung, ndA, ndB, ndC, ndD, maGV);
        if (!setDapAnDung(dapAnDung)) this.dapAnDung = "";
    }

    public CauHoiNhieuDapAn(CauHoiNhieuDapAn x) {
        super(x);
        this.dapAnDung = x.dapAnDung;
    }

    @Override
    public CauHoi saoChep() {
        return new CauHoiNhieuDapAn(this);
    }

    @Override
    public String[] xuat() {
        String[] goc = super.xuat();
        String[] kq = Arrays.copyOf(goc, goc.length + 2);
        kq[goc.length] = "Nhieu dap an";
        kq[goc.length + 1] = getDapAnChuan();
        return kq;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        while (!setDapAnDung(NhapChuan.docChuoiKhongRong("Dap an dung (VD: ABC)"))) {
            System.out.println("Chi duoc nhap cac ky tu A, B, C, D!");
        }
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        dos.writeUTF(dapAnDung == null ? "" : dapAnDung);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        this.dapAnDung = dis.readUTF();
    }

    public String getDapAnDung() { return dapAnDung; }
    @Override public int getLoaiCauHoi() { return 2; }
    @Override public String getDapAnChuan() { return dapAnDung; }

    public boolean setDapAnDung(String dapAnDung) {
        if (dapAnDung == null) return false;
        String s = dapAnDung.trim().toUpperCase();
        if (s.isEmpty()) return false;
        boolean[] co = new boolean[4];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 'A' || c > 'D') return false;
            co[c - 'A'] = true;
        }
        String kq = "";
        for (int i = 0; i < 4; i++) {
            if (co[i]) kq += (char) ('A' + i);
        }
        this.dapAnDung = kq;
        return true;
    }

    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (traLoi == null || dapAnDung == null || dapAnDung.isEmpty()) return 0;
        String s = traLoi.trim().toUpperCase();
        boolean[] daDem = new boolean[4];
        int soDung = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'A' && c <= 'D' && !daDem[c - 'A']) {
                daDem[c - 'A'] = true;
                if (dapAnDung.indexOf(c) >= 0) soDung++;
            }
        }
        return ((double) soDung / dapAnDung.length()) * diemToiDa;
    }
}
