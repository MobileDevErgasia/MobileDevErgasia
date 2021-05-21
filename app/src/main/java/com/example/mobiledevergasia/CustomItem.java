package com.example.mobiledevergasia;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mobiledevergasia.R;

import java.io.IOException;
//Todo javadoc genika kai return to default.
/**
 * Κλαση για τα αντικειμενα του gridView
 *              Μεταβλητες :
 * path : το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
 * desc : περιγραφη του αρχειου,χρησιμοποειται στην εμφανιση του στο gridView
 * playing : boolean μεταβλητη που ελεγχει αν ο mediaPlayer παιζει
 * toAutoLoop : boolean μεταβλητη που ελεγχει αν mediaPlayer θα κανει αυτοματη επαναληψη,ρυθμιζεται απο τα settings,default τιμη false
 * isChecked : boolean μεταπλητη που ελεγχει αν το αντικειμενο ειναι επιλεγμενο ή οχι
 * listener : interface της κλασης καλειται οταν ενα αντικειμενο τελειωσει την αναπαραγωγη του
 * myView : το View που αντιστοιχει στο αντικειμενο
 * mediaPlayer : MediaPLayer μεταβλητη χρησιμοποιεται για την αναπαραγωγη της ηχογραφησης που αντιστοιχει στο αντικειμενο
 */
public class CustomItem implements Parcelable {
    private String path,desc,previous;
    private boolean playing,isChecked,toAutoLoop,backgroundColorEdited,textColorEdited;
    private customItemListener listener;

    private int red,green,blue,textColor,backGroundColor;
    private View myView;
    public MediaPlayer mediaPlayer;

    /**
     * Constructor της κλασης. Αρχικοποιει τις τιμες των playing και isChecked σε false
     * @param path Το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
     * @param name Περιγραφη του αρχειου,χρησιμοποειται στην εμφανιση του στο gridView
     */
    public CustomItem(String path, String name){
        this.path=path;
        this.desc=name;
        playing=false;
        isChecked=false;
        toAutoLoop=false;
        backgroundColorEdited=false;
        textColorEdited=false;
        textColor=Color.WHITE;
    }

    protected CustomItem(Parcel in) {
        path = in.readString();
        desc = in.readString();
        playing = in.readByte() != 0;
        isChecked = in.readByte() != 0;
        toAutoLoop = in.readByte() != 0;
        backgroundColorEdited = in.readByte() != 0;
        textColorEdited = in.readByte() != 0;
        red = in.readInt();
        green = in.readInt();
        blue = in.readInt();
        textColor = in.readInt();
        backGroundColor=in.readInt();
    }

    public static final Creator<CustomItem> CREATOR = new Creator<CustomItem>() {
        @Override
        public CustomItem createFromParcel(Parcel in) {
            return new CustomItem(in);
        }

        @Override
        public CustomItem[] newArray(int size) {
            return new CustomItem[size];
        }
    };

    /**
     * αλλαζει  την τιμη του path
     * @param s το νεο path
     */
    public void setPath(String s){path=s;}

    /**
     * αλλαζει την τιμη του desc
     * @param s το νεο desc
     */
    public void setName(String s){
        desc=s;
    }

    /**
     *
     * @param color το επιθυμητο χρωμα
     */
    public void setTextColor(int color){
        textColor=color;
        textColorEdited=true;
    }

    /**
     *
     * @param red
     * @param green
     * @param blue
     */
    public void setBackgroundColor(int red, int green, int blue){
        this.red=red;
        this.green=green;
        this.blue=blue;
        backGroundColor=Color.rgb(red,green,blue);

        backgroundColorEdited=true;
    }

    /**
     *
     * @param b true για αυτοματη επαναληψη, false για να σταματαει την πρωτη φορα
     */
    public void setToAutoLoop(Boolean b){
        toAutoLoop=b;
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

    public void reset(){
        backgroundColorEdited=false;
        backGroundColor=0;
        textColorEdited=false;
        textColor=Color.WHITE;
    }

    public CustomItem getItem(){
        return this;
    }

    /**
     *
     * @return Επιστρεφει την περιγραφη του αντικειμενου,χρησιμοποειται στην εμφανιση του στο gridView
     */
    public String getName() {
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
     * @return επιστρεφει το χρωμα του κειμενου
     */
    public int getTextColor(){return  textColor;}

    /**
     *
     * @return επιστρεφει το χρωμα του background
     */
    public int getBackgroundColor(){

        return backGroundColor;
    }

    /**
     *
     * @return την τιμη της red μεταβλητης
     */
    public int getRed() {
        return red;
    }

    /**
     *
     * @return την τιμη της green μεταβλητης
     */
    public int getGreen() {
        return green;
    }

    /**
     *
     * @return την τιμη της blue μεταβλητης
     */
    public int getBlue() {
        return blue;
    }

    /**
     *
     * @return True αν το background χρωμα εχει αλλαχθει, False αν οχι
     */
    public Boolean isBackgroundColorEdited(){
        return backgroundColorEdited;
    }

    /**
     *
     * @return True αν το χρωμα κειμενου εχει αλλαχθει, False αν οχι
     */
    public Boolean isTextColorEdited(){return textColorEdited;}

    /**
     *
     * @return Επιστρεφει True αν γινεται αν αναπαραγη του αντικειμενου, False αν οχι
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
            mediaPlayer.setLooping(toAutoLoop);
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
        return "CustomItem{" +
                "path='" + path + '\'' +
                ", desc='" + desc + '\'' +
                ", playing=" + playing +
                ", isChecked=" + isChecked +
                ", toAutoLoop=" + toAutoLoop +
                ", backgroundColorEdited=" + backgroundColorEdited +
                ", listener=" + listener +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", myView=" + myView +
                ", mediaPlayer=" + mediaPlayer +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(desc);
        dest.writeByte((byte) (playing ? 1 : 0));
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (toAutoLoop ? 1 : 0));
        dest.writeByte((byte) (backgroundColorEdited ? 1 : 0));
        dest.writeByte((byte) (textColorEdited ? 1 : 0));
        dest.writeInt(red);
        dest.writeInt(green);
        dest.writeInt(blue);
        dest.writeInt(textColor);
        dest.writeInt(backGroundColor);
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
