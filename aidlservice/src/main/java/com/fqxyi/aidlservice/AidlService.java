package com.fqxyi.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by qingfeng on 2017/7/27.
 */

public class AidlService extends Service {

    private Student student;

    private IAidlBinder.Stub serviceBinder = new IAidlBinder.Stub(){
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getInfo() throws RemoteException {
            return "I'm a Service";
        }

        @Override
        public Student getStudentInfo() throws RemoteException {
            return student;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        student = new Student();
        student.setName("风清袖一");
        student.setAge(18);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }
}
