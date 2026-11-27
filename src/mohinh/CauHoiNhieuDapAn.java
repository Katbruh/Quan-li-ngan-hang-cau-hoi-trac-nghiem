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
        this.dapAnDung = "A";
    }

    public CauHoiNhieuDapAn(String maCH, String maMonHoc, String noiDung, String ndA, String ndB, String ndC, String ndD, String maGV, String dapAnDung) {
        super(maCH, maMonHoc, noiDung, ndA, ndB, ndC, ndD, maGV);
        if (!setDapAnDung(dapAnDung)) this.dapAnDung = "A";
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
        String[] coBan = super.xuat();
        String[] ketQua = Arrays.copyOf(coBan, coBan.length + 2);
        ketQua[coBan.length] = "Nhieu dap an";
        ketQua[coBan.length + 1] = getDapAnChuan();
        return ketQua;
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        super.nhap();
        while (true) {
            String s = NhapChuan.docChuoi("Dap an dung (vd AC, ABC)");
            if (setDapAnDung(s)) {
                break;
            }
            System.out.println("Vui long nhap cac chu cai A, B, C, D!");
        }
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        super.ghiFile(dos);
        dos.writeUTF(dapAnDung);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        super.docFile(dis);
        this.dapAnDung = dis.readUTF();
    }

    public String getDapAnDung() { return dapAnDung; }

    @Override
    public int getLoaiCauHoi() { return 2; }

    @Override
    public String getDapAnChuan() { return dapAnDung; }

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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (co[i]) sb.append((char) ('A' + i));
        }
        this.dapAnDung = sb.toString();
        return true;
    }

    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (traLoi == null || dapAnDung == null || dapAnDung.isEmpty()) return 0;
        String chuan = traLoi.trim().toUpperCase();
        int soDung = 0;
        for (int i = 0; i < dapAnDung.length(); i++) {
            if (chuan.indexOf(dapAnDung.charAt(i)) >= 0) soDung++;
        }
        return ((double) soDung / dapAnDung.length()) * diemToiDa;
    }
}
