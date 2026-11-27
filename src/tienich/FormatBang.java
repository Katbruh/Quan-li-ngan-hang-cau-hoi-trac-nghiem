package tienich;

public final class FormatBang {
    private FormatBang() {
    }

    public static void inBangDuLieu(String tieuDe, String[][] bangDuLieu) {
        if (tieuDe != null && !tieuDe.isEmpty()) {
            System.out.println("\n=== " + tieuDe + " ===");
        }
        if (bangDuLieu == null || bangDuLieu.length == 0 || bangDuLieu[0] == null) {
            System.out.println("(Khong co du lieu)");
            return;
        }
        // tinh do rong lon nhat cho tung cot dua tren tat ca cac dong
        int soCot = bangDuLieu[0].length;
        int[] doRong = new int[soCot];
        for (int r = 0; r < bangDuLieu.length; r++) {
            if (bangDuLieu[r] == null) {
                continue;
            }
            for (int c = 0; c < soCot && c < bangDuLieu[r].length; c++) {
                String giaTri = bangDuLieu[r][c] == null ? "" : bangDuLieu[r][c];
                if (giaTri.length() > doRong[c]) {
                    doRong[c] = giaTri.length();
                }
            }
        }
        // ve vien tren va dong tieu de
        System.out.println(taoDuongKe(doRong, '┌', '┬', '┐'));
        inDong(bangDuLieu[0], doRong);
        // neu co du lieu thi ve duong phan cach va cac dong noi dung
        if (bangDuLieu.length > 1) {
            System.out.println(taoDuongKe(doRong, '├', '┼', '┤'));
            for (int r = 1; r < bangDuLieu.length; r++) {
                inDong(bangDuLieu[r], doRong);
            }
        }
        // ve vien duoi cua bang
        System.out.println(taoDuongKe(doRong, '└', '┴', '┘'));
        if (bangDuLieu.length == 1) {
            System.out.println("(Khong co du lieu)");
        }
    }

    public static void inBangThongKe(String[] tieuDeDong, String[] tieuDeCot, double[][] bangSoLieu) {
        if (tieuDeDong == null || tieuDeCot == null || bangSoLieu == null || tieuDeDong.length == 0) {
            System.out.println("(Khong co du lieu thong ke)");
            return;
        }
        int soDong = tieuDeDong.length;
        int soCotDuLieu = bangSoLieu[0].length;
        // kiem tra xem cot cuoi co phai cot tong cong tu dong hay khong
        boolean themCotTong = tieuDeCot.length == soCotDuLieu + 1;
        boolean tinhTongCotCuoi = tieuDeCot.length == soCotDuLieu && soCotDuLieu > 0
                && tieuDeCot[soCotDuLieu - 1].equalsIgnoreCase("Tong cong");
        int soCotBang = 1 + (themCotTong ? soCotDuLieu + 1 : soCotDuLieu);
        String[][] bang = new String[soDong + 1][soCotBang];
        bang[0][0] = "Hang muc";
        for (int c = 0; c < tieuDeCot.length && c + 1 < soCotBang; c++) {
            bang[0][c + 1] = tieuDeCot[c];
        }
        // dien du lieu tung dong va cong don tong dong vao cot cuoi
        for (int r = 0; r < soDong; r++) {
            bang[r + 1][0] = tieuDeDong[r];
            double tong = 0;
            int gioiHanCong = tinhTongCotCuoi ? soCotDuLieu - 1 : soCotDuLieu;
            for (int c = 0; c < soCotDuLieu; c++) {
                double val = bangSoLieu[r][c];
                if (c < gioiHanCong) {
                    tong += val;
                }
                bang[r + 1][c + 1] = dinhDangSo(val);
            }
            if (themCotTong) {
                bang[r + 1][soCotDuLieu + 1] = dinhDangSo(tong);
            } else if (tinhTongCotCuoi) {
                bang[r + 1][soCotDuLieu] = dinhDangSo(tong);
            }
        }
        inBangDuLieu("THONG KE", bang);
    }

    // tao chuoi duong ke ngang bang ky tu khung unicode
    private static String taoDuongKe(int[] doRong, char trai, char giua, char phai) {
        StringBuilder sb = new StringBuilder();
        sb.append(trai);
        for (int c = 0; c < doRong.length; c++) {
            for (int k = 0; k < doRong[c] + 2; k++) {
                sb.append('─');
            }
            sb.append(c < doRong.length - 1 ? giua : phai);
        }
        return sb.toString();
    }

    // in mot dong du lieu can le trai theo mang do rong tung cot
    private static void inDong(String[] dong, int[] doRong) {
        StringBuilder sb = new StringBuilder();
        sb.append('│');
        for (int c = 0; c < doRong.length; c++) {
            String giaTri = (dong != null && c < dong.length && dong[c] != null) ? dong[c] : "";
            sb.append(' ').append(giaTri);
            for (int k = giaTri.length(); k < doRong[c]; k++) {
                sb.append(' ');
            }
            sb.append(" │");
        }
        System.out.println(sb.toString());
    }

    // dinh dang so nguyen gon hoac 2 chu so thap phan
    private static String dinhDangSo(double val) {
        if (Math.abs(val - Math.round(val)) < 1e-9) {
            return String.valueOf(Math.round(val));
        }
        return String.format("%.2f", val);
    }
}
