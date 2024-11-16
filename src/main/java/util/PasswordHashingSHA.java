package util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashingSHA {
    /*
    MessageDigest.getInstance("SHA-256"): Lấy một instance của MessageDigest sử dụng thuật toán SHA-256.
    md.digest(input.getBytes(StandardCharsets.UTF_8)): Chuyển đổi chuỗi đầu vào thành mảng byte UTF-8, sau đó áp dụng SHA-256 để băm chuỗi này thành một mảng byte (mã băm)
     */
    public byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /*
    Hàm này chuyển đổi mảng byte thành một chuỗi hex (chuỗi hexa) dễ đọc.
    BigInteger number = new BigInteger(1, hash): Chuyển đổi mảng byte thành số nguyên lớn, với 1 để chỉ số này là số dương.
    number.toString(16): Chuyển đổi BigInteger thành chuỗi hexa.
    while (hexString.length() < 64): Thêm số 0 vào đầu chuỗi nếu độ dài của chuỗi hex nhỏ hơn 64 ký tự (SHA-256 luôn tạo ra chuỗi hexa dài 64 ký tự).
    return hexString.toString(): Trả về chuỗi hex hoàn chỉnh.
     */
    public String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}
