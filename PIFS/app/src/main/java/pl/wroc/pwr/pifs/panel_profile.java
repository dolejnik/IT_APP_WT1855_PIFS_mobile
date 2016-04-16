package pl.wroc.pwr.pifs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class panel_profile extends AppCompatActivity {
    TextView userID;
    TextView tokenID;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_profile);

        GlobalCatcher mApp = ((GlobalCatcher)getApplicationContext());
        String globalVarValue = mApp.getSomeVariable1();
        userID = (TextView) findViewById(R.id.text_userID);
        userID.setText(globalVarValue);
        String globalVarValue2 = mApp.getSomeVariable2();
        tokenID = (TextView) findViewById(R.id.text_tokenID);
        tokenID.setText(globalVarValue2);
        String globalVarValue3 = mApp.getSomeVariable3();
        email = (TextView) findViewById(R.id.text_email);
        email.setText(globalVarValue3);

    }
}
