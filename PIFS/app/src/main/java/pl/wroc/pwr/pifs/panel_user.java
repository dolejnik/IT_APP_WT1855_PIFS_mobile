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
    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Response", "Pause");
    }
    @Override
    public void onBackPressed() {
        Log.d("Response", "BackPressed");
        minimizeApp();
    }

}
