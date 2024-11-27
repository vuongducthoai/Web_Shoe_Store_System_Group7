package util;

import java.util.Base64;

public class ConvertImageStringToByteArray {
    public static byte[] convertBase64ToByteArray(String base64Image) {
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                base64Image = base64Image.replaceAll("[^A-Za-z0-9+/=]", "");
                // Kiểm tra chuỗi base64 có hợp lệ không
                if (!isBase64(base64Image)) {
                    throw new IllegalArgumentException("Chuỗi base64 không hợp lệ");
                }
                return Base64.getDecoder().decode(base64Image);  // Giải mã từ base64 thành byte[]
            } catch (IllegalArgumentException e) {
                System.err.println("Lỗi giải mã base64: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean isBase64(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
