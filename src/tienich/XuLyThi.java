package tienich;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import mohinh.CauHoi;
import mohinh.ChiTietDeThi;
import mohinh.ChiTietKetQua;
import mohinh.DeThi;
import mohinh.GiangVien;
import mohinh.KetQuaThi;
import mohinh.SinhVien;

public final class XuLyThi {
    private XuLyThi() {
    }

    // tao de thi moi va chon ngau nhien cau hoi khong trung
    public static void taoDeThi() {
        boolean laGV = QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien;
        String maGV = laGV ? QuanLyDanhSach.nguoiDungHienTai.getMa() : NhapChuan.docChuoiKhongRong("Ma giang vien");
        int loai = NhapChuan.docSoNguyenTrongKhoang("Loai cau hoi (1: Mot DA, 2: Nhieu DA, 3: Dung/Sai)", 1, 3);
        String tenDe = NhapChuan.docChuoiKhongRong("Ten de thi");
        String maMon = NhapChuan.docChuoiKhongRong("Ma mon hoc");
        CauHoi[] nguon = new CauHoi[0];
        for (int i = 0; i < QuanLyDanhSach.DS_CAUHOI.getSoLuong(); i++) {
            CauHoi ch = QuanLyDanhSach.DS_CAUHOI.docPhanTu(i);
            if (ch.getMaMonHoc().equalsIgnoreCase(maMon) && ch.getLoaiCauHoi() == loai
                    && (!laGV || ch.getMaGV().equalsIgnoreCase(maGV))) {
                nguon = Arrays.copyOf(nguon, nguon.length + 1);
                nguon[nguon.length - 1] = ch;
            }
        }
        if (nguon.length == 0) { System.out.println("Khong co cau hoi phu hop!"); return; }
        int thoiGian = NhapChuan.docSoNguyenTrongKhoang("Thoi gian lam bai (phut)", 1, 300);
        LocalDateTime ngayThi = NhapChuan.docNgayGio("Ngay thi");
        String maKyThi = NhapChuan.docChuoiKhongRong("Ma ky thi");
        String loaiDT;
        while (true) {
            loaiDT = NhapChuan.docChuoiKhongRong("Loai doi tuong (LOP/SINH_VIEN)").toUpperCase();
            if (loaiDT.equals("LOP") || loaiDT.equals("SINH_VIEN")) break;
            System.out.println("Chi nhap LOP hoac SINH_VIEN!");
        }
        String doiTuong = NhapChuan.docChuoiKhongRong("Doi tuong (Ten lop hoac DS ma SV)");
        int maxCau = Math.min(50, nguon.length);
        int soCau = NhapChuan.docSoNguyenTrongKhoang("So cau hoi (1.." + maxCau + ")", 1, maxCau);
        String maDe = QuanLyDanhSach.DS_DETHI.taoMaMoi();
        DeThi de = new DeThi(maDe, tenDe, maMon, loai, thoiGian, ngayThi, LocalDateTime.now(), maKyThi, loaiDT, doiTuong, maGV);
        QuanLyDanhSach.DS_DETHI.themVaoDanhSach(de);
        boolean[] daChon = new boolean[nguon.length];
        Random rand = new Random();
        double diemToiDa = 10.0 / soCau;
        int daLay = 0;
        while (daLay < soCau) {
            int idx = rand.nextInt(nguon.length);
            if (!daChon[idx]) {
                daChon[idx] = true;
                QuanLyDanhSach.DS_CHITIETDETHI.themVaoDanhSach(new ChiTietDeThi(maDe, nguon[idx].getMaCH(), diemToiDa));
                daLay++;
            }
        }
        QuanLyDanhSach.ghiDuLieuDanhSach();
        System.out.println("Da tao de thi " + maDe + " voi " + soCau + " cau hoi.");
    }

    // to chuc cho sinh vien lam bai thi va tu dong cham diem
    public static void lamBaiThi(SinhVien sv) {
        DeThi[] dsGiao = new DeThi[0];
        for (int i = 0; i < QuanLyDanhSach.DS_DETHI.getSoLuong(); i++) {
            DeThi de = QuanLyDanhSach.DS_DETHI.docPhanTu(i);
            boolean khop = (de.getLoaiDoiTuong().equalsIgnoreCase("LOP") && de.getDoiTuong().equalsIgnoreCase(sv.getLop()))
                    || (de.getLoaiDoiTuong().equalsIgnoreCase("SINH_VIEN") && coTrongDanhSach(de.getDoiTuong(), sv.getMaSV()));
            if (khop) {
                dsGiao = Arrays.copyOf(dsGiao, dsGiao.length + 1);
                dsGiao[dsGiao.length - 1] = de;
            }
        }
        if (dsGiao.length == 0) { System.out.println("Khong co de thi nao duoc giao!"); return; }
        System.out.println("\n=== DANH SACH DE THI DUOC GIAO ===");
        for (int i = 0; i < dsGiao.length; i++) {
            String tt = QuanLyDanhSach.DS_KETQUA.daThi(sv.getMaSV(), dsGiao[i].getMaDe()) ? "[DA THI]" : "[CHUA THI]";
            System.out.println(tt + " " + dsGiao[i].getMaDe() + " - " + dsGiao[i].getTenDe() + " (" + dsGiao[i].getThoiGian() + " phut)");
        }
        String maDe = NhapChuan.docChuoiKhongRong("Nhap ma de muon thi");
        DeThi deChon = null;
        for (int i = 0; i < dsGiao.length; i++) {
            if (dsGiao[i].getMaDe().equalsIgnoreCase(maDe)) { deChon = dsGiao[i]; break; }
        }
        if (deChon == null) { System.out.println("Ma de khong nam trong danh sach duoc giao!"); return; }
        if (QuanLyDanhSach.DS_KETQUA.daThi(sv.getMaSV(), deChon.getMaDe())) {
            System.out.println("Ban da thi de nay roi, khong duoc thi lai!");
            return;
        }
        ChiTietDeThi[] dsCT = QuanLyDanhSach.DS_CHITIETDETHI.layTheoMaDe(deChon.getMaDe());
        if (dsCT.length == 0) { System.out.println("De thi chua co cau hoi!"); return; }
        String maKQ = QuanLyDanhSach.DS_KETQUA.taoMaMoi();
        long batDau = System.currentTimeMillis();
        long gioiHanMs = deChon.getThoiGian() * 60000L;
        int soCauDung = 0;
        double tongDiem = 0;
        for (int i = 0; i < dsCT.length; i++) {
            CauHoi ch = QuanLyDanhSach.DS_CAUHOI.timKiemMa(dsCT[i].getMaCH());
            if (ch == null) continue;
            System.out.println("\n--- Cau " + (i + 1) + "/" + dsCT.length + " ---");
            ch.xuatChiTiet();
            String traLoi;
            if (ch.getLoaiCauHoi() == 1 || ch.getLoaiCauHoi() == 2) {
                traLoi = docDapAnChon(ch.getLoaiCauHoi());
            } else {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < 4; k++) sb.append(NhapChuan.docDungSai("Y " + (char) ('A' + k)));
                traLoi = sb.toString();
            }
            double diemDat = ch.chamDiem(traLoi, dsCT[i].getDiemToiDa());
            tongDiem += diemDat;
            if (Math.abs(diemDat - dsCT[i].getDiemToiDa()) < 1e-6) soCauDung++;
            QuanLyDanhSach.DS_CHITIETKETQUA.themVaoDanhSach(
                    new ChiTietKetQua(maKQ, ch.getMaCH(), traLoi, ch.getDapAnChuan(), diemDat));
            if ((System.currentTimeMillis() - batDau) >= gioiHanMs) {
                System.out.println("Da het thoi gian lam bai! He thong tu dong nop bai.");
                for (int j = i + 1; j < dsCT.length; j++) {
                    CauHoi con = QuanLyDanhSach.DS_CAUHOI.timKiemMa(dsCT[j].getMaCH());
                    QuanLyDanhSach.DS_CHITIETKETQUA.themVaoDanhSach(
                            new ChiTietKetQua(maKQ, dsCT[j].getMaCH(), "", con == null ? "" : con.getDapAnChuan(), 0));
                }
                break;
            }
        }
        long thoiGianGiay = (System.currentTimeMillis() - batDau) / 1000L;
        KetQuaThi kq = new KetQuaThi(maKQ, sv.getMaSV(), deChon.getMaDe(), LocalDateTime.now(), soCauDung, dsCT.length, tongDiem, thoiGianGiay);
        QuanLyDanhSach.DS_KETQUA.themVaoDanhSach(kq);
        QuanLyDanhSach.ghiDuLieuDanhSach();
        System.out.println("\n=== KET QUA BAI THI (" + maKQ + ") ===");
        System.out.println("So cau dung: " + soCauDung + "/" + dsCT.length);
        System.out.println("Tong diem: " + String.format(Locale.US, "%.2f", tongDiem));
        System.out.println("Thoi gian lam: " + thoiGianGiay + " giay");
    }

    public static void xemChiTietBaiLam(String maKQ) {
        ChiTietKetQua[] dsCT = QuanLyDanhSach.DS_CHITIETKETQUA.layTheoMaKQ(maKQ);
        if (dsCT.length == 0) { System.out.println("Khong tim thay chi tiet cho ma " + maKQ); return; }
        String[][] bang = new String[dsCT.length + 1][5];
        bang[0] = new String[]{"Ma KQ", "Ma cau hoi", "Tra loi", "Dap an dung", "Diem dat"};
        for (int i = 0; i < dsCT.length; i++) bang[i + 1] = dsCT[i].xuat();
        FormatBang.inBangDuLieu("CHI TIET BAI LAM " + maKQ, bang);
    }

    // loc ket qua theo ma de, mssv, tat ca hoac diem
    public static void xemKetQuaTheoBoLoc(String maGV, boolean laQTV) {
        int chon = NhapChuan.docSoNguyenTrongKhoang("Loc theo (1: Ma de, 2: MSSV, 3: Tat ca, 4: Diem)", 1, 4);
        String key = (chon == 1 || chon == 2) ? NhapChuan.docChuoiKhongRong("Nhap ma can loc") : "";
        String diemStr = chon == 4 ? String.format(Locale.US, "%.2f", NhapChuan.docSoThucKhongAm("Nhap diem can tim")) : "";
        String[][] bang = new String[1][7];
        bang[0] = new String[]{"Ma KQ", "Ma SV", "Ma de", "Ngay thi", "Dung/Tong", "Diem", "Thoi gian"};
        for (int i = 0; i < QuanLyDanhSach.DS_KETQUA.getSoLuong(); i++) {
            KetQuaThi kq = QuanLyDanhSach.DS_KETQUA.docPhanTu(i);
            if (!laQTV) {
                DeThi de = QuanLyDanhSach.DS_DETHI.timKiemMa(kq.getMaDe());
                if (de == null || !de.getMaGV().equalsIgnoreCase(maGV)) continue;
            }
            boolean khop = (chon == 3)
                    || (chon == 1 && kq.getMaDe().equalsIgnoreCase(key))
                    || (chon == 2 && kq.getMaSV().equalsIgnoreCase(key))
                    || (chon == 4 && String.format(Locale.US, "%.2f", kq.getDiem()).equals(diemStr));
            if (khop) {
                bang = Arrays.copyOf(bang, bang.length + 1);
                bang[bang.length - 1] = kq.xuat();
            }
        }
        FormatBang.inBangDuLieu("KET QUA THI", bang);
    }

    private static String docDapAnChon(int loai) {
        String prompt = loai == 1 ? "Chon dap an (A/B/C/D hoac Enter bo qua)" : "Chon cac dap an (VD: AB, Enter bo qua)";
        String mau = loai == 1 ? "[A-D]" : "[A-D]+";
        while (true) {
            String s = NhapChuan.docChuoi(prompt).toUpperCase();
            if (s.isEmpty() || s.matches(mau)) return s;
            System.out.println("Dap an khong hop le! Chi nhap A, B, C, D hoac Enter bo qua.");
        }
    }

    private static boolean coTrongDanhSach(String dsMa, String maSV) {
        String[] parts = dsMa.split("[,;]");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].trim().equalsIgnoreCase(maSV)) return true;
        }
        return false;
    }
}
