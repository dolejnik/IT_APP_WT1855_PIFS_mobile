package pl.wroc.pwr.pifs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class panel_user extends AppCompatActivity {
    Button showProfile;
    Button showServices;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_user);


        showProfile = (Button) findViewById(R.id.btn_profile);
        showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), panel_profile.class);
                startActivity(i);
            }
        });

        showServices = (Button) findViewById(R.id.btn_services);
        showServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), panel_services.class);
                startActivity(i);
            }
        });
        logout = (Button) findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Response", "Stopped");
    }
}
