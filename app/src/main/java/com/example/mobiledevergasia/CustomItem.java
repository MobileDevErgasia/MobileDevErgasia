package com.example.mobiledevergasia;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Κλαση για τα ειδικα αντικειμενα.Χρησιμοποιειται απο την CustomListHandler
 */
public class CustomItem {
    private String path,desc;
    private boolean playing;

    private MediaPlayer mediaPlayer;

    /**
     * Constructor της κλασης,αρχικοποιει το μονοπατι για το mp3 αρχειο και μια περιγραφη
     * @param path το μονοπατι για το mp3 αρχειο
     * @param desc περιγραφη του αρχειου,χρησιμοποιειται στην εμφανιση του
     */
    public CustomItem(String path,String desc){
        this.path=path;
        this.desc=desc;
        playing=false;
    }

    /**
     * @return την περιγραφη του αρχειου
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return το μονοπατι του αρχειου
     */
    public String getPath(){
        return path;
    }

    /**
     * Ελεχγει αν το συγκεκριμενο αντικειμενο αναπαραγει το mp3 αρχειο του
     * @return true αν το αναπαραγει, false αν οχι
     */
    public Boolean isPlaying(){ return playing;}

    /**
     * Ξεκιναει την αναπαραγωγη του mp3 αρχειου
     */
    public void start(){
        playing=true;
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //οταν ο MediaPlayer ολοκληρωσει την αναπαραγωγη του
            @Override                                                                //καλειται η stop()
            public void onCompletion(MediaPlayer mp) {
               stop();
            }
        });

        try{
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Σταματαει την αναπαραγωγη του mp3 αρχειου και αποδεσμευει τον mediaPlayer
     */
    public void stop(){
        playing=false;
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }
    }

    @Override
    public String toString() {
        return "CustomList{" +
                "path='" + path + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
