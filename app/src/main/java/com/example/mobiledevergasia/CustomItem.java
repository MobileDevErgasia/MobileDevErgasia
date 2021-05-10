package com.example.mobiledevergasia;

import android.media.MediaPlayer;
import android.view.View;

import java.io.IOException;

/**
 * Κλαση για τα αντικειμενα του gridView
 *              Μεταβλητες :
 * path : το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
 * desc : περιγραφη του αρχειου,χρησιμοποειται στην εμφανιση του στο gridView
 * playing : boolean μεταβλητη που ελεγχει αν ο mediaPlayer παιζει
 * isChecked : boolean μεταπλητη που ελεγχει αν το αντικειμενο ειναι επιλεγμενο ή οχι
 * listener : interface της κλασης καλειται οταν ενα αντικειμενο τελειωσει την αναπαραγωγη του
 * myView : το View που αντιστοιχει στο αντικειμενο
 * mediaPlayer : MediaPLayer μεταβλητη χρησιμοποιεται για την αναπαραγωγη της ηχογραφησης που αντιστοιχει στο αντικειμενο
 */
public class CustomItem {
    private String path,desc;
    private boolean playing,isChecked;

    private customItemListener listener;

    private View myView;
    public MediaPlayer mediaPlayer;

    /**
     * Constructor της κλασης. Αρχικοποιει τις τιμες των playing και isChecked σε false
     * @param path Το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
     * @param desc Περιγραφη του αρχειου,χρησιμοποειται στην εμφανιση του στο gridView
     */
    public CustomItem(String path, String desc){

        this.path=path;
        this.desc=desc;
        playing=false;
        isChecked=false;
    }

    /**
     * αρχικοποιει τον listener
     * @param listener Interface το οποιο πρεπει να γινει override
     */
    public void setListener(customItemListener listener) {
        this.listener = listener;
    }

    /**
     * Αρχικοποιει το myView
     * @param view Το view που αντιστοιχει στο αντικειμενο,χρησιμοποιειται για την ευρεση της εικονας
     *             soundOnImageView και την διαχειριση της
     */
    public void setView(View view) {
        myView = view;
    }

    /**
     *
     * @return Επιστρεφει την περιγραφη του αντικειμενου,χρησιμοποειται στην εμφανιση του στο gridView
     */
    public String getDesc() {
        return desc;
    }

    /**
     *
     * @return Επιστρεφει το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
     */
    public String getPath(){
        return path;
    }

    /**
     *
     * @return Επιστρεφε True αν γινεται αν αναπαραγη του αντικειμενου, False αν οχι
     */
    public Boolean isPlaying(){ return playing;}

    /**
     *
     * @return Επιστρεφει True αν το αντικειμενο ειναι επιλεγμενο, False αν οχι
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Επιλεγει το αντικειμενο και κανει την isChecked=true
     * Χρησιμοποιειται απο την CustomListHandler στην επιλογη
     * πολλαπλων στοιχειων
     */
    public void check(){ isChecked=true;}

    /**
     * Αποεπελεγει το αντικειμενο και κανει την isChecked=false
     * Χρησιμοποιειται απο την CustomListHandler στην επιλογη
     * πολλαπλων στοιχειων
     */
    public void uncheck(){isChecked=false;}

    /**
     * Ξεκιναει την αναπαραγωγη του αρχειου που
     * αντιστοιχει στο αντικειμενο αυτο
     * @param filePath το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
     */
    public void start(String filePath){
        myView.findViewById(R.id.soundOnImageView).setVisibility(View.VISIBLE);
        playing=true;
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playing=false; //Οταν ολοκληρωθει η αναπαραγωγη, το playing παιρνει την τιμη false
                myView.findViewById(R.id.soundOnImageView).setVisibility(View.GONE);
                listener.onItemFinished();
            }
        });

        try{

            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Σταματαει την αναπαραγωγη του αρχειου που αντιστοιχει στο
     * αντικειμενο και απελευθερωνει τον mediaPlayer
     */
    public void stop(){
        myView.findViewById(R.id.soundOnImageView).setVisibility(View.GONE);

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

    /**
     * interface της κλασης,χρησιμοποιειται απο την CustomListHandler
     * η συναρτηση onItemFinished() καλειται οταν το αντικειμενο σταματησει
     * την αναπαραγωγη,ειτε λογω χρηστη ειτε επειδη τελειωσε
     */
    public interface customItemListener{
       void onItemFinished();
    }

}
