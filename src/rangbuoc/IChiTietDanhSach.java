package rangbuoc;

public interface IChiTietDanhSach extends ITruyVanDanhSach {
    // maCha co the la maDe (chi tiet de thi) hoac maKQ (chi tiet ket qua)
    void xoaTheoMa(String maCha);
    int demTheoMa(String maCha);
}
