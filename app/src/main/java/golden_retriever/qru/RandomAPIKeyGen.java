package golden_retriever.qru;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Hex;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by daniel on 3/6/18.
 */

public class RandomAPIKeyGen {
    public static String generate(final int keyLen) throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyLen);
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return Hex.encodeHexString(encoded, true);
    }

    public static String generate2(final int keyLen) throws NoSuchAlgorithmException {

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[keyLen / 8];
        random.nextBytes(bytes);
        return Hex.encodeHexString(bytes, true);
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
