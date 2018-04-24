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
    ImageView image;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_qrgenerator);
        //text = (EditText) findViewById(R.id.text);
        //gen_btn = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image);
        Intent intent = getIntent();
        final String ID = intent.getStringExtra("ID");
        Bitmap bmap = QRGenerator.getInstance().getQRCode(ID);

    }


}