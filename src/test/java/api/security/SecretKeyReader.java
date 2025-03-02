package api.security;

import javax.crypto.SecretKey;

public interface SecretKeyReader {
    SecretKey getKey(String keyName);
}