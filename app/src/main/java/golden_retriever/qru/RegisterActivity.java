package golden_retriever.qru;

import android.nfc.Tag;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build.VERSION.*;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText fNameField;
    private EditText lNameField;
    private EditText pwField1;
    private EditText pwField2;

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

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

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

        boolean cancel = false;
        View focusView = null;

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
                registration(email, pw1, fName, lName);
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

    private void registration(String email, String password, String firstName, String lastName) throws BadHashAndSaltException{
        String hashAndSalt = null;
        String pw;
        String salt;
        try {
            hashAndSalt = DankHash.hashPassword(password);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }

        if(hashAndSalt.equals(null)){
            throw new BadHashAndSaltException("Something is wrong");
        } else {
            pw = hashAndSalt.substring(0, hashAndSalt.indexOf(" "));
            salt = hashAndSalt.substring(hashAndSalt.indexOf(" "), hashAndSalt.length() - 1);
        }

        /*Document newUser = new Document("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("passWord", pw)
                .append("salt", salt);*/

        JSONObject newUser = new JSONObject();
        try {
            newUser.put("email", email)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("passWord", pw)
                .put("salt", salt);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, newUser.toString());

        //testing.getIT();
        testing.postIT(newUser);
    }
}
