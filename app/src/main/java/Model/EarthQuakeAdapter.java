package Model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.TextView;

import org.me.gcu.equakestartercode.R;

import java.text.MessageFormat;
import java.util.ArrayList;

//Gavin Mackle S1513818
public class EarthQuakeAdapter extends BaseAdapter {

    Activity context;

    ArrayList<EarthQuake> earthquakes;

    private static LayoutInflater inflater = null;

    public EarthQuakeAdapter(Activity context, ArrayList<EarthQuake> earthquakes) {
        this.context = context;
        this.earthquakes = earthquakes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return earthquakes.size();
    }

    @Override
    public EarthQuake getItem(int position)
    {
        return earthquakes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false );
        }

        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView magnitude = (TextView) convertView.findViewById(R.id.Date);

        EarthQuake eq = getItem(position);

        int colour = Colours.GetEarthquakeDisplayColour(eq.getDescription().getMagnitude());

        location.setText(eq.getLocation().getName());
        location.setBackgroundColor(colour);

        magnitude.setText(MessageFormat.format("Strength:{0}", eq.getDescription().getMagnitude()));
        magnitude.setBackgroundColor(colour);

        return convertView;
    }
}
