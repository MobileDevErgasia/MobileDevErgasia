package com.example.mobiledevergasia;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class VoiceRecordActivity extends AppCompatActivity {

    private Button button;
    private MediaRecorder mediaRecorder;
    private File folder;
    private EditText textView;

    private CustomListHandler customListHandler;

    private boolean recording;
    private String filename;

    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_voice_record);

        //Ζηταει τις απαραιτητες αδειες


        recording=false;

        //αρικοποιηση
        chronometer=findViewById(R.id.simpleChronometer);

        mediaRecorder=new MediaRecorder();

        button=findViewById(R.id.button);

        textView=findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClick();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if (chronometer.getText().toString().endsWith("15")){//το endswith("15") σημαινει
                    stopClock();                                     //οταν το χρονομετρο γραψει "00:15" θα σταματησει η ηχογραφηση
                    stopRecording();
                }

            }
        });

        folder=new File(getExternalFilesDir(null) + "/MyRecording/");
        if (!folder.exists()){
            folder.mkdir(); //αν δεν υπαρχει το δημιουργει
        }

        //κλαση που χειριζεται τα αντικειμενα της λιστας με της ηχογραφησεις
        //todo : πιο ομορφη λιστα
        customListHandler = new CustomListHandler(findViewById(R.id.voiceRecord),getApplicationContext());
        filename=folder + "/";
    }

    /**
     * Την πρωτη φορα που θα πατηθει το κουμπι ξεκιναει η ηχογραφηση
     * Την δευτερη σταματαει και αποθηκευεται.
     */
    private void ButtonClick(){
        if (!recording){
            startClock();
            startRecording();
        }else{
            stopClock();
            stopRecording();
        }

    }

    /**
     * Ξεκιναει το χρονομετρο
     */
    private void startClock(){
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.start();
    }

    /**
     * Σταματαει το χρονομετρο
     */
    private void stopClock(){
        chronometer.setText("00:00");

        chronometer.stop();
    }

    /**
     * Ξεκιναει η ηχογραφηση
     */
    private void startRecording(){
        customListHandler.stop(); //Οταν ξεκινησει η ηχογραφηση θελουμε να σταματησουν να παιζουν τυχον αλλες ηχογραφησεις

        Toast.makeText(VoiceRecordActivity.this,R.string.started_recording, Toast.LENGTH_SHORT).show();

        if (mediaRecorder==null){//ηχογραφηση του mediaRecorder,ειναι η κλαση που χρησιμοποιειται για την ηχογραφηση
            mediaRecorder=new MediaRecorder();
        }

        //Θα αλλαξει αυτο,αλλα προς το παρον:
        //Παιρνει το κειμενο απο το editText,αν ειναι κενο παιρνουμε ενα timestamp ουσιαστικα
        //Αν δεν ειναι,χρησιμοποιουμε το κειμενο που εχει δωσει ο χρηστης
        //todo : ελεγχο οτι δεν εχει περιεργους χαρακτηρες (. / ! κλπ)
        String temp=textView.getText().toString();
        if (temp.equals("")){
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            filename= filename + ts + ".mp3";
        }else{
            filename=filename+ temp + ".mp3";
        }

        File file=new File(filename );
        write(file); //συναρτηση για την δημιουργεια του αρχειου.
        filename=folder + "/"; //reset


        customListHandler.add(file.getPath(),filename); //προσθηκη ηχογραφησης στην λιστα

        //google
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }catch(IOException e){
            e.printStackTrace();
        }

        button.setText(R.string.stop_recording);
        recording=true;
    }

    /**
     * Σταματαει η ηχογραφηση
     */
    private void stopRecording(){

        Toast.makeText(VoiceRecordActivity.this, R.string.stopped_recording , Toast.LENGTH_SHORT).show();

        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder=null;

        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        button.setText(R.string.start_recording);
        recording=false;

    }

    /**
     * Δημιουργει το αρχειο στο οποιο θα αποθηκευτει η ηχογραφηση
     * @param file το αρχειο
     */
    private  void write(File file)  {
        file=check(file,0); //συναρτηση που υλοποιειται παρακατω
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Αναδρομικη συναρτηση που ελεγχει αν το αρχειο υπαρχει ηδη
     * αν υπαρχει προσθετει το (1) και ξανα ελεγχει κοκ
     * @param oldFile το αρχειο το οποιο ελεγχουμε
     * @param found ποσες φορες εχει βρεθει αρχειο με το ιδιο ονομα,χρησιμοποιειται στην δημιουργια του καινουριου
     * @return επιστρεφει το αρχειο.
     */
    private  File check(File oldFile, int found){

        System.out.println("CHEKING  " + oldFile.getName());

        File newFile=new File(oldFile.getAbsolutePath());

        if (oldFile.exists()){
            String t=oldFile.getAbsolutePath();

            if (found==0){
                t=t.replaceFirst("\\.mp3","(1).mp3");

            }else{
                t= t.replaceFirst("(\\(\\d\\))","(" + (found+1) + ")");
            }
            newFile=new File(t);
            newFile=check(newFile,++found);
        }else{
            return newFile;
        }
        return newFile;
    }

    /**
     * Οταν σταματαει η εφαρμογη,πχ παει στο background,ελευθερονεται ο mediaRecorder
     * και ο mediaPlayer μεσω της customListHandler.clear() για να μην σπαταλιζονται ποροι
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        customListHandler.clear();

    }

}