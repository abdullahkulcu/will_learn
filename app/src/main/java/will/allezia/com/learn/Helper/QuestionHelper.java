package will.allezia.com.learn.Helper;

import will.allezia.com.learn.Pages.SettingsActivity;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by abdullah on 20.10.2017.
 */

public class QuestionHelper {
    private  ArrayList<String> wordList = new ArrayList<>();
    private Random random = new Random();
    private ArrayList<String> textsForShow = new ArrayList<>();
    private Context mContext;
    private final String BANNEDS = "banned_words";
    private String[] willBlockWords;
    private SweetAlertDialog dialog;
    private DateFormat formatter = new SimpleDateFormat("HH:mm");
    private Date quiteStartHour,quiteStopHour;
    private Date currentTime;
    public QuestionHelper(Context context) {
        mContext =  context;
        fillArray();
    }

    private void fillArray() {
        wordList.add("abbreviated");
        wordList.add("appliances");
        wordList.add("circuit");
        wordList.add("infrared");
        wordList.add("identifiable");
        wordList.add("influence");
        wordList.add("represents");
        wordList.add("entities");
        wordList.add("Environment");
        wordList.add("visual");
        wordList.add("accommodates");
        wordList.add("allocate");
        wordList.add("spans small");
        wordList.add("proven");
        wordList.add("afford");
        wordList.add("accuse");
        wordList.add("departure");
        wordList.add("declares");
        wordList.add("cluster");
        wordList.add("compass");
        wordList.add("warn");
        wordList.add("adore");
        wordList.add("accountant");
        wordList.add("afraid");
        wordList.add("accident");
        wordList.add("against");
        wordList.add("through");
        wordList.add("grant");
        wordList.add("bindings");
        wordList.add("evidence");
        wordList.add("executive");
        wordList.add("disease");
        wordList.add("discussion");
        wordList.add("decade");
        wordList.add("debate");
        wordList.add("indicate");
        wordList.add("individual");
        wordList.add("institution");
        wordList.add("manufacture");
        wordList.add("malice");
        wordList.add("nail");
        wordList.add("negligence");
        wordList.add("observe");
        wordList.add("obvious");
        wordList.add("occupy");
        wordList.add("paraphrase");
        wordList.add("quotation");
        wordList.add("satisfaction");
        wordList.add("vacation");
        wordList.add("verdict");
        controlForbidden();
    }

    public ArrayList<String> getData(){
        System.out.println(wordList);
        int randWordLoop = 0;
        while (randWordLoop == 0) {
            int randomInt = random.nextInt(wordList.size());
            if (!textsForShow.contains(wordList.get(randomInt))) {
                textsForShow.add(wordList.get(randomInt));
                if (textsForShow.size() == 4)
                    randWordLoop = 1;
            }
        }
        return textsForShow;
    }

    public void forbiddenWord(String word){
        if(wordList.size() > 10){
            PreferenceHelper helper = new PreferenceHelper(mContext);
            String banneds = helper.getStringData(BANNEDS);
            if (banneds == null)
                banneds = word;
            else
                banneds += "," + word;
            helper.setData(BANNEDS, banneds);
        }else{
            dialog = new SweetAlertDialog(mContext,SweetAlertDialog.NORMAL_TYPE)
                    .setContentText("You have passed enough words to delete it.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                          dialog.dismiss();
                        }
                    });

        }
    }
    private void controlForbidden(){

        String forbiddenPreference = new PreferenceHelper(mContext).getStringData(BANNEDS);
        if (forbiddenPreference != null) {
            willBlockWords = forbiddenPreference.split(",");
            for (String wrongWord : willBlockWords) {
                wordList.remove(wrongWord);
            }
        }
    }
    public boolean controlQuiteTime(){
        boolean controlResult =false;
        try {
            boolean quite = new PreferenceHelper(mContext).getBooleanData("quite_enabled");
            if(quite) {
                String[] hoursArray = new PreferenceHelper(mContext).getStringData("quite_hours").split("-");
                quiteStartHour = formatter.parse(hoursArray[0]);
                quiteStopHour = formatter.parse(hoursArray[1]);
                currentTime = Calendar.getInstance().getTime();
                int currentHour = currentTime.getHours();
                int currentMinute = currentTime.getMinutes();
                if(currentHour > quiteStartHour.getHours() && currentHour < quiteStopHour.getHours() ){
                    if(currentMinute > quiteStartHour.getMinutes())
                        controlResult = true;
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(SettingsActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
         return controlResult;
    }

}
