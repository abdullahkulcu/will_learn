package will.allezia.com.learn.Pages;

import will.allezia.com.learn.Helper.PreferenceHelper;
import will.allezia.com.learn.Helper.QuestionHelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.ArrayList;


public class SettingsActivity extends Activity {
    private LinearTimePickerDialog timePicker;
    private TextView quiteView;
    private ArrayList<Integer> time = new ArrayList<>();
    private Button quiteStart, statusView;
    private QuestionHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(will.allezia.com.learn.R.layout.activity_settings);
        helper = new QuestionHelper(this);
        quiteView = findViewById(will.allezia.com.learn.R.id.quiteHoursView);
        quiteStart = findViewById(will.allezia.com.learn.R.id.startQuite);
        statusView = findViewById(will.allezia.com.learn.R.id.statusView);
        controlViews();
        quiteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time.size() < 4)
                    showTimePicker();
                else {
                    deleteTimeArray();
                    showTimePicker();
                }
            }
        });

        quiteStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quiteStart.getText().toString().equals("Quite Hours Start")) {
                    if (time.size() == 4) {
                        deleteTimeArray();
                        String hoursView = quiteView.getText().toString();
                        new PreferenceHelper(view.getContext()).setData("quite_enabled", true);
                        new PreferenceHelper(view.getContext()).setData("quite_hours", hoursView);
                        quiteStart.setText("Quite Hours Stop");
                        quiteView.setEnabled(false);
                        quiteView.setClickable(false);
                    }else if(time.size()==2){
                        showTimePicker();
                    }else {
                        Toast.makeText(getApplicationContext(), "Please select the time interval again.", Toast.LENGTH_LONG).show();
                        showTimePicker();
                    }
                }else{
                    new PreferenceHelper(view.getContext()).setData("quite_enabled", false);
                    quiteStart.setText("Quite Hours Start");
                    quiteView.setText("00:00-00:00");
                    quiteView.setEnabled(true);
                    quiteView.setClickable(true);
                }
            }
        });



    }

    private void showTimePicker() {
        timePicker = LinearTimePickerDialog.Builder.with(this)
                .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                    @Override
                    public void onPositive(DialogInterface dialog, int hour, int minutes) {
                        if (time.size() < 4) {
                            time.add(hour);
                            time.add(minutes);
                        }
                        System.out.println(hour);
                        System.out.println(minutes);
                        dialog.dismiss();
                        changeTimeView();
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        timePicker.show();
    }

    private void changeTimeView() {
        System.out.println(time);
        switch (time.size()) {
            case 2:
                quiteView.setText(time.get(0) + ":" + time.get(1) + "-00:00");
                break;
            case 4:
                if (time.get(0) < time.get(2))
                    quiteView.setText(time.get(0) + ":" + time.get(1) + "-" + time.get(2) + ":" + time.get(3));
                else if (time.get(0) == time.get(2)) {
                    if (time.get(1) < time.get(3))
                        quiteView.setText(time.get(0) + ":" + time.get(1) + "-" + time.get(2) + ":" + time.get(3));
                    else
                        quiteView.setText(time.get(0) + ":" + time.get(3) + "-" + time.get(2) + ":" + time.get(2));
                } else {
                    quiteView.setText(time.get(2) + ":" + time.get(3) + "-" + time.get(0) + ":" + time.get(1));
                }
                break;
            default:
                break;
        }
    }
    private void deleteTimeArray(){
        for (int i = 0; i <= 3; i++)
            time.remove(0);
    }

    private void controlViews(){
        boolean quiteWorkingControl= new PreferenceHelper(this).getBooleanData("quite_enabled");
        if (quiteWorkingControl){
            String quiteTimeforView = new PreferenceHelper(this).getStringData("quite_hours");
            quiteView.setText(quiteTimeforView);
            quiteView.setEnabled(false);
            quiteView.setClickable(false);

            quiteStart.setText("Quite Hours Stop");
            boolean quiteIsNowWorking = helper.controlQuiteTime();
            if(quiteIsNowWorking){
                statusView.setText("Passive - In Quite Hours Time");
                statusView.setBackgroundColor(getResources().getColor(will.allezia.com.learn.R.color.status_passive));
            }
        }
    }
}
