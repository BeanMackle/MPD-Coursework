package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.EarthQuakeListReceiver;
import net.EarthQuakeService;

import java.util.ArrayList;

import Model.EarthQuake;
import Model.EarthQuakeAdapter;

//Gavin Mackle S1513818
public class MainActivity extends AppCompatActivity
{
    private ListView list;

    private EarthQuakeAdapter adapter;
    public ArrayList<EarthQuake> Earthquakes;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        EarthQuakeService.StartServiceLoadAll(this, new EarthQuakeListReceiver(this));

    }

    public void Search(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void populateListView()
    {
        list = findViewById(R.id.list);
        adapter = new EarthQuakeAdapter(this, Earthquakes);
        Log.e("Earthquake count", String.valueOf(Earthquakes.size()));
        ListClickListener();
        list.setAdapter(adapter);
    }

    public void showData( )
    {
        MainActivity.this.runOnUiThread(() -> {
            populateListView();
        });
    }

    public void ListClickListener()
    {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake earthquake = adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), DetailedEarthQuake.class);
                intent.putExtra("earthquake", earthquake);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }


}