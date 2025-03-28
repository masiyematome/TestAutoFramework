package api.utilities;

import commons.LogUtil;
import org.checkerframework.checker.units.qual.C;

import javax.crypto.*;
import java.util.Base64;

public class SecurityUtil {

    public SecretKey generateSecretKey(String algorithm,int keySize){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(keySize);
            return keyGenerator.generateKey();
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Error while generating secret key. " + e.getMessage());
            throw new RuntimeException("Error while generating secret key. ",e);
        }
    }

    public static String encrypt(String input, String algorithm, SecretKey key){
        byte[] inputAsBytes;
        try{
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            inputAsBytes = cipher.doFinal(input.getBytes());
        }catch (Exception e){
            LogUtil.logError(SecurityUtil.class,"Error while encoding input. " + e.getMessage());
            throw new RuntimeException("Error while encoding input. ", e);
        }
        return Base64.getEncoder().encodeToString(inputAsBytes);
    }

    public static String decrypt(String encryptedInput, String algorithm, SecretKey key){
        byte[] inputAsBytes;
        try{
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            inputAsBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedInput));
        }catch (Exception e){
            LogUtil.logError(SecurityUtil.class,"Error while decrypting '" + encryptedInput + "'. " + e.getMessage());
            throw new RuntimeException("Error while decrypting '" + encryptedInput + "'. ", e);
        }
        return new String(inputAsBytes);
    }

}
