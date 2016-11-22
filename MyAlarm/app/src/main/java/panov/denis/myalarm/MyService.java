package panov.denis.myalarm;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyService extends Service {

    ExecutorService es;

    public void onCreate() {
        super.onCreate();
    }
    public void onDestroy() {
        super.onDestroy();
    }
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        es = Executors.newFixedThreadPool(1);
        MyRun mr = new MyRun(title,text);
        es.execute(mr);
        mr.run();

        Toast.makeText(getApplicationContext(),"You`re right!",Toast.LENGTH_LONG).show();
        stopSelf();
        onDestroy();
        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        String title, text;

        private MyRun(String title, String text) {
            this.title = title;
            this.text = text;
        }

        public void run() {
            Intent notificationIntent = new Intent(getApplicationContext(),ShowActivity.class);
            notificationIntent.putExtra("text",text);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Remember to:")
                            .setContentText(title);

            builder.setContentIntent(contentIntent);
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_ALL;
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);
            stopSelf();
        }
    }
}
