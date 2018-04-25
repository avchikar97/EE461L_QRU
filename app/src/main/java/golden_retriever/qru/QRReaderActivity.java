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

import java.io.File;
import java.util.concurrent.ExecutionException;

import static android.support.v4.content.ContextCompat.startActivity;
import static java.net.Proxy.Type.HTTP;

public class QRReaderActivity extends AppCompatActivity implements AsyncResponse {
    JSONObject hold = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_qr);
        final Activity activity = this;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
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
                String email = "";
                String attachmentPath = "";
                String profileType = "";


                JSONObject json = getProfile(ID);
                try{
                    firstName = json.getString("firstName");
                    lastName = json.getString("lastName");
                    email = json.getString("email");
                    attachmentPath = json.getString("attachment");
                    profileType = json.getString("profileType");
                } catch(Exception e){
                    e.printStackTrace();
                }
                if(profileType.equals("Student")) {
                    File resume = new File(attachmentPath);
                    sendMail(email, firstName, lastName, resume);
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

        try{
            profile.put("_id", ID);
        } catch (JSONException e){
            e.printStackTrace();
        }

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

    public void sendMail(String email, String firstName, String lastName, File file){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your interaction with " + firstName + " " +lastName);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "hello!");
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/" + file.getAbsolutePath()));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        this.getBaseContext().startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    @Override
    public void processFinish(JSONObject output) {
        hold = output;
    }
}
