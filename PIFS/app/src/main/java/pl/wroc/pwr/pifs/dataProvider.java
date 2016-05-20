package pl.wroc.pwr.pifs;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vreon-PC on 16.05.2016.
 */


public class dataProvider extends Application {

    public static HashMap<String, List<String>> getInfo(Context mContext)
    {
        String accepted = mContext.getString(R.string.state_accepted);
        HashMap<String, List<String>> choiceDetails = new HashMap<String, List<String>>();
        List<String> stateList = new ArrayList<String>();
        stateList.add(mContext.getString(R.string.state_accepted));
        stateList.add(mContext.getString(R.string.state_update));
        stateList.add(mContext.getString(R.string.state_waitingComp));
        stateList.add(mContext.getString(R.string.state_inProgres));
        stateList.add(mContext.getString(R.string.state_contactClient));
        stateList.add(mContext.getString(R.string.state_ready));
        stateList.add(mContext.getString(R.string.state_done));
        stateList.add(mContext.getString(R.string.state_rejected));

        choiceDetails.put("Status", stateList);

        return choiceDetails;
    }

}
