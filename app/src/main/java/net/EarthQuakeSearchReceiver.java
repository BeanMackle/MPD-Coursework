package net;

import android.util.Log;

import org.me.gcu.equakestartercode.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Model.EarthQuake;

//Gavin Mackle S1513818
public class EarthQuakeSearchReceiver implements ReceiverCallBack<ArrayList<EarthQuake>> {

    private WeakReference<SearchActivity> ref;

    public EarthQuakeSearchReceiver(SearchActivity s)
    {
        ref = new WeakReference<SearchActivity>(s);
    }

    @Override
    public void Success(ArrayList<EarthQuake> data)
    {
        if(data.size() > 0)
        {
            ref.get().EarthQuakes = data;
            ref.get().sortQuakes();
        }
    }

    @Override
    public void Error(Exception e)
    {
        Log.e("Search Error", e.getMessage());
    }
}
