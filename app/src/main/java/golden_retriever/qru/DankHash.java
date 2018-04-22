
package golden_retriever.qru;

import android.os.Build;
import android.util.Log;
import android.util.Base64;

import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
import java.security.Security;
import javax.crypto.SecretKeyFactory;
import java.security.Provider;
import java.security.Provider.Service;
import java.util.Set;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;



//import static org.apache.commons.codec.binary.Base64.encodeBase64String;

//import static org.apache.commons.codec.binary.Base64.decodeBase64;
//import static org.apache.commons.codec.binary.Base64.encodeBase64String;
//import org.spongycastle.jce.provider.BouncyCastleProvider;




/*
 * Created by daniel on 3/18/18.
 */


public class DankHash{
    static{
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public static final String TAG = "DANKClient";

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 192;  //bits

    private String password;
    private String theSalt;

    public void hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = getSalt();
        theSalt = Base64.encodeToString(saltBytes, Base64.DEFAULT);

        SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, 64 * 8);
        byte[] hash = key.generateSecret(spec).getEncoded();
        setPassword((new BigInteger(hash).toString()));
        //return String.format("%x %x", new BigInteger(hash), new BigInteger(saltBytes));
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String checkPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.decode(salt, Base64.DEFAULT);
        //Log.d(TAG, salt);
        //salt = salt.replace(" ", "");
        //BigInteger hold = new BigInteger(salt, 16);
        //byte[] saltBytes = hold.toByteArray();

        //Log.d(TAG, "Provided: " + hold + " What I got: " + saltBytes);

        SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, 64 * 8);
        byte[] hash = key.generateSecret(spec).getEncoded();
        return new BigInteger(hash).toString();
    }
    
    public static void testProvider() throws NoSuchAlgorithmException, InvalidKeySpecException{
        //Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

        SecretKeyFactory key = SecretKeyFactory.getInstance("HmacSHA1", Security.getProvider("SC"));

        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            boolean printedProvider = false;
            Set<Service> services = provider.getServices();
            for (Service service : services) {
                String algorithm = service.getAlgorithm();
                String type = service.getType();
                if (type.equalsIgnoreCase("SecretKeyFactory")) {
                    if (!printedProvider) {
                        System.out.printf("%n === %s ===%n%n", provider.getName());
                        printedProvider = true;
                    }
                    System.out.printf("Type: %s alg: %s%n", type, algorithm);
                }
            }
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTheSalt() {
        return theSalt;
    }

    public void setTheSalt(String saltBytes) {
        this.theSalt = saltBytes;
    }

}

