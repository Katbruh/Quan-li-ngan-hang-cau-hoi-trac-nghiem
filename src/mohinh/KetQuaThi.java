package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class KetQuaThi implements ITruyXuatMoHinh {
    private String maKQ;
    private String maSV;
    private String maDe;
    private LocalDateTime ngayThi;
    private int soCauDung;
    private int tongSoCau;
    private double diem;
    private long thoiGianGiay;

    public KetQuaThi() {
        this.maKQ = "";
        this.maSV = "";
        this.maDe = "";
        this.ngayThi = LocalDateTime.now();
        this.soCauDung = 0;
        this.tongSoCau = 1;
        this.diem = 0;
        this.thoiGianGiay = 0;
    }

    public KetQuaThi(String maKQ, String maSV, String maDe, LocalDateTime ngayThi, int soCauDung, int tongSoCau, double diem, long thoiGianGiay) {
        this();
        if (!setMaKQ(maKQ)) this.maKQ = "";
        if (!setMaSV(maSV)) this.maSV = "";
        if (!setMaDe(maDe)) this.maDe = "";
        if (!setNgayThi(ngayThi)) this.ngayThi = LocalDateTime.now();
        if (!setSoCauDung(soCauDung)) this.soCauDung = 0;
        if (!setTongSoCau(tongSoCau)) this.tongSoCau = 1;
        if (!setDiem(diem)) this.diem = 0;
        if (!setThoiGianGiay(thoiGianGiay)) this.thoiGianGiay = 0;
    }

    public KetQuaThi(KetQuaThi x) {
        this.maKQ = x.maKQ;
        this.maSV = x.maSV;
        this.maDe = x.maDe;
        this.ngayThi = x.ngayThi;
        this.soCauDung = x.soCauDung;
        this.tongSoCau = x.tongSoCau;
        this.diem = x.diem;
        this.thoiGianGiay = x.thoiGianGiay;
    }

    public KetQuaThi saoChep() {
        return new KetQuaThi(this);
    }

    @Override
    public String[] xuat() {
        return new String[]{maKQ, maSV, maDe, ngayThi.format(NhapChuan.DINH_DANG), String.valueOf(soCauDung), String.valueOf(tongSoCau), String.format("%.2f", diem), String.valueOf(thoiGianGiay)};
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        setMaSV(NhapChuan.docChuoiKhongRong("Ma sinh vien"));
        setMaDe(NhapChuan.docChuoiKhongRong("Ma de"));
        setNgayThi(NhapChuan.docNgayGio("Ngay thi"));
        setSoCauDung(NhapChuan.docSoNguyenKhongAm("So cau dung"));
        while (!setTongSoCau(NhapChuan.docSoNguyen("Tong so cau"))) {
            System.out.println("Tong so cau phai lon hon 0!");
        }
        while (!setDiem(NhapChuan.docSoThuc("Diem"))) {
            System.out.println("Diem phai lon hon hoac bang 0!");
        }
        setThoiGianGiay(NhapChuan.docSoNguyenKhongAm("Thoi gian giay"));
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        dos.writeUTF(maKQ == null ? "" : maKQ);
        dos.writeUTF(maSV == null ? "" : maSV);
        dos.writeUTF(maDe == null ? "" : maDe);
        dos.writeUTF(ngayThi.format(NhapChuan.DINH_DANG));
        dos.writeInt(soCauDung);
        dos.writeInt(tongSoCau);
        dos.writeDouble(diem);
        dos.writeLong(thoiGianGiay);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        this.maKQ = dis.readUTF();
        this.maSV = dis.readUTF();
        this.maDe = dis.readUTF();
        this.ngayThi = LocalDateTime.parse(dis.readUTF(), NhapChuan.DINH_DANG);
        this.soCauDung = dis.readInt();
        this.tongSoCau = dis.readInt();
        this.diem = dis.readDouble();
        this.thoiGianGiay = dis.readLong();
    }

    public String getMaKQ() { return maKQ; }
    public String getMaSV() { return maSV; }
    public String getMaDe() { return maDe; }
    public LocalDateTime getNgayThi() { return ngayThi; }
    public int getSoCauDung() { return soCauDung; }
    public int getTongSoCau() { return tongSoCau; }
    public double getDiem() { return diem; }
    public long getThoiGianGiay() { return thoiGianGiay; }

    public boolean setMaKQ(String maKQ) {
        if (maKQ == null || maKQ.trim().isEmpty()) return false;
        this.maKQ = maKQ.trim();
        return true;
    }

    public boolean setMaSV(String maSV) {
        if (maSV == null || maSV.trim().isEmpty()) return false;
        this.maSV = maSV.trim();
        return true;
    }

    public boolean setMaDe(String maDe) {
        if (maDe == null || maDe.trim().isEmpty()) return false;
        this.maDe = maDe.trim();
        return true;
    }

    public boolean setNgayThi(LocalDateTime ngayThi) {
        if (ngayThi == null) return false;
        this.ngayThi = ngayThi;
        return true;
    }

    public boolean setSoCauDung(int soCauDung) {
        if (soCauDung < 0) return false;
        this.soCauDung = soCauDung;
        return true;
    }

    public boolean setTongSoCau(int tongSoCau) {
        if (tongSoCau <= 0) return false;
        this.tongSoCau = tongSoCau;
        return true;
    }

    public boolean setDiem(double diem) {
        if (diem < 0 || Double.isNaN(diem)) return false;
        this.diem = diem;
        return true;
    }

    public boolean setThoiGianGiay(long thoiGianGiay) {
        if (thoiGianGiay < 0) return false;
        this.thoiGianGiay = thoiGianGiay;
        return true;
    }
}
