package golden_retriever.qru;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.jcajce.provider.symmetric.ARC4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class UploadResume extends AppCompatActivity implements AsyncResponse{
    Button button;
    Button homeButton;
    TextView textView;
    private JSONObject hold;
    private String ID = "";
    private byte[] bArray;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }

        ID = this.getIntent().getStringExtra("ID");
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(UploadResume.this)
                        .withRequestCode(1000)
                        .withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                        //.withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });

        homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIT = new Intent(UploadResume.this, StudentMain.class);
                sendIT.putExtra("ID", ID);
                startActivity(sendIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            try
            {
                bArray = loadFile(filePath);
            }
            catch (IOException e) {e.printStackTrace();}
            addNewResume(filePath);
            // Do anything with file

            Log.d("D", filePath);
            String filename = filePath.substring(filePath.lastIndexOf("/")+1);
            File file = new File(filePath);
            //sendMail("mjohnson082396@gmail.com", "Matt", "Johnson", file);
            textView.setText(filePath);
        }
    }

    protected void addNewResume(String resume_fp){
        if(resume_fp == null){
            return;
        }
        RestAsync checkResume = new RestAsync(this);
        JSONObject result = null;
        checkResume.setType("GET");

        JSONObject checkObj = new JSONObject();
        try {
            checkObj.put("_id", ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkResume.execute(checkObj);

        try{
            result = checkResume.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }

        //retrieving JSON object
        RestAsync rest = new RestAsync(this);
        RestAsync restResume = new RestAsync(this);

        if(!result.has("resumeid")) {
            rest.setType("UPDATE");
            restResume.setType("POST");
            rest.setId(ID);
            String id = new String();

            try {
                id = RandomAPIKeyGen.generate(96);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            JSONObject resumeObj = new JSONObject();
            try {
                resumeObj.put("_id", id)
                        .put("resumeData", Base64.encodeToString(bArray, Base64.DEFAULT));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject resume = new JSONObject();
            try {
                resume.put("resumeid", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            rest.execute(resume);
            restResume.execute(resumeObj);
        } else {
            restResume.setType("UPDATE");

            JSONObject resumeObj = new JSONObject();
            try {
                resumeObj.put("resumeData", Base64.encodeToString(bArray, Base64.DEFAULT));
                restResume.setId(result.getString("resumeid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            restResume.execute(resumeObj);
        }

        /*JSONObject idCheck = new JSONObject();
        try {
            idCheck.put("_id", this.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rest.setType("GET");
        rest.execute(idCheck);
        try {
            hold = rest.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //updating file path
        hold.remove("attachment");
        try {
            hold.put("attachment", resume_fp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //putting updated JSON object
        rest = new RestAsync(this);
        rest.setType("POST");
        rest.execute(hold);
        Log.d(this.getClass().toString(), hold.toString());*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1001:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }


    }



    public static byte[] readFully(InputStream stream) throws IOException
    {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1)
        {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public static byte[] loadFile(String sourcePath) throws IOException
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(sourcePath);
            return readFully(inputStream);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }

//    public void sendMail(String email, String firstName, String lastName, File file){
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        // The intent does not have a URI, so declare the "text/plain" MIME type
//        emailIntent.setType("text/html");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email}); // recipients
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your interaction with " + firstName + " " +lastName);
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "hello!");
//        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/" + file.getAbsolutePath()));
//        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        this.getBaseContext().startActivity(Intent.createChooser(emailIntent, "Send email"));
//    }

    @Override
    public void processFinish(JSONObject output) {
        hold = output;
    }
}
