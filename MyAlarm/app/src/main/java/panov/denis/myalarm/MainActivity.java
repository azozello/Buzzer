package panov.denis.myalarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    int DIALOG_TIME = 123;
    int hours = 0, minutes = 0;

    Button add, del;
    AlarmManager alarmManager;
    EditText editText, editTitle;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (Button) findViewById(R.id.buttonAdd);
        del = (Button) findViewById(R.id.buttonDelete);
        editText = (EditText) findViewById(R.id.editNoteText);
        editTitle  = (EditText) findViewById(R.id.editNoteTitle);

        add.setOnClickListener(this);
        del.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAdd:
                showDialog(DIALOG_TIME);
                break;
            case R.id.buttonDelete:
                removeNotification();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME){
            TimePickerDialog tpd = new TimePickerDialog(this,timeSetListener,hours,minutes,true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hours = hourOfDay;
            minutes = minute;
            Date startDate = new Date(System.currentTimeMillis());

            long startTime = startDate.getHours()*60+startDate.getMinutes();
            long finTime = hourOfDay*60+minute;
            long wait = finTime - startTime;
            wait = wait*60000;
            Toast.makeText(getApplicationContext(),""+wait,Toast.LENGTH_SHORT).show();
            if (wait>0) addNotification(wait);
            else addNotification(5000);
        }
    };
    private void addNotification(long wait){

        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("title",editTitle.getText().toString());
        intent.putExtra("text",editText.getText().toString());

        pendingIntent = PendingIntent.getService(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+wait,pendingIntent);
    }
    private void removeNotification(){
        alarmManager.cancel(pendingIntent);
    }
}
