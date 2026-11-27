import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import mohinh.*;
import tienich.*;

public class ConvertData {
    private static final DateTimeFormatter FMT_SQL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        new File("data").mkdirs();
        CauHoi[] dsCH = chuyenCauHoi();
        NguoiDung[] dsND = chuyenNguoiDung();
        DeThi[] dsDT = chuyenDeThi();
        ChiTietDeThi[] dsCTDT = chuyenChiTietDeThi(dsDT);
        KetQuaThi[] dsKQ = chuyenKetQua();
        ChiTietKetQua[] dsCTKQ = chuyenChiTietKetQua(dsKQ, dsDT, dsCTDT, dsCH);

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSCH))) {
            dos.writeInt(dsCH.length);
            for (int i = 0; i < dsCH.length; i++) { dos.writeInt(dsCH[i].getLoaiCauHoi()); dsCH[i].ghiFile(dos); }
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(LuuTru.FILEDSND))) {
            dos.writeInt(dsND.length);
            for (int i = 0; i < dsND.length; i++) { dos.writeInt(dsND[i] instanceof SinhVien ? 1 : 2); dsND[i].ghiFile(dos); }
        }
        ghiMang(LuuTru.FILEDSDT, dsDT);
        ghiMang(LuuTru.FILEDSCTDT, dsCTDT);
        ghiMang(LuuTru.FILEDSKQ, dsKQ);
        ghiMang(LuuTru.FILEDSCTKQ, dsCTKQ);

        QuanLyDanhSach.taiDuLieuDanhSach();
        System.out.println("Convert thanh cong -> CH: " + QuanLyDanhSach.DS_CAUHOI.getSoLuong()
                + ", ND: " + QuanLyDanhSach.DS_NGUOIDUNG.getSoLuong()
                + ", DT: " + QuanLyDanhSach.DS_DETHI.getSoLuong()
                + ", CTDT: " + QuanLyDanhSach.DS_CHITIETDETHI.getSoLuong()
                + ", KQ: " + QuanLyDanhSach.DS_KETQUA.getSoLuong()
                + ", CTKQ: " + QuanLyDanhSach.DS_CHITIETKETQUA.getSoLuong());
    }

    // ghi mang thuc the khong da hinh ra file nhi phan
    private static void ghiMang(String file, rangbuoc.ITruyXuatMoHinh[] ds) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.writeInt(ds.length);
            for (int i = 0; i < ds.length; i++) ds[i].ghiFile(dos);
        }
    }

    private static String[] docDong(String file) throws IOException {
        String[] tam = new String[500];
        int n = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("old_data/" + file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                line = line.trim();
                if (line.isEmpty()) continue;
                if (n == tam.length) tam = Arrays.copyOf(tam, tam.length * 2);
                tam[n++] = line;
            }
        }
        return Arrays.copyOf(tam, n);
    }

    private static String[] tachCot(String line) {
        String[] p = line.split("\\|", -1);
        for (int i = 0; i < p.length; i++) p[i] = p[i].trim();
        return p;
    }

    private static String doiMaCH(String maCu, int loai) {
        String tienTo = loai == 1 ? "M" : (loai == 2 ? "N" : "DS");
        return maCu.startsWith("Q") ? tienTo + maCu.substring(1) : maCu;
    }

    private static int suyRaLoaiDe(String maDe) {
        if (maDe.startsWith("NDA")) return 2;
        if (maDe.startsWith("DS")) return 3;
        return 1;
    }

    private static LocalDateTime parseNgay(String s) {
        if (s.contains("/")) return LocalDateTime.parse(s, NhapChuan.DINH_DANG);
        if (s.contains("T")) return LocalDateTime.parse(s);
        return LocalDateTime.parse(s, FMT_SQL);
    }

    private static CauHoi[] chuyenCauHoi() throws IOException {
        String[] l1 = docDong("cauhoimotdapan.txt");
        String[] l2 = docDong("cauhoinhieudapan.txt");
        String[] l3 = docDong("cauhoidungsai.txt");
        CauHoi[] ds = new CauHoi[l1.length + l2.length + l3.length];
        int k = 0;
        for (int i = 0; i < l1.length; i++) {
            String[] p = tachCot(l1[i]);
            ds[k++] = new CauHoiMotDapAn(doiMaCH(p[0], 1), p[1], p[3], p[4], p[5], p[6], p[7], p[2], p[8].charAt(0));
        }
        for (int i = 0; i < l2.length; i++) {
            String[] p = tachCot(l2[i]);
            StringBuilder da = new StringBuilder();
            for (int j = 9; j < p.length - 1; j++) {
                if (p[j].length() == 1 && p[j].charAt(0) >= 'A' && p[j].charAt(0) <= 'D') da.append(p[j]);
            }
            ds[k++] = new CauHoiNhieuDapAn(doiMaCH(p[0], 2), p[1], p[3], p[4], p[5], p[6], p[7], p[2], da.toString());
        }
        for (int i = 0; i < l3.length; i++) {
            String[] p = tachCot(l3[i]);
            boolean[] da = new boolean[4];
            for (int j = 0; j < 4; j++) da[j] = p[8 + j].equalsIgnoreCase("Đúng") || p[8 + j].equalsIgnoreCase("Dung") || p[8 + j].equalsIgnoreCase("D");
            ds[k++] = new CauHoiDungSai(doiMaCH(p[0], 3), p[1], p[3], p[4], p[5], p[6], p[7], p[2], da);
        }
        return ds;
    }

    private static NguoiDung[] chuyenNguoiDung() throws IOException {
        String[] sv = docDong("DSSV.txt");
        String[] gv = docDong("DSGV.txt");
        NguoiDung[] ds = new NguoiDung[sv.length + gv.length];
        int k = 0;
        for (int i = 0; i < sv.length; i++) {
            String[] p = tachCot(sv[i]);
            ds[k++] = new SinhVien(p[0], p[0], "123456", p[1], p[2]);
        }
        for (int i = 0; i < gv.length; i++) {
            String[] p = tachCot(gv[i]);
            ds[k++] = new GiangVien(p[0], p[0], "123456", p[1], p[2]);
        }
        return ds;
    }

    private static DeThi[] chuyenDeThi() throws IOException {
        String[] lines = docDong("dethi.txt");
        DeThi[] ds = new DeThi[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String[] p = tachCot(lines[i]);
            DeThi dt = new DeThi(p[0], p[1], p[2], suyRaLoaiDe(p[0]), Integer.parseInt(p[4]), parseNgay(p[5]), p[7], p[8], p[9], p[3]);
            dt.setNgayTao(parseNgay(p[6]));
            ds[i] = dt;
        }
        return ds;
    }

    private static ChiTietDeThi[] chuyenChiTietDeThi(DeThi[] dsDT) throws IOException {
        String[] lines = docDong("chitietcauhoibaithi.txt");
        ChiTietDeThi[] ds = new ChiTietDeThi[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String[] p = tachCot(lines[i]);
            int loai = suyRaLoaiDe(p[1]);
            for (int j = 0; j < dsDT.length; j++) {
                if (dsDT[j].getMaDe().equals(p[1])) { loai = dsDT[j].getLoaiCauHoi(); break; }
            }
            ds[i] = new ChiTietDeThi(p[1], doiMaCH(p[2], loai), Double.parseDouble(p[3]));
        }
        return ds;
    }

    private static KetQuaThi[] chuyenKetQua() throws IOException {
        String[] lines = docDong("ketqua.txt");
        KetQuaThi[] ds = new KetQuaThi[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String[] p = tachCot(lines[i]);
            String maDe = p[2];
            if (maDe.contains("[") && maDe.contains("]")) maDe = maDe.substring(maDe.indexOf('[') + 1, maDe.indexOf(']')).trim();
            ds[i] = new KetQuaThi("KQ" + String.format("%03d", i + 1), p[0], maDe, parseNgay(p[7]), Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]), Long.parseLong(p[6]));
        }
        return ds;
    }

    private static ChiTietKetQua[] chuyenChiTietKetQua(KetQuaThi[] dsKQ, DeThi[] dsDT, ChiTietDeThi[] dsCTDT, CauHoi[] dsCH) throws IOException {
        String[] lines = docDong("chitietketquabaithi.txt");
        ChiTietKetQua[] ds = new ChiTietKetQua[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String[] p = tachCot(lines[i]);
            LocalDateTime ngay = parseNgay(p[2]);
            String maKQ = "";
            for (int j = 0; j < dsKQ.length; j++) {
                if (dsKQ[j].getMaSV().equals(p[0]) && dsKQ[j].getMaDe().equals(p[1]) && dsKQ[j].getNgayThi().equals(ngay)) { maKQ = dsKQ[j].getMaKQ(); break; }
            }
            int loai = suyRaLoaiDe(p[1]);
            for (int j = 0; j < dsDT.length; j++) {
                if (dsDT[j].getMaDe().equals(p[1])) { loai = dsDT[j].getLoaiCauHoi(); break; }
            }
            String maCH = doiMaCH(p[3], loai);
            double diemToiDa = 1.0;
            for (int j = 0; j < dsCTDT.length; j++) {
                if (dsCTDT[j].getMaDe().equals(p[1]) && dsCTDT[j].getMaCH().equals(maCH)) { diemToiDa = dsCTDT[j].getDiemToiDa(); break; }
            }
            double diem = 0;
            String dapAnChuan = p[5];
            for (int j = 0; j < dsCH.length; j++) {
                if (dsCH[j].getMaCH().equals(maCH)) { dapAnChuan = dsCH[j].getDapAnChuan(); diem = dsCH[j].chamDiem(p[4], diemToiDa); break; }
            }
            ds[i] = new ChiTietKetQua(maKQ, maCH, p[4], dapAnChuan, diem);
        }
        return ds;
    }
}
