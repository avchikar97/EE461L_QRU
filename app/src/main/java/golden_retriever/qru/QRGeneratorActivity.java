package golden_retriever.qru;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;

        import com.google.zxing.BarcodeFormat;
        import com.google.zxing.MultiFormatReader;
        import com.google.zxing.MultiFormatWriter;
        import com.google.zxing.WriterException;
        import com.google.zxing.common.BitMatrix;
        import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGeneratorActivity extends AppCompatActivity {
    EditText text;
    Button gen_btn;
    private ImageView image;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_qrgenerator);
        image = (ImageView) findViewById(R.id.image);
        Intent intent = getIntent();
        final String ID = intent.getStringExtra("ID");
        QRGenerator QRGen = QRGenerator.getInstance();
        Bitmap bmap = QRGen.getQRCode(ID);

        image.setImageBitmap(bmap);

    }


}