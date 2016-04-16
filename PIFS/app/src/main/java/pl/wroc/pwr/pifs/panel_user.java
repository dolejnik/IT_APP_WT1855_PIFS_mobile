package pl.wroc.pwr.pifs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class panel_user extends AppCompatActivity {
    TextView loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_user);

        GlobalCatcher mApp = ((GlobalCatcher)getApplicationContext());
        String globalVarValue = mApp.getSomeVariable();
        loggedUser = (TextView) findViewById(R.id.printUserID);
        loggedUser.setText(globalVarValue);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Response", "Stopped");
    }
}
