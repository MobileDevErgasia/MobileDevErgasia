package com.example.mobiledevergasia;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import java.io.IOException;

/**
 * Κλαση για τα αντικειμενα του gridView
 * Κανει implement Parcelable για να μπορει να σταλει μεσω intent
 *              Μεταβλητες :
 * path : το μονοπατι που βρισκεται το αρχειο που αντιστοιχει σε αυτο το αντικειμενο
 * desc : περιγραφη του αρχειου,χρησιμοποειται στην εμφανιση του στο gridView
 * playing : boolean μεταβλητη που ελεγχει αν ο mediaPlayer παιζει
 * toAutoLoop : boolean μεταβλητη που ελεγχει αν mediaPlayer θα κανει αυτοματη επαναληψη,ρυθμιζεται απο τα settings,default τιμη false
 * backgroundColorEdited : boolean μεταβλητη που ελεγχει αν εχει αλλαξει το background του αντικειμενου
 * textColorEdited : boolean μεταβλητη που ελεγχει αν εχει αλλαξει το χρωμα κειμενου του αντικειμενου
 * isChecked : boolean μεταπλητη που ελεγχει αν το αντικειμενο ειναι επιλεγμενο ή οχι
 * red : int μεταβλητη για την τιμη του κοκκινου στο backGroundColor
 * green : int μεταβλητη για την τιμη του πρασσινου στο backGroundColor
 * blue : int μεταβλητη για την τιμη του μπλε στο backGroundColor
 * textColor : int μεταβλητη για το χρωμα κειμενου
 * backGroundColor : int μεταβλητη για το χρωμα του background,χρησιμοποει τα red green blue
 * listener : interface της κλασης καλειται οταν ενα αντικειμενο τελειωσει την αναπαραγωγη του
 * myView : το View που αντιστοιχει στο αντικειμενο
 * mediaPlayer : MediaPLayer μεταβλητη χρησιμοποιεται για την αναπαραγωγη της ηχογραφησης που αντιστοιχει στο αντικειμενο
 */
public class CustomItem implements Parcelable {
    private String path,desc;
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

    /**
     * Απαιτειται για να γινει implement Parcelable
     * @param in
     */
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

    /**
     * Απαιτειται για να γινει implement Parcelable
     */
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
     * αλλαζει το χρωμα κειμενου και ενημερωνει το αντιστοιχο
     * flag οτι εχει γινει αλλαγη
     * @param color το επιθυμητο χρωμα
     */
    public void setTextColor(int color){
        textColor=color;
        textColorEdited=true;
    }

    /**
     * Αλλαζει το backgroundColor μεσω την Color.rgb
     * (για την αλλαγη του χρωματος χρησιμοποιουνται seekbars red green blue)
     * και ενημερωνει το αντιστοιχο flag οτι εχει γινει αλλαγη
     * @param red η τιμη του κοκκινου
     * @param green η τιμη του πρασσινου
     * @param blue η τιμη του μπλε
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

    /**
     * Επαναφερει το αντικειμενο στην αρχικη του κατασταση
     */
    public void reset(){
        backgroundColorEdited=false;
        backGroundColor=0;
        textColorEdited=false;
        textColor=Color.WHITE;
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

    /**
     * Απαιτειται για να γινει implement Parcelable
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Απαιτειται για να γινει implement Parcelable
     * @param dest
     * @param flags
     */
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
