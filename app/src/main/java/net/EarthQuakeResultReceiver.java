package net;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

//Gavin Mackle S1513818
public class EarthQuakeResultReceiver<T> extends ResultReceiver {


    public enum Results
    {
        SUCCESS, FAILURE
    }

    private ReceiverCallBack receiver;
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public EarthQuakeResultReceiver(Handler handler) {
        super(handler);
    }

    public void SetReceiver(ReceiverCallBack<T> receiver)
    {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData)
    {
        if(resultData != null && Results.SUCCESS.ordinal() == resultCode){
            //TODO will need to resivit when data is succesully coming back
            this.receiver.Success(resultData.getSerializable(EarthQuakeResultReceiver.Results.SUCCESS.toString()));
        }
        else
            {
                this.receiver.Error(new Exception("No Data"));
            }

    }
}
