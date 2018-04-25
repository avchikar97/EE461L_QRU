package golden_retriever.qru;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse {

    private EditText emailField;
    private EditText fNameField;
    private EditText lNameField;
    private EditText pwField1;
    private EditText pwField2;
    private Spinner selectEntitySpinner;

    private final RegisterActivity _THISACTIVITY = this;
    private LISessionManager mLISessionManager;


    JSONObject hold = null;

    public static final String TAG = "RegClient";

    protected RestClient testing = new RestClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.email);
        fNameField = findViewById(R.id.first_name);
        lNameField = findViewById(R.id.last_name);
        pwField1 = findViewById(R.id.password1);
        pwField2 = findViewById(R.id.password2);

        selectEntitySpinner = findViewById(R.id.entitySpinner);
        selectEntitySpinner.setPrompt("Select account type");
        List<String> profiles = new ArrayList<String>();
        profiles.add("Student");
        profiles.add("Recruiter");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, profiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectEntitySpinner.setAdapter(adapter);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        ImageButton liRegisterButton = (ImageButton) findViewById(R.id.li_register_button);
        liRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LI_login();
            }
        });

    }

    private void LI_login(){
        mLISessionManager = LISessionManager.getInstance(getApplicationContext());
        mLISessionManager.init(_THISACTIVITY, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                //Toast.makeText(getApplicationContext(), "success ", Toast.LENGTH_LONG).show();
                Log.d(this.getClass().toString(), "Successful LinkedIn login");
                fetchInfo();
                //startWithLI();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Toast.makeText(getApplicationContext(), "Login failed: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void fetchInfo(){
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)?format=json";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                String profileType = null;
                String firstName = "";
                String lastName = "";
                String email = "";
                String passWord = "";
                View focusView = null;

                JSONObject LI_JSON = apiResponse.getResponseDataAsJson();
                Log.d(TAG, LI_JSON.toString());
                try {
                    profileType = (String) selectEntitySpinner.getSelectedItem();
                    firstName = LI_JSON.getString("firstName");
                    lastName = LI_JSON.getString("lastName");
                    email = LI_JSON.getString("emailAddress");
                    passWord = LI_JSON.getString("id");//we can use the LinkedIn profile ID as the password

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                RestAsync rest = new RestAsync(_THISACTIVITY);

                JSONObject emailCheck = new JSONObject();
                try {
                    emailCheck.put("email", email);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                rest.setType("GET");
                rest.execute(emailCheck);

                try{
                    hold = rest.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e ) {
                    e.printStackTrace();
                }
                if(hold.has("salt")){
                    String error_message = "LinkedIn account already registered";
                    Log.d(TAG, error_message);
                    focusView = emailField;
                    emailField.setError("LinkedIn account already registered");
                    Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
                }
                if(selectEntitySpinner == null || profileType ==null){
                    String error_message = "Please select your profile type";
                    Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
                    focusView = selectEntitySpinner;
                }
                if(focusView == null) {
                    try {
                        registration(email, passWord, firstName, lastName, profileType);//register LinkedIn user
                    } catch (BadHashAndSaltException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    focusView.requestFocus();
                }

            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                String error_message = "LinkedIn API request failed";
                Log.d(TAG, error_message);
                Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private static Scope buildScope(){
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void logIn(){
        emailField.setError(null);
        fNameField.setError(null);
        lNameField.setError(null);
        pwField1.setError(null);

        String email = emailField.getText().toString();
        String fName = fNameField.getText().toString();
        String lName = lNameField.getText().toString();
        String pw1 = pwField1.getText().toString();
        String pw2 = pwField2.getText().toString();
        String profileType = (String) selectEntitySpinner.getSelectedItem();

        RestAsync rest = new RestAsync(this);

        JSONObject emailCheck = new JSONObject();
        try {
            emailCheck.put("email", email);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        rest.setType("GET");
        rest.execute(emailCheck);

        try{
            hold = rest.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }

        boolean cancel = false;
        View focusView = null;

        if(hold.has("salt")){
            Log.d(TAG, "INVALID EMAIL ALREADY EXISTS");
            emailField.setError("This email is already registered");
            focusView = emailField;
            cancel = true;
        }

        if(!TextUtils.isEmpty(pw1) && !isPasswordValid(pw1)) {
            pwField1.setError(getString(R.string.error_invalid_password));
            focusView = pwField1;
            cancel = true;
        } else if(TextUtils.isEmpty(pw1)){
            pwField1.setError(getString(R.string.error_field_required));
            focusView = pwField1;
            cancel = true;
        }

        if(TextUtils.isEmpty(pw2)){
            pwField2.setError(getString(R.string.error_no_confirm_pw));
            focusView = pwField2;
            cancel = true;
        }

        if(TextUtils.isEmpty(fName)){
            fNameField.setError(getString(R.string.error_field_required));
            focusView = fNameField;
            cancel = true;
        }

        if(TextUtils.isEmpty(lName)){
            lNameField.setError(getString(R.string.error_field_required));
            focusView = lNameField;
            cancel = true;
        }

        if(TextUtils.isEmpty(email)) {
            emailField.setError(getString(R.string.error_field_required));
            focusView = emailField;
            cancel = true;
        } else if(!isEmailValid(email)) {
            emailField.setError(getString(R.string.error_invalid_email));
            focusView = emailField;
            cancel = true;
        }

        if(selectEntitySpinner == null || profileType ==null){
            emailField.setError("Please select a valid profile type.");
            focusView = selectEntitySpinner;
            cancel = true;
        }

        if(!cancel){
            if(!pw1.equals(pw2)){
                pwField1.setError(getString(R.string.error_pw_no_match));
                pwField2.setError(getString(R.string.error_pw_no_match));
                focusView = pwField1;
                cancel = true;
            }
        }

        if(cancel){
            focusView.requestFocus();
        } else {
            try {
                hold = null;
                registration(email, pw1, fName, lName, profileType);
            } catch(BadHashAndSaltException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void registration(String email, String password, String firstName, String lastName, String profileType) throws BadHashAndSaltException{
        String hashAndSalt = null;
        DankHash test = new DankHash();
        String pw;
        String salt;
        //byte[] salt;
        try {
            test.hashPassword(password);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }

        /*if(hashAndSalt.equals(null)){
            throw new BadHashAndSaltException("Something is wrong");
        } else {
            pw = hashAndSalt.substring(0, hashAndSalt.indexOf(" "));
            salt = hashAndSalt.substring(hashAndSalt.indexOf(" "), hashAndSalt.length() - 1);
        }*/

        pw = test.getPassword();
        salt = test.getTheSalt();

        String id = new String();

        try {
            id = RandomAPIKeyGen.generate(96);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        JSONObject newUser = new JSONObject();
        try {
            newUser.put("_id", id)
                .put("email", email)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("profileType", profileType)
                .put("passWord", pw)
                .put("salt", salt)
                .put("attachment", "");

        } catch(JSONException e) {
            e.printStackTrace();
        }

        String theMail = new String("762A784708A3");

        //double b = 12345678;

        JSONObject get = new JSONObject();
        try{
            get.put("_id", theMail);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        JSONObject insert = new JSONObject();
        try{
            insert.put("School", "University of Texas at Austin");
        } catch(JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, newUser.toString());

        RestAsync yeah = new RestAsync(this);
        yeah.setType("POST");

        yeah.execute(newUser);


        Intent sendIT = new Intent(RegisterActivity.this, LoginActivity.class);
        sendIT.putExtra("JSON", newUser.toString());
        startActivity(sendIT);

        //Log.d(TAG, hold.toString());

        //testing.postIT(newUser);
        //testing.updateIT(insert, "5ac24a964bb7953fc5a4a9fc");
    }

    @Override
    public void processFinish(JSONObject output){
        hold = output;
    }
}
