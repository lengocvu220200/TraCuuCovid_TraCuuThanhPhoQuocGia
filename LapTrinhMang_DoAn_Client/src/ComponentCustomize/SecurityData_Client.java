package ComponentCustomize;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecurityData_Client {
    //2048
    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA17vSQowcfTemnHrGzwPP36HX99okwnYjCoJQeCtFhSfDH60tEyzL/D6ECujflvMrUAYG6UfYtGWd9lTznn9Y+61NtJJ2aZecV9VACZi3Lt6rmmy4BapkbKgPk1Kdz/56ZJ04uFXVNEluSd13tjo5pbgnAXLpbyulL+Nmsk/g2l4hrBixOzB0chxTZLHgkQclPuiT0wutPOJRzWLg4iStDiFH9PiSDGsV2RYoDy8deCMR4nvly5LvcFBvH/BoCqg0kT1OcKOJ0qfhICdtsIawzY9q6eUz/FSn3oeE67svcyTfc0g8UT7IfopnaVirGWcFAI3cl8LQ1b6Jxm2S6d69HwIDAQAB";
    
    public SecurityData_Client(){
        
    }
    
    public String maHoaRSA(String chuoiCanMaHoa){
        String strEncrypt = "";
        try{
            byte[] decodedBytesPub = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytesPub);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = factory.generatePublic(spec);

            // Mã hoá dữ liệu
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, pubKey);
            byte encryptOut[] = c.doFinal(chuoiCanMaHoa.getBytes());
            strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return strEncrypt;
    }
    
    public String maHoaAES(String strToEncrypt, String myKey) {
        try {
              MessageDigest sha = MessageDigest.getInstance("SHA-1");
              byte[] key = myKey.getBytes("UTF-8");
              key = sha.digest(key);
              key = Arrays.copyOf(key, 16);
              SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
              Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
              cipher.init(Cipher.ENCRYPT_MODE, secretKey);
              return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
              System.out.println(e.toString());
        }
        return null;
    }
    public String giaiMaAES(String strToDecrypt, String myKey) {
        try {
              MessageDigest sha = MessageDigest.getInstance("SHA-1");
              byte[] key = myKey.getBytes("UTF-8");
              key = sha.digest(key);
              key = Arrays.copyOf(key, 16);
              SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
              Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
              cipher.init(Cipher.DECRYPT_MODE, secretKey);
              return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
              System.out.println(e.toString());
        }
        return null;
    }
}
