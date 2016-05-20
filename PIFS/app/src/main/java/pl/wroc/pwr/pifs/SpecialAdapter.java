package pl.wroc.pwr.pifs;

/**
 * Created by Vreon-PC on 20.05.2016.
 */
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SpecialAdapter extends SimpleAdapter {
    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

    public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        //String value = (String) getItem(position);
        String device_name = "";
        device_name = ((TextView) view.findViewById(R.id.device_name)).getText()
                .toString();
        String order_State = "";
        order_State = (device_name.split("\\.")[1]).split("\\- ")[1];
        //Log.d("SpecialAdapter", order_State);
        if (order_State.equals("0")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorGray));
            Log.d("SpecialAdapter", "colorGray");

        }
        if (order_State.equals("1")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorLightGray));
            Log.d("SpecialAdapter", "colorLightGray");

        }
        if (order_State.equals("2")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorLightGray));
            Log.d("SpecialAdapter", "colorLightGray");

        }
        if (order_State.equals("3")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorLightGray));
            Log.d("SpecialAdapter", "colorLightGray");

        }
        if (order_State.equals("4")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorOrange));
            Log.d("SpecialAdapter", "colorOrange");

        }
        if (order_State.equals("5")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorLightGray));
            Log.d("SpecialAdapter", "colorLightGray");

        }
        if (order_State.equals("6")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorGreen));
            Log.d("SpecialAdapter", "colorGreen");

        }
        if (order_State.equals("7")) {
            view.setBackgroundColor(parent.getResources().getColor(R.color.colorRed));
            Log.d("SpecialAdapter", "colorRed");

        }
        return view;
    }
}