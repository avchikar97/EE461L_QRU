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
    private Button generateQRCodeButton;
    private TextView welcomeMessage;

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

        generateQRCodeButton = (Button) findViewById(R.id.generate_recruiter_QR_btn);
        generateQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RecruiterMain.this, QRGeneratorActivity.class);
                myIntent.putExtra("profiletype", "Recruiter");
                RecruiterMain.this.startActivity(myIntent);
            }
        });


    }

}
