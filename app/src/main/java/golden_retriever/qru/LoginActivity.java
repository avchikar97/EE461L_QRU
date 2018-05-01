package golden_retriever.qru;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.utils.Scope;
import com.linkedin.platform.listeners.AuthListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, AsyncResponse {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    public static final String TAG = "LoginClient";

    private UserLoginTask mAuthTask = null;

    /**
     * LinkedIn Variables
     */
    private final LoginActivity _THISACTIVITY = this;
    private LISessionManager mLISessionManager;
    private JSONObject mLIResponse;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner selectEntitySpinner;

    private JSONObject hold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        generatePackageHash();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        ImageButton mLinkedinSignInButton = (ImageButton) findViewById(R.id.linkedin_sign_in_button);
        mLinkedinSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attempt_LILogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(theIntent);
            }
        });

        // set up dropdown for recruiter, student
//        selectEntitySpinner = findViewById(R.id.entitySpinner);
//        selectEntitySpinner.setPrompt("Select a profile");
//        List<String> profiles = new ArrayList<String>();
//        profiles.add("Student");
//        profiles.add("Recruiter");
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                android.R.layout.simple_spinner_item, profiles);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        selectEntitySpinner.setAdapter(adapter);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String profileType = null;
        String ID = null;

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

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        else if(TextUtils.isEmpty(password)){
            password = "";
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check if email is registered
        if(!hold.has("salt")){
            Log.d(TAG, "INVALID EMAIL DOES NOT EXISTS");
            mEmailView.setError("This email is not registered");
            focusView = mEmailView;
            cancel = true;
        }

        // Check if password is correct
        String hashed = null;
        try {
            hashed = DankHash.checkPassword(password, hold.getString("salt"));
            //Log.d(TAG, "Provided password: " + mine.getString("passWord") + " What I got: " + hashed);
            if(!hashed.equals(hold.getString("passWord"))){
                mPasswordView.setError("Password is incorrect");
                focusView = mPasswordView;
                cancel = true;
            } else {
                profileType = hold.getString("profileType");
                ID = hold.getString("_id");
            }
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            mPasswordView.setError("Email/password do not match");
            focusView = mPasswordView;
            cancel = true;
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
            mPasswordView.setError("Email/password do not match");
            focusView = mPasswordView;
            cancel = true;
        } catch(JSONException e) {
            e.printStackTrace();
            mPasswordView.setError("Email/password do not match");
            focusView = mPasswordView;
            cancel = true;
        }

        // check that drop-down list was selected

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            if(profileType == null){
                profileType = "";
            }
            // start Main activity depending on different types of profile
            if (profileType.equals("Recruiter")) {
                Intent myIntent = new Intent(LoginActivity.this, RecruiterMain.class);
                myIntent.putExtra("email", email); //Optional parameters
                myIntent.putExtra("ID", ID);
                LoginActivity.this.startActivity(myIntent);
            } else if(profileType.equals("Student")) {
                Intent myIntent = new Intent(LoginActivity.this, StudentMain.class);
                myIntent.putExtra("email", email); //Optional parameters
                myIntent.putExtra("ID", ID);
                LoginActivity.this.startActivity(myIntent);
            }
        }
    }

    private void attempt_LILogin(){
        mLISessionManager = LISessionManager.getInstance(getApplicationContext());
        mLISessionManager.init(_THISACTIVITY, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                String message = "LinkedIn login successful";
                //Toast.makeText(getApplicationContext(), "LinkedIn login successful", Toast.LENGTH_LONG).show();
                Log.d(this.getClass().toString(), message);
                fetchInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void fetchInfo(){
        String url = "https://api.linkedin.com/v1/people/~:(id,email-address)?format=json";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                String profileType = null;
                String email = "";
                String passWord = "";
                String ID = null;
                View focusView = null;

                JSONObject LI_JSON = apiResponse.getResponseDataAsJson();
                try {
                    email = LI_JSON.getString("emailAddress");
                    passWord = LI_JSON.getString("id");//we can use the LinkedIn profile ID as the password

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mAuthTask != null) {
                    return;
                }

                // Reset errors.
                mEmailView.setError(null);
                mPasswordView.setError(null);

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

                // Check if email is registered
                if(!hold.has("salt")){
                    Log.d(TAG, "INVALID EMAIL DOES NOT EXISTS");
                    mEmailView.setError("This LinkedIn account is not registered");
                    focusView = (Button) findViewById(R.id.register_button);
                }
                // Check if password is correct
                String hashed = null;
                try {
                    hashed = DankHash.checkPassword(passWord, hold.getString("salt"));
                    //Log.d(TAG, "Provided password: " + mine.getString("passWord") + " What I got: " + hashed);
                    if(!hashed.equals(hold.getString("passWord"))){
                        focusView = (Button) findViewById(R.id.register_button);
                    } else {
                        profileType = hold.getString("profileType");
                        ID = hold.getString("_id");
                    }
                } catch(NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    focusView = (Button) findViewById(R.id.register_button);
                } catch(InvalidKeySpecException e) {
                    e.printStackTrace();
                    focusView = (Button) findViewById(R.id.register_button);
                } catch(JSONException e) {
                    e.printStackTrace();
                    focusView = (Button) findViewById(R.id.register_button);
                }

                // check that drop-down list was selected

                if (focusView != null) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    String error_message = "Please register your LinkedIn account or a new account";
                    Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    showProgress(true);
                    mAuthTask = new UserLoginTask(email, passWord);
                    mAuthTask.execute((Void) null);
                    if(profileType == null){
                        profileType = "";
                    }
                    // start Main activity depending on different types of profile
                    if (profileType.equals("Recruiter")) {
                        Intent myIntent = new Intent(LoginActivity.this, RecruiterMain.class);
                        myIntent.putExtra("email", email); //Optional parameters
                        myIntent.putExtra("ID", ID);
                        LoginActivity.this.startActivity(myIntent);
                    } else if(profileType.equals("Student")) {
                        Intent myIntent = new Intent(LoginActivity.this, StudentMain.class);
                        myIntent.putExtra("email", email); //Optional parameters
                        myIntent.putExtra("ID", ID);
                        LoginActivity.this.startActivity(myIntent);
                    }
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.d(TAG, liApiError.toString());
                Toast.makeText(getApplicationContext(), liApiError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void generatePackageHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("golden_retriever.qru",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch(PackageManager.NameNotFoundException| NoSuchAlgorithmException e) {
        }
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        int contain = 0;
        for(int i = 0; i < email.length(); i++)
        {
            if (String.valueOf(email.charAt(i)).equals("@"))
            {
                contain++;
            }
        }

        if(contain != 1) return false;
        if((email.substring(0, email.indexOf('@')).length() < 1) ||
            (email.substring(email.indexOf('@') + 1, email.length()).length() < 1) ||
                ((!email.contains(".com")) && (!email.contains(".edu")) && (!email.contains(".org")) && (!email.contains(".gov"))))
            return false;
        return true;
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    public void processFinish(JSONObject output){
        hold = output;
    }

    private static Scope buildScope(){
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

}

