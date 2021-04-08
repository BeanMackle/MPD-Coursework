package net;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import Model.EarthParserHelper;
import Model.EarthQuake;


//Gavin Mackle S1513818
public class EarthQuakeService extends IntentService {

    private static final URL url = buildURL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");

    private Context context = this;

    enum Service
    {
        RECEIVER, FILE, ACTION
    }

    enum Action
    {
        LOADALL, DATESEARCH
    }
    enum SearchDate
    {
        DATE1, DATE2
    }

    public EarthQuakeService(){
        super("service");
    }

    private static URL buildURL(String url)
    {
        try {
            return new URL(url);
        }
        catch(MalformedURLException e)
        {
            Log.e("Something Went Wrong!", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       // Bundle extras = intent.getExtras();

        ResultReceiver resultReceiver = intent.getParcelableExtra(Service.RECEIVER.name());

        String xmlData = GetXMLData();

        CheckDataAccuracy(xmlData);

       ArrayList<EarthQuake> earthQuakes =  ParseData(resultReceiver);

       if(earthQuakes != null && earthQuakes.size() > 0)
       {
          Bundle bundle =  intent.getExtras();
          String action = bundle.getString(Service.ACTION.name());

           if(action.equals(Action.DATESEARCH.name()))
           {
               Date startDate = (Date) bundle.get(SearchDate.DATE1.name());
               Date endDate = (Date) bundle.get(SearchDate.DATE2.name());
               earthQuakes = SortByDate(earthQuakes, startDate, endDate);
           }

           SendEarthQuakes(earthQuakes, resultReceiver);
       }
    }

    private String GetXMLData()
    {
        try {
            URLConnection result = url.openConnection();
            String xml =  RemoveXmlHeader(new BufferedReader(new InputStreamReader(result.getInputStream())));

            return xml;
        }
        catch(IOException e)
        {
            Log.e("Failed to Get XML Data", e.getMessage());
            return null;
        }
    }

    private void SaveData(String XmlData)
    {
        try {
            OutputStreamWriter fileWriter = new OutputStreamWriter(context.openFileOutput(Service.FILE.name(), Context.MODE_PRIVATE));
            fileWriter.write(XmlData);
            fileWriter.close();
        }
        catch(IOException e)
        {
            Log.e("Saving Data Error", "IOException when saving the data!");
        }
    }

    private void CheckDataAccuracy(String newData)
    {

        String savedData = GetSavedData();

        if(savedData == null)
        {
            SaveData(newData);
        }
        else
            {
                CompareData(newData, savedData);
            }
    }



    private void CompareData(String newData, String savedData)
    {

        if(!(newData.equalsIgnoreCase(savedData)))
        {
            SaveData(newData);
        }
    }

    private String GetSavedData() {
        try
        {
            InputStream inputStream = context.openFileInput(Service.FILE.name());

            if(inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String data = ReadDataFromFile(bufferedReader);

                inputStream.close();

              return data;

            }
            return null;
        }
        catch(IOException e)
        {
            Log.e("Saved Data Successfully", "IO exception when retrieving saved data");
            return null;
        }
    }

    private String ReadDataFromFile(BufferedReader reader)
    {
        StringBuilder builder = new StringBuilder();
        String foundString = "";

        try {
            while ((foundString = reader.readLine()) != null) {
                builder.append(foundString);
            }
            return builder.toString();
        }
        catch(IOException e)
        {
            return null;
        }
    }

    private String RemoveXmlHeader(BufferedReader reader)
    {
        String xml = "";
        String inputLine = "";

        try
        {
            while((inputLine = reader.readLine()) != null)
            {
                xml += inputLine;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return xml;

    }

    private XmlParserCreator ReturnParser() {
        XmlParserCreator parserCreator = new XmlParserCreator() {
            @Override
            public XmlPullParser createParser() {
                try {
                    return XmlPullParserFactory.newInstance().newPullParser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        return parserCreator;
    }

    private String StripEndTags(String data)
    {
        if(data.contains("</rss>") && data.contains("</channel>"))
        {
            int pos = data.indexOf("</channel>");

            return data.substring(0, pos);
        }

        return data;
    }


    private ArrayList<EarthQuake> ParseData(ResultReceiver receiver)
    {

        String xmlData = GetSavedData();
        try {

            GsonXml xmlParser = new GsonXmlBuilder().setXmlParserCreator(ReturnParser()).setSameNameLists(true).create();

            String[] data = xmlData.split("<item>");

            data[data.length - 1] = StripEndTags(data[data.length - 1]);

            ArrayList<EarthQuake> earthquakes = new ArrayList<EarthQuake>();

            for(int i = 1; i < data.length; i++)
            {
                EarthParserHelper parsed = xmlParser.fromXml("<item>" + data[i], EarthParserHelper.class);


                EarthQuake eq = parsed.MapToEarthQuake();

                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, -50);

                if(!eq.getPublishDate().before(c.getTime()))
                {
                    earthquakes.add(eq);
                }

            }
               return earthquakes;


        } catch(Exception e) {
            receiver.send(EarthQuakeResultReceiver.Results.FAILURE.ordinal(), null);
            return null;
        }
    }

    private ArrayList<EarthQuake> SortByDate(ArrayList<EarthQuake> earthQuakes, Date startDate, Date endDate)
    {
        ArrayList<EarthQuake> sortedQuakes = new ArrayList<EarthQuake>();
        for(EarthQuake e: earthQuakes)
        {
            if(CheckDate(e.getPublishDate(), startDate, endDate))
            {
                sortedQuakes.add(e);
            }
        }

        return sortedQuakes;
    }
    private Date removeTimeFromDate(Date date) {
        Date res;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

    private boolean CheckDate(Date earthQuakeDate, Date startDate, Date endDate)
    {

            Date eDate = removeTimeFromDate(earthQuakeDate);

        if(!eDate.before(removeTimeFromDate(startDate)) && !eDate.after(removeTimeFromDate(endDate)))
        {
            return true;
        }
        else
            {
                return false;
            }
    }

    private void SendEarthQuakes(ArrayList<EarthQuake> earthQuakes, ResultReceiver receiver)
    {
        Bundle bundle = new Bundle();

        bundle.putSerializable(EarthQuakeResultReceiver.Results.SUCCESS.toString(), earthQuakes);

        receiver.send(EarthQuakeResultReceiver.Results.SUCCESS.ordinal(), bundle);
    }


    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    public static void StartServiceLoadAll(Context context, ReceiverCallBack receiverCallBack)
    {
        EarthQuakeResultReceiver earthQuakeResultReceiver = new EarthQuakeResultReceiver(new Handler(context.getMainLooper()));
        earthQuakeResultReceiver.SetReceiver(receiverCallBack);


        Intent intent = new Intent(context, EarthQuakeService.class);
        intent.putExtra(Service.RECEIVER.name(), earthQuakeResultReceiver);
        intent.putExtra(Service.ACTION.name(), Action.LOADALL.name());
        context.startService(intent);
    }

    public static void StartServiceDateSearch(Context context, ReceiverCallBack receiverCallBack, Date startDate, Date endDate)
    {
        EarthQuakeResultReceiver earthQuakeResultReceiver = new EarthQuakeResultReceiver(new Handler(context.getMainLooper()));
        earthQuakeResultReceiver.SetReceiver(receiverCallBack);

        Intent intent = new Intent(context, EarthQuakeService.class);
        intent.putExtra(Service.RECEIVER.name(), earthQuakeResultReceiver);
        intent.putExtra(Service.ACTION.name(), Action.DATESEARCH.name());
        intent.putExtra(SearchDate.DATE1.name(), startDate);
        intent.putExtra(SearchDate.DATE2.name(), endDate);
        context.startService(intent);
    }
}