package api.security;

import javax.crypto.SecretKey;

public class ReadKey {
    public static SecretKey readKey(SecretKeyReader keyReader, String keyName){
        return keyReader.getKey(keyName);
    }
}
