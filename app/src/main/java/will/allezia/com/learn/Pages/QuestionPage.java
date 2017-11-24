package will.allezia.com.learn.Pages;

import will.allezia.com.learn.Helper.ApiHelper;
import will.allezia.com.learn.Helper.Connection;
import will.allezia.com.learn.Helper.PreferenceHelper;
import will.allezia.com.learn.Helper.QuestionHelper;

import will.allezia.com.learn.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class QuestionPage extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private CircularProgressButton answer1, answer2, answer3, answer4;
    private TextView question;
    private Button startAudio, wrongWord;
    private QuestionHelper helper;
    private ArrayList<String> mWords = new ArrayList<>();
    private AsyncHttpClient client;
    private String url;
    private String translated = null;
    private ArrayList<String> convertedWords = new ArrayList<>();
    private ArrayList<String> buttonList = new ArrayList<>();
    private Random random = new Random();
    private String realWord, trueAnswer;
    private TextToSpeech tts;
    private boolean receiver;
    private SweetAlertDialog dialog;

    @Override
    protected void onStop() {
        super.onStop();
        tts.stop();
        tts.shutdown();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
            tts.setLanguage(Locale.ENGLISH);
    }

    public void speakOut() {
        tts.speak(realWord, TextToSpeech.QUEUE_FLUSH, null);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean ring = new PreferenceHelper(this).getBooleanData("isRing");
        if (Connection.isOnline(this) && !ring) {
            receiver = getIntent().getBooleanExtra("receiver", false);
            helper = new QuestionHelper(this);
            tts = new TextToSpeech(this, this);
            makeFullScreen();
            getWords();
        }
    }

    @Override
    public void onBackPressed() {
        if (receiver)
            return;
        super.onBackPressed();

    }

    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private void getWords() {
        mWords = helper.getData();
        translateReal();
        translateKey();
    }

    private void translateReal() {
        client = new AsyncHttpClient();
        url = ApiHelper.getFullUrl(mWords.get(0));
        client.get(this, url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    if (!responseString.isEmpty()) {
                        trueAnswer = ApiHelper.getTranslatedWord(responseString);
                    }
                }
            }
        });
    }

    private void translateKey() {
        client = new AsyncHttpClient();

        for (String word : mWords) {
            url = ApiHelper.getFullUrl(word);
            client.get(this, url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (statusCode == 200) {
                        if (!responseString.isEmpty()) {
                            translated = ApiHelper.getTranslatedWord(responseString);
                            convertedWords.add(translated);
                            if (convertedWords.size() == 4)
                                setScreen();
                        }
                    }
                }
            });
        }
    }


    private void setScreen() {
        realWord = mWords.get(0);
        setContentView(R.layout.activity_question_page);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        question = findViewById(R.id.qeustionText);
        startAudio = findViewById(R.id.startAudio);
        wrongWord = findViewById(R.id.wrongWord);


        question.setText(realWord);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);

        buttonSetText();

        startAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut();
            }
        });
        wrongWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("This word removed from list, it will not come again ")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                helper.forbiddenWord(realWord);
                                destroyScreen();
                                dialog.dismiss();
                            }
                        });
                dialog.show();

            }
        });

    }

    private void buttonSetText() {
        int loop = 0;
        while (loop == 0) {
            int randomButtonValue = random.nextInt(convertedWords.size());
            if (!buttonList.contains(convertedWords.get(randomButtonValue))) {
                buttonList.add(convertedWords.get(randomButtonValue));
                convertedWords.remove(randomButtonValue);
            }
            if (convertedWords.size() == 0)
                loop = 1;
        }
        answer1.setText(buttonList.get(0));
        answer1.setIdleText(buttonList.get(0));
        answer2.setText(buttonList.get(1));
        answer2.setIdleText(buttonList.get(1));
        answer3.setText(buttonList.get(2));
        answer3.setIdleText(buttonList.get(2));
        answer4.setText(buttonList.get(3));
        answer4.setIdleText(buttonList.get(3));
    }

    @Override
    public void onClick(View view) {
        CircularProgressButton answerButton = (CircularProgressButton) view;
        answerButton.setProgress(50);
        answerButton.setProgress(100);
        String answer = answerButton.getIdleText();
        System.out.println(answer);
        if (answer.equals(trueAnswer)) {
            destroyScreen();
        } else {
            answerButton.setProgress(-1);
            answerButton.setErrorText("Wrong Answer");
        }
    }

    private void destroyScreen() {
        if (receiver)
            finish();
        else {
            Intent cIntent = getIntent();
            overridePendingTransition(0, 0);
            cIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(cIntent);
        }
    }

}
