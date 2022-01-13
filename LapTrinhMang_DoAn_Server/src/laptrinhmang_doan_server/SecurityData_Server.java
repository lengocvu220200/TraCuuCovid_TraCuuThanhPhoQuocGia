package laptrinhmang_doan_server;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecurityData_Server {
    //2048
    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA17vSQowcfTemnHrGzwPP36HX99okwnYjCoJQeCtFhSfDH60tEyzL/D6ECujflvMrUAYG6UfYtGWd9lTznn9Y+61NtJJ2aZecV9VACZi3Lt6rmmy4BapkbKgPk1Kdz/56ZJ04uFXVNEluSd13tjo5pbgnAXLpbyulL+Nmsk/g2l4hrBixOzB0chxTZLHgkQclPuiT0wutPOJRzWLg4iStDiFH9PiSDGsV2RYoDy8deCMR4nvly5LvcFBvH/BoCqg0kT1OcKOJ0qfhICdtsIawzY9q6eUz/FSn3oeE67svcyTfc0g8UT7IfopnaVirGWcFAI3cl8LQ1b6Jxm2S6d69HwIDAQAB";
        
    String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDXu9JCjBx9N6acesbPA8/fodf32iTCdiMKglB4K0WFJ8MfrS0TLMv8PoQK6N+W8ytQBgbpR9i0ZZ32VPOef1j7rU20knZpl5xX1UAJmLcu3quabLgFqmRsqA+TUp3P/npknTi4VdU0SW5J3Xe2OjmluCcBculvK6Uv42ayT+DaXiGsGLE7MHRyHFNkseCRByU+6JPTC6084lHNYuDiJK0OIUf0+JIMaxXZFigPLx14IxHie+XLku9wUG8f8GgKqDSRPU5wo4nSp+EgJ22whrDNj2rp5TP8VKfeh4Truy9zJN9zSDxRPsh+imdpWKsZZwUAjdyXwtDVvonGbZLp3r0fAgMBAAECggEBAMdZuCHmdyz+j+dpTO1fCGgOlhNicTWBFUBQyytAUS2KYLSUwmJFsvmUi0/JgVNrwUphR2GqEoYgoKdQOQ7tfrySmIuUC/fnMYG4fURg6mVVPh2k+SfDHKmLY1kvrsTU0yTGY/Ny3WFDubZoAIdTppGMaf7cwPkJy8tEx1FentkpD+ZKLQ2EDsINAmyktGHq5ASi8MuYf72gPkpuiyL5N+dLx+JwTPLRuFTqq9cmrVI6LSGHN6nlhXvIl5vKm6gSk02Pm3VQKVrnVbl99iLq4YBMpvGZpHnjm3bOgxl28dv9CWf24uFbYFuFHV5BrE7sbCaGDZ9YmyTq1IJPLA5UjQECgYEA9790c0vsc/FlavgCZG2O6RwNuqAfwEaZ7mZs4iWpghuAkaIC7qbCQtxwsgG5zl20/SRPOlqQYO/Sg8e8QNYWtrMOPd4ijk7pei/XfgV3vbjy06evy8s4Z5oNsHjp09YhoUIu0OD/5fHOjCbWv/je/DGDva+JDHFzxr9/0YBOZ3kCgYEA3uthrLBcKd6tjih6lvY5gwUiLkpaDkfIkxuPkk9vN8vbbFalFkvlYNugp+RklYqsTu1u6d09EFs7n03HS9o3A9piS1OfFfseCTFLVqxW08kjbNvOX2Jh5JPWctEgcYPKEXax5ESg0FOgW+NZOZTlHx2wsG+JqvioquSwjWlKa1cCgYEApq0UsDjlJH/ERnoCSgR3QBdSmTr/AYG+L+iS2naSGulbzx6bMGnaidb6rpeApPjgOeqCrvSc68qM64GmAkzGTTYbn73hq7IlW8HyDWOqUmaqX4ESQV5AXVsEWLSQQ6dtSqtXCcpF/zJ2Tk4W7mKFyk+ZnTUKU+fEB3Nwj8bteKkCgYAv0vfFgKCFKwy47t49/N0hyrwmijT/006ooMUQcpI9ujJ876nDOs0fFn9FBa6+ll2ZIT8mVRyAodIMvzb+gvZCkVt9JZd/s7wh5L1w/tAx53j+JboEpKqzyzO6tjrrn4Z+cKSBgGevygEhNYky3Uq6fjgl4gf68hXdc+qcq/YXSQKBgQCu7845/U6mMZ7pUc+o8nIArQZaSui0EbFho+75ZKPW8lfssbCUKpVysyuf4DToo9HFjMWYMyf9rx+yqI/nsEkvoNVrt06gigBEkY6WUjNZLbvh+kFTUbfhUyjFgoqQcThGPi+OWTZHXeq4hZYn/Cc52mn6d/I+MPTZLuFBRwhqPw==";
    
    
    public SecurityData_Server(){
        //Phương thức java.util.UUID.randomUUID() được sử dụng để sinh ra một chuỗi ngẫu nhiên có độ dài 32 ký tự (128 bit)
        //được biểu diễn ở hệ hệ thập lục phân (hex: 0-9A-F) và 4 ký tự phân tách (–).
        //secretKey = UUID.randomUUID().toString();
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
    public String giaiMaRSA(String chuoiCanGiaiMa){
        String kq = null;
        try{
            byte[] decodedBytesPri = Base64.getDecoder().decode(privateKey);
            // Tạo private key
            PKCS8EncodedKeySpec dd = new PKCS8EncodedKeySpec(decodedBytesPri);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = factory.generatePrivate(dd);
            // Giải mã dữ liệu
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, priKey);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(chuoiCanGiaiMa));
            kq = new String(decryptOut);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return kq;
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
