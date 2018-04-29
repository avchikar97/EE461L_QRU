package golden_retriever.qru;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class QRReaderActivity extends AppCompatActivity implements AsyncResponse {
    JSONObject mine = null;
    JSONObject yours = null;
    String myID;    // ID of user reading the app

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
        myID = getIntent().getStringExtra("ID");

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


                // profile of activity
                String myfirstName = "";
                String mylastName = "";
                String myemail = "";
                String myattachmentPath = "";
                String myprofileType = "";
                String myCompany = "";



                JSONObject json = getProfile(ID, "yours");
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
                    // send message from recruiter

                    JSONObject myProfile = getProfile(myID, "mine");

                    try{
                        myfirstName = myProfile.getString("firstName");
                        mylastName = myProfile.getString("lastName");
                        myemail = myProfile.getString("email");
                        myattachmentPath = myProfile.getString("attachment");
                        myprofileType = myProfile.getString("profileType");
                        myCompany = myProfile.getString("companyName");

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    File companyAttachment = new File(myattachmentPath);
                    String message = "Hello " + firstName + " " + lastName + ",\n"
                            + "Thank you for visiting with " + myCompany + ". \n\n" + myfirstName + "\n"
                            + mylastName + "\n" + myCompany;
                    String subject = "Your interaction with " + myfirstName + " " + mylastName + " at " + myCompany;
                    sendMail(email, subject, message,  companyAttachment);




                } else if (profileType.equals("Recruiter")){
                    JSONObject yourProfile = getProfile(ID, "yours");
                    try{
                        myfirstName = yourProfile.getString("firstName");
                        mylastName = yourProfile.getString("lastName");
                        myemail = yourProfile.getString("email");
                        myattachmentPath = yourProfile.getString("attachment");
                        myprofileType = yourProfile.getString("profileType");

                    } catch(Exception e){
                        e.printStackTrace();
                    }


                    File resume = new File(attachmentPath);
                    String message = "Hello " + firstName + " " + lastName + ",\n"
                            + "Thank you for visiting with me at the job fair. Attached is my resume. \n\n" + myfirstName
                            + "\n" + mylastName;
                    String subject = "Your interaction with " + myfirstName + " " + mylastName;
                    sendMail(email, subject, message,  resume);
                }

                String message = "You just met " + firstName + " " + lastName +  "!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public JSONObject getProfile(String ID, String profileType){
        RestAsync rest = new RestAsync(this);
        if (profileType.equals("mine")){
            JSONObject profile = new JSONObject();

            try{
                profile.put("_id", ID);
            } catch (JSONException e){
                e.printStackTrace();
            }

            rest.setType("GET");
            rest.execute(profile);

            try{
                mine = rest.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e ) {
                e.printStackTrace();
            }
            if(mine.has("_id")){
                return mine;
            } else {
                return null;
            }
        }
        else {
            JSONObject profile = new JSONObject();

            try {
                profile.put("_id", ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            rest.setType("GET");
            rest.execute(profile);

            try {
                yours = rest.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (yours.has("_id")) {
                return yours;
            } else {
                return null;
            }
        }
    }

    public void sendMail(String email, String subject, String message, File file){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/" + file.getAbsolutePath()));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        this.getBaseContext().startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    @Override
    public void processFinish(JSONObject output) {
        mine = output;
    }
}
