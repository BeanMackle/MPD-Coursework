package net;

//Gavin Mackle S1513818
public interface ReceiverCallBack<T> {
    public void Success(T Data);
    public void Error(Exception e);
}
