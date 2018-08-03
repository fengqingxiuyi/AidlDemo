package com.fqxyi.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private IAidlBinder binder;
    // 默认未绑定service
    private boolean isBind = false;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = IAidlBinder.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind Service
        findViewById(R.id.bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conn != null) {
                    isBind = true;
                    toast("Bind Service is Success");
                    /**
                     * Android 5.0 以上会报错：IllegalArgumentException:
                     * Service Intent must be explicit，可通过下面代码实现
                     *
                     * Intent intent = new Intent("com.fqxyi.aidlservice.remote");
                     * bindService(intent, conn, Context.BIND_AUTO_CREATE);
                     */
                    Intent intent = new Intent();
                    intent.setAction("com.fqxyi.aidl.remote");
                    intent.setPackage("com.fqxyi.aidl");
                    bindService(intent, conn, Context.BIND_AUTO_CREATE);
                } else {
                    toast("ServiceConnection is null");
                }
            }
        });
        // unBind Service
        findViewById(R.id.un_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBind) {
                    if (conn != null) {
                        isBind = false;
                        toast("UnBind Service is Success");

                        unbindService(conn);
                    } else {
                        toast("ServiceConnection is null");
                    }
                } else {
                    toast("binder is null, please bind service at first!");
                }
            }
        });
        // get info
        findViewById(R.id.get_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binder == null) {
                        toast("binder is null, please bind service at first!");
                        return;
                    }
                    toast(binder.getInfo());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        // get student info
        findViewById(R.id.get_student_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binder == null) {
                        toast("binder is null, please bind service at first!");
                        return;
                    }
                    if (binder.getStudentInfo() != null) {
                        toast(binder.getStudentInfo().toString());
                    } else {
                        toast("binder.getStudentInfo() == null");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            if (conn != null) {
                isBind = false;
                unbindService(conn);
            }
        }
    }
}