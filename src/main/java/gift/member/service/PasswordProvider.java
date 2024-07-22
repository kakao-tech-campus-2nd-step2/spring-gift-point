package gift.member.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

class PasswordProvider {
    public static String encode(final String username, final String password) {
        try {
            byte[] salt = getSalt(username);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64Coder.encodeLines(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getSalt(String username) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(username.getBytes());
    }

    public static boolean match(final String username, final String password, final String encodedPassword) {
        return encodedPassword.equals(encode(username, password));
    }
}
