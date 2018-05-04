package golden_retriever.qru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecruiterMain extends AppCompatActivity {
    private Button updateProfileButton;
    private Button newScanButton;
    private TextView welcomeMessage;
    private Button LogOffButton;

    private String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_main);

        ID = getIntent().getStringExtra("ID");

        updateProfileButton = (Button) findViewById(R.id.update_recruiter_profile_btn);
        updateProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(RecruiterMain.this, UpdateRecruiterProfile.class);
                myIntent.putExtra("profiletype", "Recruiter");
                myIntent.putExtra("ID", ID);
                RecruiterMain.this.startActivity(myIntent);
            }
        });

        newScanButton = (Button) findViewById(R.id.new_scan_btn);
        newScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(RecruiterMain.this, QRReaderActivity.class);
                myIntent.putExtra("profiletype", "Recruiter");
                myIntent.putExtra("ID", ID);
                RecruiterMain.this.startActivity(myIntent);
            }
        });


        LogOffButton = (Button) findViewById(R.id.log_off_recruiter_button);
        LogOffButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(RecruiterMain.this, LoginActivity.class);
                RecruiterMain.this.startActivity(myIntent);
            }
        });
    }

}
