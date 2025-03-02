package api.security;

import api.utilities.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityHandler {

    public static SecretKey generateKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(SecurityHandler.class, "Could not generate key. " + e);
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(SecretKey key, String dataToEncrypt){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(dataToEncrypt.getBytes()));
        }catch (Exception e){
            LogUtil.logError(SecurityHandler.class, "Couldn't encrypt '" + dataToEncrypt + "' " + e);
            throw new RuntimeException("Couldn't encrypt '" + dataToEncrypt + "' ",e);
        }
    }

    public static String decrypt(SecretKey key, String dataToDecrypt){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dataInRawForm = cipher.doFinal(Base64.getDecoder().decode(dataToDecrypt));
            return new String(dataInRawForm, StandardCharsets.UTF_8);
        }catch (Exception e){
            LogUtil.logError(SecurityHandler.class, "Couldn't decrypt '" + dataToDecrypt + "' " + e);
            throw new RuntimeException("Couldn't decrypt '" + dataToDecrypt + "' ",e);
        }
    }

}
