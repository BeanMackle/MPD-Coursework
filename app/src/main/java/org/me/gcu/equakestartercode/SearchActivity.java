package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import net.EarthQuakeListReceiver;
import net.EarthQuakeSearchReceiver;
import net.EarthQuakeService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Model.Colours;
import Model.EarthQuake;

//Gavin Mackle S1513818
public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private Date BeginDate;

    private Date EndDate;

    public ArrayList<EarthQuake> EarthQuakes;

    private EarthQuake NorthQuake;

    private EarthQuake SouthQuake;

    private EarthQuake EastQuake;

    private EarthQuake WestQuake;

    private EarthQuake StrongestQuake;

    private EarthQuake DeepestQuake;

    private EarthQuake ShallowestQuake;

    private DatePickerFragment datePicker;

    private TextView North;

    private TextView East;

    private TextView South;

    private TextView West;

    private TextView Deepest;

    private TextView Strongest;

    private TextView Shallowest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        North = findViewById(R.id.north);
        East = findViewById(R.id.east);
        South = findViewById(R.id.south);
        West = findViewById(R.id.west);
        Deepest = findViewById(R.id.deep);
        Strongest = findViewById(R.id.strongest);
        Shallowest = findViewById(R.id.shallowest);
        SetupOnClicks();
    }

    public void Date1Picker(View view)
    {
        OpenDialog("date1");
    }

    public void Date2Picker(View view)
    {
        OpenDialog("date2");
    }

    private void OpenDialog(String tag)
    {
         datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), tag);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(datePicker.getTag().equals("date1"))
        {
            BeginDate = c.getTime();
        }
        else
            {
                EndDate = c.getTime();
            }
    }

    private void Search()
    {
        ResetTextViews();
        ResetEarthQuakes();
        if(EndDate == null)
        {
            EndDate = BeginDate;
        }

        if(BeginDate == null)
        {
            BeginDate = EndDate;
        }

        EarthQuakeService.StartServiceDateSearch(this, new EarthQuakeSearchReceiver(this), BeginDate, EndDate);
        BeginDate = null;
        EndDate = null;
    }

    private void ResetTextViews()
    {
        North.setText(R.string.most_northely_earthquake);
        East.setText(R.string.most_eastily_earthquake);
        South.setText(R.string.most_southernly_earthquake);
        West.setText(R.string.most_westernly_eartquake);
        Deepest.setText(R.string.deepest_earthquake);
        Strongest.setText(R.string.strongest_earthquake);
        Shallowest.setText(R.string.most_shallow_earthquake);
    }

    private void ResetEarthQuakes()
    {
         NorthQuake = null;

         SouthQuake = null;

        EastQuake = null;

         WestQuake = null;

        StrongestQuake = null;

        DeepestQuake = null;

       ShallowestQuake = null;
    }

    public void SubmitSearch(View view)
    {
     Search();
    }

    public void sortQuakes()
    {
        int size = EarthQuakes.size();

        Log.e("TEST", String.valueOf(size));
        for(EarthQuake e: EarthQuakes)
        {
            CheckLatitude(e);
            CheckLongtitude(e);
            CheckEarthQuakeStrength(e);
            CheckEarthQuakeDepth(e);
        }

        DisplayEarthquakes();
    }

    private void CheckLatitude(EarthQuake earthQuake)
    {
        if(EastQuake == null)
        {
            EastQuake = earthQuake;
        }
        else
        {
            if(EastQuake.getLocation().GetLongtitude() < earthQuake.getLocation().GetLongtitude())
            {
                EastQuake = earthQuake;
            }
        }

        if(WestQuake == null)
        {
            WestQuake = earthQuake;
        }
        else
        {
            if(earthQuake.getLocation().GetLongtitude() < WestQuake.getLocation().GetLongtitude())
            {
                WestQuake = earthQuake;
            }
        }
    }

    private void CheckLongtitude(EarthQuake earthQuake)
    {
        if(NorthQuake == null)
        {
            NorthQuake = earthQuake;
        }
        else
            {
                if(NorthQuake.getLocation().GetLongtitude() < earthQuake.getLocation().GetLongtitude())
                {
                    NorthQuake = earthQuake;
                }
            }

        if(SouthQuake == null)
        {
            SouthQuake = earthQuake;
        }
        else
            {
                if(earthQuake.getLocation().GetLongtitude() < SouthQuake.getLocation().GetLongtitude())
                {
                    SouthQuake = earthQuake;
                }
            }
    }

    private void CheckEarthQuakeDepth(EarthQuake earthQuake)
    {
        if (DeepestQuake == null)
        {
            DeepestQuake = earthQuake;
        }
        else if(DeepestQuake.getDescription().getDepth() < earthQuake.getDescription().getDepth())
        {
            DeepestQuake = earthQuake;
        }

        if(ShallowestQuake == null)
        {
            ShallowestQuake = earthQuake;
        }
        else if(earthQuake.getDescription().getDepth() < ShallowestQuake.getDescription().getDepth())
        {
            ShallowestQuake = earthQuake;
        }
    }

    private void CheckEarthQuakeStrength(EarthQuake earthQuake)
    {
        if(StrongestQuake == null)
        {
            StrongestQuake = earthQuake;
        }
        else if(StrongestQuake.getDescription().getMagnitude() < earthQuake.getDescription().getMagnitude())
        {
            StrongestQuake = earthQuake;
        }
    }

    private void DisplayEarthquakes()
    {
        North.append(Html.fromHtml(BuildDisplayQuake(NorthQuake)));
        North.setBackgroundColor(Colours.GetEarthquakeDisplayColour(NorthQuake.getDescription().getMagnitude()));

        East.append(Html.fromHtml(BuildDisplayQuake(EastQuake)));
        East.setBackgroundColor(Colours.GetEarthquakeDisplayColour(EastQuake.getDescription().getMagnitude()));

        South.append(Html.fromHtml(BuildDisplayQuake(SouthQuake)));
        South.setBackgroundColor(Colours.GetEarthquakeDisplayColour(SouthQuake.getDescription().getMagnitude()));

        West.append(Html.fromHtml(BuildDisplayQuake(WestQuake)));
        West.setBackgroundColor(Colours.GetEarthquakeDisplayColour(WestQuake.getDescription().getMagnitude()));

        Deepest.append(Html.fromHtml(BuildDisplayQuake(DeepestQuake)));
        Deepest.setBackgroundColor(Colours.GetEarthquakeDisplayColour(DeepestQuake.getDescription().getMagnitude()));

        Strongest.append(Html.fromHtml(BuildDisplayQuake(StrongestQuake)));
        Strongest.setBackgroundColor(Colours.GetEarthquakeDisplayColour(StrongestQuake.getDescription().getMagnitude()));

        Shallowest.append(Html.fromHtml(BuildDisplayQuake(ShallowestQuake)));
        Shallowest.setBackgroundColor(Colours.GetEarthquakeDisplayColour(ShallowestQuake.getDescription().getMagnitude()));
    }

    private String BuildDisplayQuake(EarthQuake earthQuake)
    {
        String text = "<br>" + earthQuake.getLocation().getName() + " Lat: " + earthQuake.getLocation().GetLatitude() +
                " long: " + earthQuake.getLocation().GetLongtitude() + "<br>" +
                "Date: " + earthQuake.getPublishDate() + "<br>" +
                "Magnitude: " + earthQuake.getDescription().getMagnitude() + "<br>" +
                "Depth: " + earthQuake.getDescription().getDepth() + "km";

        return text;
    }

    private void SetupOnClicks()
    {
        North.setOnClickListener(v -> {
                MoveToDetailedView(NorthQuake);
        });

        East.setOnClickListener(v -> {
            MoveToDetailedView(NorthQuake);
        });

        South.setOnClickListener(v -> {
            MoveToDetailedView(NorthQuake);
        });

        West.setOnClickListener(v -> {
            MoveToDetailedView(NorthQuake);
        });
        Deepest.setOnClickListener(v -> {
            MoveToDetailedView(NorthQuake);
        });

        Shallowest.setOnClickListener(v -> {
            MoveToDetailedView(NorthQuake);
        });

        Strongest.setOnClickListener(v -> {
            MoveToDetailedView(NorthQuake);
        });

    }

    private void MoveToDetailedView(EarthQuake e)
    {
        if(e != null)
        {
            Intent intent = new Intent(getApplicationContext(), DetailedEarthQuake.class);
            intent.putExtra("earthquake", e);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("search", true);
            startActivity(intent);
        }
    }

    public void BackToMain(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}