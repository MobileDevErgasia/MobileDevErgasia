package com.example.mobiledevergasia;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

/**
 * TODO
 */
public class    VoiceRecordActivity extends AppCompatActivity implements SaveDialog.SaveDialogListener,ConfirmDeleteDialog.ConfirmDeleteListener {


    private ImageButton recordButton,stopPlayingButton,settingsButton;
    private File folder,temporaryFile,finalFile;

    private CustomGridHandler customGridHandler;
    private Recorder recorder;

    private String filename;

    private Chronometer chronometer;
    private Toolbar myToolbar;
    private String recordingDurationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_record);
        myToolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},PackageManager.PERMISSION_GRANTED);


        chronometer=findViewById(R.id.simpleChronometer);


        //αρικοποιηση
        recordButton =findViewById(R.id.recordButton);
        stopPlayingButton=findViewById(R.id.stopPlayingButton);
        settingsButton=findViewById(R.id.settingsButton);


        recorder=new Recorder();

        recordButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               buttonClick();
           }
        });

        stopPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customGridHandler.stop();
                stopPlayingButton.setVisibility(View.GONE);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

       chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
           @Override
           public void onChronometerTick(Chronometer chronometer) {
               if (chronometer.getText().toString().endsWith(recordingDurationText)){//το endswith("15") σημαινει
                   stopClock();                                     //πχ οταν το χρονομετρο γραψει "00:15" θα σταματησει η ηχογραφηση
                   stopRecording();
               }

           }
       });

        folder=new File(getExternalFilesDir(null) + "/MyRecording/");
        if (!folder.exists()){
            folder.mkdir(); //αν δεν υπαρχει το δημιουργει
        }

        //κλαση που χειριζεται τα αντικειμενα της λιστας με της ηχογραφησεις
        customGridHandler = new CustomGridHandler(findViewById(R.id.voice_record_activity),getApplicationContext(),this);
        customGridHandler.setCustomGridListener(new CustomGridHandler.CustomGridListener() {
            @Override
            public void onStartPlaying() {
                showStopButton();
            }

            @Override
            public void onStopPlaying() {
                if(!customGridHandler.areItemsPlaying()){
                    hideStopButton();
                }
            }

            @Override
            public void onDeletePressed() {
                showConfirmDeleteDialog();
            }
        });
    }

    private void showConfirmDeleteDialog(){
        ConfirmDeleteDialog confirmDeleteDialog=new ConfirmDeleteDialog();
        confirmDeleteDialog.show(getSupportFragmentManager(),"confirmDeleteDialog");
    }

    /**
     * Ανοιγει το Setting.class,καλειται απο το
     * settingsButton
     */
    private void openSettings(){
        hideStopButton();
        customGridHandler.cancel();


        Intent intent= new Intent(getApplicationContext(),Settings.class);
        String currentLanguage=Locale.getDefault().getLanguage();
        intent.putExtra("language",currentLanguage);
        startActivity(intent);
    }

    /**
     * Την πρωτη φορα που θα πατηθει το κουμπι ξεκιναει η ηχογραφηση
     * Την δευτερη σταματαει και αποθηκευεται.
     */
    private void buttonClick(){
        if (!recorder.isRecording()){
            customGridHandler.stop();
            startRecording();
        }else{
            stopRecording();
        }

    }

    /**
     * Ξεκιναει το χρονομετρο
     */
    private void startClock(){
        chronometer.setVisibility(View.VISIBLE);

        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.start();
    }

    /**
     * Σταματαει το χρονομετρο
     */
    private void stopClock(){
        chronometer.setVisibility(View.INVISIBLE);

        chronometer.setText("00:00");

        chronometer.stop();
    }

    /**
     * Ξεκιναει η ηχογραφηση
     * Οταν ξεκινησει η ηχογραφηση θελουμε να σταματησουν
     * να παιζουν τυχον αλλες ηχογραφησεις
     * Δινεται ενα dummy ονομα στο αρχειο το οποιο χρησιμοποιειται για την αποθηκευση
     * Στην συνεχεια αυτο μπορει να αλλαξει απο τον χρηστη
     * Χρησιμοποιειται η startRecording(File temporaryFile) του Recorder
     */
    private void startRecording(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=0){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},0);
            return;
        }
        startClock();
        recordButton.setImageResource(R.drawable.stop_recording_image);


        Toast.makeText(VoiceRecordActivity.this,R.string.started_recording, Toast.LENGTH_SHORT).show();

        String dummy = "dummy";
        filename=folder+ "/" + dummy + ".mp3"; //δινεται ενα dummy μοναδικο ονομα το οποιο στην συνεχεια αλλαζει.

        temporaryFile=new File(filename );

        recorder.startRecording(temporaryFile);
    }

    /**
     * Σταματαει η ηχογραφηση και καλειται η showSaveDialog για να δωσει ο χρηστης
     * το ονομα που επιθυμει ή να ακυρωσει την αποθηκευση της
     * Χρησιμοποιειται η stopRecording() του Recorder
     */
    private void stopRecording(){
        stopClock();

        recordButton.setImageResource(R.drawable.start_recording_image);
        Toast.makeText(VoiceRecordActivity.this, R.string.stopped_recording , Toast.LENGTH_SHORT).show();

        recorder.stopRecording();

        showSaveDialog();
    }

    /**
     * Εμφανιζει το dialog για να δωθει ονομα στην ηχογραφηση
     */
    private void showSaveDialog(){
        SaveDialog saveDialog=new SaveDialog();
        saveDialog.show(getSupportFragmentManager(), "saveDialog");
    }

    @Override
    public void deleteFiles() {
        customGridHandler.delete();
    }

    @Override
    public void deleteCancelled() {
        customGridHandler.cancel();
    }

    /**
     * Αν πατηθει το cancel στο SaveDialog ακυρωνεται η ηχογραφηση
     */
    @Override
    public void cancelled() {
        temporaryFile.delete();
    }


    private String getFilename(File file){
        String t=file.getName().replace(".mp3","");
        return t;
    }

    /**
     * Αλλαζει το ονομα της ηχογραφησης στο επιθυμητο
     * και το προσθετει στο GridView
     * @param n το επιθυμητο ονομα
     */
    @Override
    public void saveFileAs(String n) {
        finalFile=new File(folder + "/" + n + ".mp3");
        temporaryFile.renameTo(finalFile);
        String name=getFilename(finalFile);
        customGridHandler.addToList(finalFile.getPath(),name); //προσθηκη ηχογραφησης στην λιστα αν αλλαξει το name
    }

    private void renameFile(String path,String newPath){
        File newFile=new File(path);
        newFile.renameTo(new File(newPath));
    }

    /**
     * Φορτωνει τα settings,καλειται απτην onResume η οποια γινεται override
     * Πιο συγκεκριμενα,την μεγιστη διαρκεια ηχογραφησης
     * την γλωσσα και την αυτοματη επαναληψη.
     */
    private void loadSettings(){

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String tempKey=sharedPreferences.getString("maxRecordDuration","10");
        recordingDurationText=tempKey;
        switch(tempKey){
            case "1" :
                recordingDurationText="05";
                break;
            case "2" :
                recordingDurationText="10";
                break;
            case "3" :
                recordingDurationText="15";
                break;
            case "4" :
                recordingDurationText="20";
                break;
        }

        boolean toAutoLoop=sharedPreferences.getBoolean("AutoLoop",false);
        customGridHandler.setAutoLooping(toAutoLoop);

        String language=sharedPreferences.getString("selectLanguage","el");

        switch (language){
            case "el": setLocale(this,"el"); break;
            case "en": setLocale(this,"en"); break;
        }
    }

    /**
     *  Αλλαζει την γλωσσα των στοιχειων
     * @param activity το VoiceRecordActivity
     * @param languageCode ο κωδικος της γλωσσας, el για ελληνικα,en για αγγλικα
     */
    private static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * Οταν σταματαει η εφαρμογη,πχ παει στο background,ελευθερονεται ο mediaRecorder
     * μεσω της recorded.clear() και ο mediaPlayer μεσω της
     * customListHandler.clear() για να μην σπαταλιζονται ποροι
     */
    @Override
    public void onStop() {
        if(customGridHandler.isToCheck()){
            customGridHandler.backPressed();
        }

        if(recorder.isRecording()){
            recorder.clear();
            customGridHandler.stop();
            stopClock();
            recordButton.setImageResource(R.drawable.start_recording_image);
            temporaryFile.delete();
        }
        super.onStop();
    }
    @Override
    protected void onRestart() {
        customGridHandler.cancel();
        super.onRestart();
    }

    /**
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        CustomItem item=intent.getParcelableExtra("customItem");
        int index=intent.getIntExtra("index",0);

        if(intent.hasExtra("finished")){
            String previousName=intent.getStringExtra("previousName");
            if (previousName!=null){
                String path=item.getPath();
                String newPath=folder.getAbsolutePath() + "/" + item.getName() + ".mp3";
                renameFile(path,newPath);
                item.setPath(newPath);
            }
            customizeItem(item,index,previousName);
        }else if(intent.hasExtra("cancelled")){
            //DoNothing
        }
        else if (intent.hasExtra("Reset")){
            resetItem(item,index);
        }
        super.onNewIntent(intent);
    }

    /**
     * Καλει την loadSettings
     */
    @Override
    public void onResume(){
        loadSettings();
        super.onResume();
    }

    /**
     * Ελεγχεται αν ειναι ενεργοποιημενη η επιλογη πολλαπλων στοιχειων
     *      Αν ειναι καλειται η backPressed() του CustomListHandler
     */
    @Override
    public void onBackPressed() {
        if(customGridHandler.isToCheck()){
            customGridHandler.backPressed();
        }else{
            super.onBackPressed();
        }
    }

    private void resetItem(CustomItem item,int index){
        item.reset();
        customGridHandler.reset(index,item);
    }

    private void customizeItem(CustomItem item,int index,String previousName){

        customGridHandler.replace(index,item,previousName);
    }

    /**
     * Κρυβει το stopButton
     */
    private void hideStopButton(){
        stopPlayingButton.setVisibility(View.GONE);
    }

    /**
     * Εμφανιζει το stopButton
     */
    private void showStopButton() {
        stopPlayingButton.setVisibility(View.VISIBLE);
    }
}