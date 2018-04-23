package golden_retriever.qru;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateRecruiterProfile extends AppCompatActivity implements AsyncResponse {

    private EditText profile_firstname;
    private EditText profile_lastname;
    private EditText profile_company;
    private EditText email;
    private EditText profile_company_about;
    private EditText profile_positions;
    private Button submit_profile_btn;

    private String ID;
    private JSONObject hold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recruiter_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ID = getIntent().getStringExtra("ID");
        final RestAsync rest = new RestAsync(this);
        rest.setId(ID);
        rest.setType("UPDATE");

        profile_firstname = (EditText) findViewById(R.id.recruiter_update_profile_firstname);
        profile_lastname = (EditText) findViewById(R.id.recruiter_update_profile_lastname);
        profile_company = (EditText) findViewById(R.id.recruiter_update_profile_company);
        email = (EditText) findViewById(R.id.recruiter_update_email);
        profile_company_about = (EditText) findViewById(R.id.recruiter_update_profile_company_about);
        profile_positions = (EditText) findViewById(R.id.recruiter_update_profile_positions);
        submit_profile_btn = (Button) findViewById(R.id.submit_recruiter_profile_btn);

        submit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete old student from database, add new one
                String new_firstName = profile_firstname.getText().toString();
                String new_lastName = profile_lastname.getText().toString();
                String new_company = profile_company.getText().toString();
                String new_email = email.getText().toString();
                String new_about = profile_company_about.getText().toString();
                String new_positions = profile_positions.getText().toString();

                JSONObject updateUser = new JSONObject();
                try {
                    updateUser.put("firstName", new_firstName)
                            .put("lastName", new_lastName)
                            .put("company", new_company)
                            .put("email", new_email)
                            .put("about", new_about)
                            .put("position", new_positions);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                rest.execute(updateUser);

                Recruiter_Profile new_recruiter = new Recruiter_Profile("0", "dummySalt", "dummyPass", new_firstName, new_lastName, "dummyEmail", new_company, new_about,
                        new_positions);

                Intent myIntent = new Intent(UpdateRecruiterProfile.this, RecruiterMain.class);
                myIntent.putExtra("profiletype", "Recruiter");
                UpdateRecruiterProfile.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void processFinish(JSONObject output){
        hold = output;
    }

}
