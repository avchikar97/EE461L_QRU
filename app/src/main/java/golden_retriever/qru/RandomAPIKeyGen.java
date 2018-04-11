package golden_retriever.qru;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by daniel on 3/6/18.
 */

public class RandomAPIKeyGen {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String generate(final int keyLen) throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyLen);
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return bytesToHex(encoded).toLowerCase();
    }

    public static String generate2(final int keyLen) throws NoSuchAlgorithmException {

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[keyLen / 8];
        random.nextBytes(bytes);
        return bytesToHex(bytes).toLowerCase();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /*public static void main(String[] args) {
        String key = null;
        for(int i=0; i< 5; ++i) {
            try {
                key = RandomAPIKeyGen.generate(128);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception caught");
                e.printStackTrace();
            }
            System.out.println(key);
        }
        System.out.println("==================");

        for(int i=0; i< 5; ++i) {
            try {
                key = RandomAPIKeyGen.generate(256);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception caught");
                e.printStackTrace();
            }
            System.out.println(key);
        }
        System.out.println("==================");

        for(int i=0; i< 5; ++i) {
            try {
                key = RandomAPIKeyGen.generate2(128);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception caught");
                e.printStackTrace();
            }
            System.out.println(key);
        }
        System.out.println("==================");

        for(int i=0; i< 5; ++i) {
            try {
                key = RandomAPIKeyGen.generate2(256);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception caught");
                e.printStackTrace();
            }
            System.out.println(key);
        }
    }*/
}
