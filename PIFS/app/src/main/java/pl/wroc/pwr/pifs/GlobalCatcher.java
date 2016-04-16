package pl.wroc.pwr.pifs;

import android.app.Application;

/**
 * Created by Vreon-PC on 09.04.2016.
 */
public class GlobalCatcher extends Application {
    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
}
