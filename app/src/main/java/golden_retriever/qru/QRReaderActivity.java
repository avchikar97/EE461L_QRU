package golden_retriever.qru;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.support.v4.content.ContextCompat.startActivity;
import static java.net.Proxy.Type.HTTP;

public class QRReaderActivity extends AppCompatActivity implements AsyncResponse {
    private Button scan_btn;
    JSONObject hold = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_qr);
        scan_btn = (Button) findViewById(R.id.scan_button);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }

        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if(result.getContents() == null){
                Toast.makeText(
                        this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                String ID = result.getContents();
                String firstName = "";
                String lastName = "";


                JSONObject json = getProfile(ID);
                try{
                    firstName = json.getString("firstName");
                    lastName = json.getString("lastName");
                } catch(Exception e){

                }

                String message = "You just met " + firstName + " " + lastName +  "!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public JSONObject getProfile(String ID){
        RestAsync rest = new RestAsync(this);

        JSONObject profile = new JSONObject();

        rest.setType("GET");
        rest.execute(profile);

        try{
            hold = rest.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }
        if(hold.has("_id")){
            return hold;
        } else {
            return null;
        }
    }

    @Override
    public void processFinish(JSONObject output) {
        hold = output;
    }
}
