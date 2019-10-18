package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

public class Reminder extends AppCompatActivity implements View.OnClickListener{


    private int notificationId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //set onclick listener

        findViewById(R.id.setButton).setOnClickListener(this);
        findViewById(R.id.cancelButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker =findViewById(R.id.timePicker);

        //SetNotification and Text

        Intent intent = new Intent(Reminder.this, AlarmReceiver.class);
        intent.putExtra("NotificationId", notificationId);
        intent.putExtra("Todo",editText.getText().toString());


        PendingIntent alarmIntent = PendingIntent.getBroadcast(Reminder.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);





        switch (view.getId()) {
            case R.id.setButton:
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();

                //Create Time
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,hour);
                startTime.set(Calendar.MINUTE,min);
                startTime.set(Calendar.SECOND,0);
                long alarmSartTime = startTime.getTimeInMillis();

                //Set Alarm
                alarm.set(AlarmManager.RTC_WAKEUP, alarmSartTime, alarmIntent);

                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

                break;
                case R.id.cancelButton:
                    alarm.cancel(alarmIntent);
                    Toast.makeText(this, "Canceled!", Toast.LENGTH_SHORT).show();

                    break;

        }

    }
}
