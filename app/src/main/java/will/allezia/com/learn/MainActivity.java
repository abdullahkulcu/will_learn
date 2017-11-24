package will.allezia.com.learn;

import will.allezia.com.learn.Pages.QuestionPage;
import will.allezia.com.learn.Pages.SettingsActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button profile ,startTest;
    private Intent mIntent;
    private Context mContext =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,MainService.class));
        profile = findViewById(R.id.profile);
        startTest = findViewById(R.id.startTest);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(mContext, SettingsActivity.class);
                startActivity(mIntent);
            }
        });

        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(mContext, QuestionPage.class);
                mIntent.putExtra("receiver",false);
                startActivity(mIntent);
            }
        });
    }
}
