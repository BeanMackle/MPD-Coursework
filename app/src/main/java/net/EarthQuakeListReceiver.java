package net;

import android.app.Activity;
import android.util.Log;

import org.me.gcu.equakestartercode.MainActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.EarthQuake;

//Gavin Mackle S1513818
public class EarthQuakeListReceiver implements ReceiverCallBack<ArrayList<EarthQuake>> {

    private WeakReference<MainActivity> ref;

    private static final String ListVariableName = "Earthquakes";


    public EarthQuakeListReceiver(MainActivity a)
    {
        ref = new WeakReference<MainActivity>(a);
    }

    @Override
    public void Success(ArrayList<EarthQuake> data) {

        if(data.size() > 0){
            ref.get().Earthquakes = data;
            ref.get().showData();
        }
    }

    @Override
    public void Error(Exception e)
    {
        Log.e("ERROR", e.getMessage());
    }
}
