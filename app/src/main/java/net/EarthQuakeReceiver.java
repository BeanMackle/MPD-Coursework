package net;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.EarthQuake;

//Gavin Mackle S1513818
public class EarthQuakeReceiver implements ReceiverCallBack<List<EarthQuake>> {

    private WeakReference<Activity> ref;

    private static final String VariableName = "Earthquake";

    @Override
    public void Success(List<EarthQuake> data) {
        if(data.size() > 0 && ActivityTakesEarthQuake()){
            try
            {
                ref.get().getClass().getDeclaredField(VariableName).set(this, data);

            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                Log.e("Uh Oh", e.getMessage());
            }
        }
    }

    @Override
    public void Error(Exception e) {

    }

    private Boolean ActivityTakesEarthQuake()
    {
        Class type = ref.getClass();
        ArrayList<Field> fields = GetClassProperties(type);
        return CheckIfPropertyExists(fields);
    }

    private ArrayList<Field> GetClassProperties(Class<?> activity)
    {
        return ((ArrayList<Field>) Arrays.asList(activity.getDeclaredFields()));
    }

    private Boolean CheckIfPropertyExists(ArrayList<Field> fields)
    {
        for(Field field: fields)
        {
            if(field.getName().equalsIgnoreCase(VariableName))
            {
                field.setAccessible(true);
                return true;
            }
        }
        return false;
    }
}
