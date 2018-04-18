package golden_retriever.qru;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by mattjohnson on 4/10/18.
 */

public class QRGenerator {
    private volatile static QRGenerator  uniqueInstance;


    public QRGenerator(){
    }

    public static QRGenerator getInstance(){
        if (uniqueInstance == null){
            synchronized (QRGenerator.class){
                if(uniqueInstance == null){
                    uniqueInstance = new QRGenerator();
                }
            }
        }
        return uniqueInstance;
    }

    public static Bitmap getQRCode(String text){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try
        {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        }
        catch(WriterException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
