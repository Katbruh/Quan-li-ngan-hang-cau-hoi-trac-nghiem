package tienich;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class NhapChuan {
    private static final Scanner SCANNER = new Scanner(System.in, "UTF-8");
    public static final DateTimeFormatter DINH_DANG = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private NhapChuan() {
    }

    // doc chuoi tu ban phim va cat khoang trang hai dau
    public static String docChuoi(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt + ": ");
        }
        return SCANNER.nextLine().trim();
    }

    public static String docChuoiKhongRong(String prompt) {
        while (true) {
            String s = docChuoi(prompt);
            if (!s.isEmpty()) return s;
            System.out.println("Khong duoc de trong! Vui long nhap lai.");
        }
    }

    public static int docSoNguyen(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(docChuoi(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so nguyen hop le!");
            }
        }
    }

    public static int docSoNguyenKhongAm(String prompt) {
        while (true) {
            int val = docSoNguyen(prompt);
            if (val >= 0) return val;
            System.out.println("Gia tri phai lon hon hoac bang 0!");
        }
    }

    public static int docSoNguyenTrongKhoang(String prompt, int min, int max) {
        while (true) {
            int val = docSoNguyen(prompt);
            if (val >= min && val <= max) return val;
            System.out.println("Gia tri phai nam trong khoang " + min + " - " + max + "!");
        }
    }

    public static double docSoThuc(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(docChuoi(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so thuc hop le!");
            }
        }
    }

    public static double docSoThucKhongAm(String prompt) {
        while (true) {
            double val = docSoThuc(prompt);
            if (val >= 0) return val;
            System.out.println("Gia tri phai lon hon hoac bang 0!");
        }
    }

    // doc ngay gio theo dinh dang dd/MM/yyyy HH:mm
    public static LocalDateTime docNgayGio(String prompt) {
        while (true) {
            try {
                return LocalDateTime.parse(docChuoi(prompt + " (dd/MM/yyyy HH:mm)"), DINH_DANG);
            } catch (DateTimeParseException e) {
                System.out.println("Dinh dang ngay gio khong hop le!");
            }
        }
    }

    public static int docNam(String prompt) {
        return docSoNguyenTrongKhoang(prompt, 1000, 9999);
    }

    public static boolean docYesNo(String prompt, boolean defaultValue) {
        String goiY = defaultValue ? " (Y/n)" : " (y/N)";
        while (true) {
            String s = docChuoi(prompt + goiY).toLowerCase();
            if (s.isEmpty()) return defaultValue;
            if (s.equals("y") || s.equals("yes") || s.equals("co") || s.equals("c")) return true;
            if (s.equals("n") || s.equals("no") || s.equals("khong") || s.equals("k")) return false;
            System.out.println("Vui long nhap Y hoac N!");
        }
    }

    // nhan D, S hoac Enter rong tra ve dau '-'
    public static char docDungSai(String prompt) {
        while (true) {
            String s = docChuoi(prompt + " (D/S hoac Enter bo qua)").toUpperCase();
            if (s.isEmpty()) return '-';
            if (s.equals("D") || s.equals("S")) return s.charAt(0);
            System.out.println("Chi nhap D, S hoac Enter!");
        }
    }

    public static int nhapLuaChonMenu() {
        return docSoNguyen("Nhap lua chon");
    }
}
