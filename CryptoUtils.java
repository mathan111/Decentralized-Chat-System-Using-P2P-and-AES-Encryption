import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private static final String KEY = "1234567890abcdef";
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }
    public static String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }
}
