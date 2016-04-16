package pl.wroc.pwr.pifs;

import android.app.Application;

/**
 * Created by Vreon-PC on 09.04.2016.
 */
public class GlobalCatcher extends Application {
    private String someVariable1;
    private String someVariable2;
    private String someVariable3;

    public String getSomeVariable1() {
        return someVariable1;
    }
    public String getSomeVariable2() {
        return someVariable2;
    }
    public String getSomeVariable3() {
        return someVariable3;
    }

    public void setSomeVariable1(String someVariable1) {
        this.someVariable1 = someVariable1;
    }
    public void setSomeVariable2(String someVariable2) {
        this.someVariable2 = someVariable2;
    }
    public void setSomeVariable3(String someVariable3) { this.someVariable3 = someVariable3; }
}
