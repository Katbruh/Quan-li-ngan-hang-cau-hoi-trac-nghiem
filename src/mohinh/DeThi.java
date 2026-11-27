package mohinh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import rangbuoc.ITruyXuatMoHinh;
import tienich.NhapChuan;

public class DeThi implements ITruyXuatMoHinh {
    private String maDe;
    private String tenDe;
    private String maMonHoc;
    private int loaiCauHoi;
    private int thoiGian;
    private LocalDateTime ngayThi;
    private LocalDateTime ngayTao;
    private String maKyThi;
    private String loaiDoiTuong;
    private String doiTuong;
    private String maGV;

    public DeThi() {
        this.maDe = "";
        this.tenDe = "";
        this.maMonHoc = "";
        this.loaiCauHoi = 1;
        this.thoiGian = 1;
        this.ngayThi = LocalDateTime.now();
        this.ngayTao = LocalDateTime.now();
        this.maKyThi = "";
        this.loaiDoiTuong = "LOP";
        this.doiTuong = "";
        this.maGV = "";
    }

    public DeThi(String maDe, String tenDe, String maMonHoc, int loaiCauHoi, int thoiGian, LocalDateTime ngayThi, String maKyThi, String loaiDoiTuong, String doiTuong, String maGV) {
        this();
        setMaDe(maDe);
        setTenDe(tenDe);
        setMaMonHoc(maMonHoc);
        setLoaiCauHoi(loaiCauHoi);
        setThoiGian(thoiGian);
        setNgayThi(ngayThi);
        setMaKyThi(maKyThi);
        setLoaiDoiTuong(loaiDoiTuong);
        setDoiTuong(doiTuong);
        setMaGV(maGV);
    }

    public DeThi(DeThi x) {
        this.maDe = x.maDe;
        this.tenDe = x.tenDe;
        this.maMonHoc = x.maMonHoc;
        this.loaiCauHoi = x.loaiCauHoi;
        this.thoiGian = x.thoiGian;
        this.ngayThi = x.ngayThi;
        this.ngayTao = x.ngayTao;
        this.maKyThi = x.maKyThi;
        this.loaiDoiTuong = x.loaiDoiTuong;
        this.doiTuong = x.doiTuong;
        this.maGV = x.maGV;
    }

    public DeThi saoChep() {
        return new DeThi(this);
    }

    @Override
    public String[] xuat() {
        return new String[]{maDe, tenDe, maMonHoc, String.valueOf(loaiCauHoi), String.valueOf(thoiGian), ngayThi.format(NhapChuan.DINH_DANG), maKyThi, loaiDoiTuong, doiTuong, maGV};
    }

    @Override
    public ITruyXuatMoHinh nhap() {
        setTenDe(NhapChuan.docChuoiKhongRong("Ten de"));
        setMaMonHoc(NhapChuan.docChuoiKhongRong("Ma mon hoc"));
        setLoaiCauHoi(NhapChuan.docSoNguyenTrongKhoang("Loai cau hoi (1/2/3)", 1, 3));
        while (!setThoiGian(NhapChuan.docSoNguyen("Thoi gian (phut)"))) {
            System.out.println("Thoi gian phai lon hon 0!");
        }
        setNgayThi(NhapChuan.docNgayGio("Ngay thi"));
        setMaKyThi(NhapChuan.docChuoiKhongRong("Ma ky thi"));
        while (!setLoaiDoiTuong(NhapChuan.docChuoiKhongRong("Loai doi tuong (LOP/SINH_VIEN)"))) {
            System.out.println("Chi nhap LOP hoac SINH_VIEN!");
        }
        setDoiTuong(NhapChuan.docChuoiKhongRong("Doi tuong"));
        return this;
    }

    @Override
    public void ghiFile(DataOutputStream dos) throws IOException {
        dos.writeUTF(maDe == null ? "" : maDe);
        dos.writeUTF(tenDe == null ? "" : tenDe);
        dos.writeUTF(maMonHoc == null ? "" : maMonHoc);
        dos.writeInt(loaiCauHoi);
        dos.writeInt(thoiGian);
        dos.writeUTF(ngayThi.format(NhapChuan.DINH_DANG));
        dos.writeUTF(ngayTao.format(NhapChuan.DINH_DANG));
        dos.writeUTF(maKyThi == null ? "" : maKyThi);
        dos.writeUTF(loaiDoiTuong == null ? "" : loaiDoiTuong);
        dos.writeUTF(doiTuong == null ? "" : doiTuong);
        dos.writeUTF(maGV == null ? "" : maGV);
    }

    @Override
    public void docFile(DataInputStream dis) throws IOException {
        this.maDe = dis.readUTF();
        this.tenDe = dis.readUTF();
        this.maMonHoc = dis.readUTF();
        this.loaiCauHoi = dis.readInt();
        this.thoiGian = dis.readInt();
        this.ngayThi = LocalDateTime.parse(dis.readUTF(), NhapChuan.DINH_DANG);
        this.ngayTao = LocalDateTime.parse(dis.readUTF(), NhapChuan.DINH_DANG);
        this.maKyThi = dis.readUTF();
        this.loaiDoiTuong = dis.readUTF();
        this.doiTuong = dis.readUTF();
        this.maGV = dis.readUTF();
    }

    public String getMaDe() { return maDe; }
    public String getTenDe() { return tenDe; }
    public String getMaMonHoc() { return maMonHoc; }
    public int getLoaiCauHoi() { return loaiCauHoi; }
    public int getThoiGian() { return thoiGian; }
    public LocalDateTime getNgayThi() { return ngayThi; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public String getMaKyThi() { return maKyThi; }
    public String getLoaiDoiTuong() { return loaiDoiTuong; }
    public String getDoiTuong() { return doiTuong; }
    public String getMaGV() { return maGV; }

    public boolean setMaDe(String maDe) {
        if (maDe == null || maDe.trim().isEmpty()) return false;
        this.maDe = maDe.trim();
        return true;
    }

    public boolean setTenDe(String tenDe) {
        if (tenDe == null || tenDe.trim().isEmpty()) return false;
        this.tenDe = tenDe.trim();
        return true;
    }

    public boolean setMaMonHoc(String maMonHoc) {
        if (maMonHoc == null || maMonHoc.trim().isEmpty()) return false;
        this.maMonHoc = maMonHoc.trim();
        return true;
    }

    public boolean setLoaiCauHoi(int loaiCauHoi) {
        if (loaiCauHoi < 1 || loaiCauHoi > 3) return false;
        this.loaiCauHoi = loaiCauHoi;
        return true;
    }

    public boolean setThoiGian(int thoiGian) {
        if (thoiGian <= 0) return false;
        this.thoiGian = thoiGian;
        return true;
    }

    public boolean setNgayThi(LocalDateTime ngayThi) {
        if (ngayThi == null) return false;
        this.ngayThi = ngayThi;
        return true;
    }

    public boolean setNgayTao(LocalDateTime ngayTao) {
        if (ngayTao == null) return false;
        this.ngayTao = ngayTao;
        return true;
    }
    public boolean setMaKyThi(String maKyThi) {
        if (maKyThi == null || maKyThi.trim().isEmpty()) return false;
        this.maKyThi = maKyThi.trim();
        return true;
    }

    public boolean setLoaiDoiTuong(String loaiDoiTuong) {
        if (loaiDoiTuong == null) return false;
        String s = loaiDoiTuong.trim().toUpperCase();
        if (!s.equals("LOP") && !s.equals("SINH_VIEN")) return false;
        this.loaiDoiTuong = s;
        return true;
    }

    public boolean setDoiTuong(String doiTuong) {
        if (doiTuong == null || doiTuong.trim().isEmpty()) return false;
        this.doiTuong = doiTuong.trim();
        return true;
    }

    public boolean setMaGV(String maGV) {
        if (maGV == null || maGV.trim().isEmpty()) return false;
        this.maGV = maGV.trim();
        return true;
    }
}
