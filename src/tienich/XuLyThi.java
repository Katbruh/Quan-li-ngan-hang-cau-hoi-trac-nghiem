package tienich;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import mohinh.*;

public final class XuLyThi {
    private XuLyThi() {
    }

    // tao de thi ngau nhien khong trung cau hoi
    public static void taoDeThi() {
        String maGV;
        boolean laGV = QuanLyDanhSach.nguoiDungHienTai instanceof GiangVien;
        if (laGV) maGV = ((GiangVien) QuanLyDanhSach.nguoiDungHienTai).getMa();
        else if (QuanLyDanhSach.nguoiDungHienTai instanceof QuanTriVien) maGV = NhapChuan.docChuoiKhongRong("Ma giang vien");
        else {
            System.out.println("Ban khong co quyen tao de thi!");
            return;
        }
        int loai = NhapChuan.docSoNguyenTrongKhoang("Loai cau hoi (1=Mot DA, 2=Nhieu DA, 3=Dung/Sai)", 1, 3);
        String tenDe = NhapChuan.docChuoiKhongRong("Ten de thi");
        String maMon = NhapChuan.docChuoiKhongRong("Ma mon hoc");
        int thoiGian = NhapChuan.docSoNguyenTrongKhoang("Thoi gian lam bai (phut)", 1, 300);
        LocalDateTime ngayThi = NhapChuan.docNgayGio("Ngay thi");
        String maKyThi = NhapChuan.docChuoiKhongRong("Ma ky thi");
        String loaiDT = docLoaiDoiTuong();
        String doiTuong = NhapChuan.docChuoiKhongRong("Doi tuong (Ten lop hoac danh sach ma SV)");
        CauHoi[] nguon = locCauHoiPhuHop(maMon, loai, laGV ? maGV : null);
        if (nguon.length == 0) {
            System.out.println("Khong co cau hoi phu hop trong ngan hang!");
            return;
        }
        int soCau = NhapChuan.docSoNguyenTrongKhoang("So cau hoi (1-50)", 1, 50);
        if (soCau > nguon.length) {
            System.out.println("Khong du cau hoi! Chi co " + nguon.length + " cau phu hop.");
            return;
        }
        String maDe = QuanLyDanhSach.DS_DETHI.taoMaMoi();
        QuanLyDanhSach.DS_DETHI.themVaoDanhSach(new DeThi(maDe, tenDe, maMon, loai, thoiGian, ngayThi, maKyThi, loaiDT, doiTuong, maGV));
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
        System.out.println("Da tao de thi " + maDe + " gom " + soCau + " cau.");
    }

    // xu ly sinh vien lam bai thi va tu dong cham diem
    public static void lamBaiThi(SinhVien sv) {
        int tongDe = QuanLyDanhSach.DS_DETHI.getSoLuong();
        int dem = 0;
        System.out.println("\n=== DANH SACH DE THI DUOC GIAO ===");
        for (int i = 0; i < tongDe; i++) {
            DeThi de = QuanLyDanhSach.DS_DETHI.docPhanTu(i);
            if (deDanhChoSV(de, sv)) {
                String tt = QuanLyDanhSach.DS_KETQUA.daThi(sv.getMaSV(), de.getMaDe()) ? "[DA THI]" : "[CHUA THI]";
                System.out.println(" - " + de.getMaDe() + " | " + de.getTenDe() + " | " + de.getThoiGian() + " phut | " + tt);
                dem++;
            }
        }
        if (dem == 0) {
            System.out.println("Khong co de thi nao duoc giao!");
            return;
        }
        String maDe = NhapChuan.docChuoiKhongRong("Nhap ma de muon thi");
        DeThi de = QuanLyDanhSach.DS_DETHI.timKiemMa(maDe);
        if (de == null || !deDanhChoSV(de, sv)) {
            System.out.println("Ma de khong hop le hoac khong danh cho ban!");
            return;
        }
        if (QuanLyDanhSach.DS_KETQUA.daThi(sv.getMaSV(), maDe)) {
            System.out.println("Ban da hoan thanh de thi nay, khong the thi lai!");
            return;
        }
        ChiTietDeThi[] dsCT = QuanLyDanhSach.DS_CHITIETDETHI.layTheoMaDe(maDe);
        if (dsCT.length == 0) {
            System.out.println("De thi chua co cau hoi!");
            return;
        }
        long batDau = System.currentTimeMillis();
        String maKQ = QuanLyDanhSach.DS_KETQUA.taoMaMoi();
        ChiTietKetQua[] dsCTKQ = new ChiTietKetQua[dsCT.length];
        double tongDiem = 0;
        int soCauDung = 0;
        boolean hetGio = false;
        for (int i = 0; i < dsCT.length; i++) {
            CauHoi ch = QuanLyDanhSach.DS_CAUHOI.timKiemMa(dsCT[i].getMaCH());
            String dapAnChuan = ch == null ? "" : ch.getDapAnChuan();
            if (hetGio || ch == null) {
                dsCTKQ[i] = new ChiTietKetQua(maKQ, dsCT[i].getMaCH(), "", dapAnChuan, 0);
                continue;
            }
            System.out.println("\nCau " + (i + 1) + "/" + dsCT.length + " (" + String.format("%.2f", dsCT[i].getDiemToiDa()) + " diem):");
            ch.xuatChiTiet();
            String traLoi = docTraLoi(ch.getLoaiCauHoi());
            double diemDat = ch.chamDiem(traLoi, dsCT[i].getDiemToiDa());
            tongDiem += diemDat;
            if (Math.abs(diemDat - dsCT[i].getDiemToiDa()) < 1e-6) soCauDung++;
            dsCTKQ[i] = new ChiTietKetQua(maKQ, ch.getMaCH(), traLoi, dapAnChuan, diemDat);
            if ((System.currentTimeMillis() - batDau) >= de.getThoiGian() * 60000L) {
                System.out.println("\nHet thoi gian lam bai! He thong tu dong chot nop bai.");
                hetGio = true;
            }
        }
        long thoiGianGiay = (System.currentTimeMillis() - batDau) / 1000L;
        QuanLyDanhSach.DS_KETQUA.themVaoDanhSach(new KetQuaThi(maKQ, sv.getMaSV(), maDe, LocalDateTime.now(), soCauDung, dsCT.length, tongDiem, thoiGianGiay));
        for (int i = 0; i < dsCTKQ.length; i++) {
            QuanLyDanhSach.DS_CHITIETKETQUA.themVaoDanhSach(dsCTKQ[i]);
        }
        System.out.println("\nDa nop bai! Ma KQ: " + maKQ + " | Dung: " + soCauDung + "/" + dsCT.length + " | Diem: " + String.format("%.2f", tongDiem) + " | Thoi gian: " + thoiGianGiay + "s");
    }

    public static void xemChiTietBaiLam(String maKQ) {
        ChiTietKetQua[] dsCT = QuanLyDanhSach.DS_CHITIETKETQUA.layTheoMaKQ(maKQ);
        if (dsCT.length == 0) {
            System.out.println("Khong tim thay chi tiet bai lam cho ma " + maKQ);
            return;
        }
        String[][] bang = new String[dsCT.length + 1][5];
        bang[0] = new String[]{"Ma KQ", "Ma CH", "Tra loi", "Dap an dung", "Diem"};
        for (int i = 0; i < dsCT.length; i++) {
            bang[i + 1] = dsCT[i].xuat();
        }
        FormatBang.inBangDuLieu("CHI TIET BAI LAM " + maKQ, bang);
    }

    // loc ket qua theo bo loc cho QTV hoac GV
    public static void xemKetQuaTheoBoLoc(String maGV, boolean laQTV) {
        System.out.println("1. Theo ma de | 2. Theo MSSV | 3. Tat ca | 4. Theo diem");
        int chon = NhapChuan.docSoNguyenTrongKhoang("Chon bo loc", 1, 4);
        String giaTri = "";
        if (chon == 1) giaTri = NhapChuan.docChuoiKhongRong("Nhap ma de");
        else if (chon == 2) giaTri = NhapChuan.docChuoiKhongRong("Nhap ma sinh vien");
        else if (chon == 4) giaTri = String.format("%.2f", NhapChuan.docSoThucKhongAm("Nhap diem"));
        int tong = QuanLyDanhSach.DS_KETQUA.getSoLuong();
        KetQuaThi[] tam = new KetQuaThi[tong];
        int dem = 0;
        for (int i = 0; i < tong; i++) {
            KetQuaThi kq = QuanLyDanhSach.DS_KETQUA.docPhanTu(i);
            if (!laQTV) {
                DeThi de = QuanLyDanhSach.DS_DETHI.timKiemMa(kq.getMaDe());
                if (de == null || !de.getMaGV().equals(maGV)) continue;
            }
            boolean khop = (chon == 3)
                    || (chon == 1 && kq.getMaDe().equals(giaTri))
                    || (chon == 2 && kq.getMaSV().equals(giaTri))
                    || (chon == 4 && String.format("%.2f", kq.getDiem()).equals(giaTri));
            if (khop) tam[dem++] = kq;
        }
        String[][] bang = new String[dem + 1][8];
        bang[0] = new String[]{"Ma KQ", "Ma SV", "Ma de", "Ngay thi", "So dung", "Tong cau", "Diem", "Thoi gian (s)"};
        for (int i = 0; i < dem; i++) bang[i + 1] = tam[i].xuat();
        FormatBang.inBangDuLieu("KET QUA THI", bang);
    }

    private static String docLoaiDoiTuong() {
        while (true) {
            String s = NhapChuan.docChuoiKhongRong("Loai doi tuong (LOP/SINH_VIEN)").toUpperCase();
            if (s.equals("LOP") || s.equals("SINH_VIEN")) return s;
            System.out.println("Chi nhap LOP hoac SINH_VIEN!");
        }
    }

    private static CauHoi[] locCauHoiPhuHop(String maMon, int loai, String maGV) {
        int n = QuanLyDanhSach.DS_CAUHOI.getSoLuong();
        CauHoi[] tam = new CauHoi[n];
        int dem = 0;
        for (int i = 0; i < n; i++) {
            CauHoi ch = QuanLyDanhSach.DS_CAUHOI.docPhanTu(i);
            if (ch.getMaMonHoc().equalsIgnoreCase(maMon) && ch.getLoaiCauHoi() == loai
                    && (maGV == null || ch.getMaGV().equals(maGV))) {
                tam[dem++] = ch;
            }
        }
        return Arrays.copyOf(tam, dem);
    }

    private static boolean deDanhChoSV(DeThi de, SinhVien sv) {
        if ("LOP".equalsIgnoreCase(de.getLoaiDoiTuong())) {
            return de.getDoiTuong().equalsIgnoreCase(sv.getLop());
        }
        String[] dsMa = de.getDoiTuong().split(",");
        for (int i = 0; i < dsMa.length; i++) {
            if (dsMa[i].trim().equalsIgnoreCase(sv.getMaSV())) return true;
        }
        return false;
    }

    private static String docTraLoi(int loai) {
        if (loai == 1) return NhapChuan.docChuoi("Dap an (A/B/C/D hoac Enter bo qua)").toUpperCase();
        if (loai == 2) return NhapChuan.docChuoi("Cac dap an (vd ABC hoac Enter bo qua)").toUpperCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(NhapChuan.docDungSai("Y " + (char) ('A' + i)));
        }
        return sb.toString();
    }
}
