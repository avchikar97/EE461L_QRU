package golden_retriever.qru;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RecruiterMain extends AppCompatActivity {
    private Button updateProfileButton;
    private Button newScanButton;
    private TextView welcomeMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_main);

        updateProfileButton = (Button) findViewById(R.id.update_recruiter_profile_btn);
        updateProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // go to update profile activity
            }
        });

        newScanButton = (Button) findViewById(R.id.scan_button);
        newScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(RecruiterMain.this, StaticQR.class);
                myIntent.putExtra("profiletype", "Recruiter");
                RecruiterMain.this.startActivity(myIntent);
            }
        });


    }

}
