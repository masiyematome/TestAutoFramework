package api.security;

import api.utilities.LogUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Doppler implements SecretKeyReader{
    @Override
    public SecretKey getKey(String keyName) {
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("doppler","secrets","get", keyName, "--json");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            JsonObject jObject = JsonParser.parseString(reader.readLine()).getAsJsonObject().getAsJsonObject(keyName);
            String computedKey = jObject.get("computed").getAsString();
            return new SecretKeySpec(computedKey.getBytes(StandardCharsets.UTF_8),"AES");
        }catch (IOException e){
            LogUtil.logError(this, "Couldn't read secret key from Doppler" + e);
            throw new RuntimeException("Couldn't read secret key from Doppler", e);
        }
    }
}
