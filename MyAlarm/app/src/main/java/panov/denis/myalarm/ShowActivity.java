package panov.denis.myalarm;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent dataIntent = getIntent();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(0);

        TextView noteText = (TextView) findViewById(R.id.noteText);
        noteText.setTextColor(Color.BLUE);
        noteText.setTextSize(20);
        noteText.setText(dataIntent.getStringExtra("text"));
    }
}
