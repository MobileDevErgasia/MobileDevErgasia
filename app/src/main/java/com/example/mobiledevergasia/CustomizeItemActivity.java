package com.example.mobiledevergasia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Κλαση για την τροποποιηση αντικειμενων
 *          Μεταβλητες :
 * redSeekBar : seekBar για την αναθεση τιμης του κοκκινου χρωματος
 * greenSeekBar : seekBar για την αναθεση τιμης του πρασσινου χρωματος
 * blueSeekBar : seekBar για την αναθεση τιμης του μπλε χρωματος
 * previewText : TextView που χρησιμοποιειται ως preview του ονοματος που εχει δωσει ο χρηστης
 * nameText : EditText για την αλλαγη του ονοματος του αντικειμενου
 * preview : View που χρησιμοποιειται ως preview του τελικου αποτελεσματος
 * red : int μεταβλητη για την τιμη του κοκκινου
 * green : int μεταβλητη για την τιμη του πρασσινου
 * blue : int μεταβλητη για την τιμη του μπλε
 * textColor : int μεταβλητη για το χρωμα κειμενου
 * index : int μεταβλητη,δειχνει την θεση του αντικειμενου στην λιστα
 * name : String μεταβλητη για το τελικο ονομα του αντικειμενου
 * previousName : String μεταβλητη για το αρχικο ονομα του αντικειμενου
 * colorChanged : boolean μεταβλητη για το αν αλλαξε το background χρωμα
 * textColorChanged : boolean μεταβλητη για το αν αλλαξε το χρωμα κειμενου
 * nameChanged : boolean μεταβλητη για το αν αλλαξε το ονομα
 * item : CustomItem μεταβλητη για το αντικειμενο που τροποποιειται
 * folder : File μεταβλητη,δειχνει στον φακελο στον οποιο αποθηκευονται οι ηχογραφησεις
 * toast : Toast μεταβλητη για να μην κανουν stack τα μηνυματα προς τον χρηστη αν γινει πολλες φορες
 * και γρηγορα καποια ενεργεια που το εμφανιζει
 */
public class CustomizeItemActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar redSeekBar,greenSeekBar, blueSeekBar;
    private TextView previewText;
    private EditText nameText;
    private View preview;
    private int red,green,blue,textColor,index;
    private boolean colorChanged,textColorChanged,nameChanged;
    private String name,previousName;
    private Toast toast;
    private CustomItem item;
    private File folder;

    /**
     * Δημιουργια του activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_item);

        Intent intent=getIntent();

        item=intent.getParcelableExtra("item");
        item.uncheck();
        index=intent.getIntExtra("index",0);
        initValues();
        initViews();


        initSeekBars();
        initColorButtons();

        initActionButtons();

    }

    /**
     * Αρχικοποιηση μεταβλητων
     */
    private void initValues(){
        toast=new Toast(this);
        previousName=item.getName();
        folder=new File(getExternalFilesDir(null) + "/MyRecording/");
        textColor=item.getTextColor();
        nameChanged=false;
        colorChanged=false;
        textColorChanged=false;
    }

    /**
     * Αρχικοποιηση των seekBars,ο ρολος των οποιων ειναι
     * να διαλεξει ο χρηστης το χρωμα που θελει μεσω του
     * rgb συστηματος,κανουμε override τον listener τους πιο κατω
     * Οταν δημιουργηθουν,αν ερθει καποιο αντικειμενο που εχει ηδη
     * τροποιηθει το χρωμα του,τα seekbars παιρνουν τις αναλογες τιμες
     */
    private void initSeekBars(){
        redSeekBar =findViewById(R.id.red_seek_bar);
        greenSeekBar=findViewById(R.id.green_seek_bar);
        blueSeekBar =findViewById(R.id.blue_seek_bar);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        if(item.isBackgroundColorEdited()){
            setColors();
        }
    }

    /**
     * Αρχικοποιηση των κουμπιων για την επιλογη χρωματος κειμενου
     * Προστιθεται σε ολα ενας onClickListener μεσω του οποιου αλλαζει το χρωμα
     * του preview,του previewText και του nameText,αναλογα με ποιο κουμπι πατηθηκε
     */
    private void initColorButtons(){

        Button whiteButton =findViewById(R.id.white_button);
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.WHITE;
                previewText.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button greenButton =findViewById(R.id.green_button);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.GREEN;
                previewText.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button blackButton=findViewById(R.id.black_button);
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.BLACK;
                previewText.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button magentaButton =findViewById(R.id.magenta_button);
        magentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;
                textColor=Color.MAGENTA;
                previewText.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button redButton=findViewById(R.id.red_button);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.RED;
                previewText.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

    }

    /**
     * Αρχικοποιηση των κουμπιων cancel,reset,finish
     *
     */
    private void initActionButtons(){
        Button finishButton=findViewById(R.id.finishButton);
        Button cancelButton=findViewById(R.id.cancelButton);
        Button resetButton=findViewById(R.id.resetButton);

        //Οταν πατηθει το finishButton ελεγχεται αν εχει αλλαξει το ονομα και παιρνει την καταλληλη τιμη,
        //αν δεν εχει αλλαξει,παιρνει την τιμη του previousName. Μετα ελεγχεται αν το ονομα που επιλεχθηκε
        //πληρει τους κανονες, που θα δουμε παρακατω, μεσω της fileNamesIsOkay,αν δεν ειναι ενημερωνεται ο
        //χρηστης με toast μηνυμα. Αν ειναι,ελεγχεται αν το νεο ονομα ειναι ιδιο με το αρχικο,δεν χρησιμοποιειται
        //η nameChanged γιατι μπορει να εχει γινει καποια τροποποιηση στο κειμενο αλλα στο τελος ο χρηστης να επιλεξει
        //το ιδιο ονομα. Αν το νεο ονομα δεν ειναι ιδιο με το αρχικο,ελεγχεται αν υπαρχει καποιο αρχειο με αυτο
        //το ονομα,αν υπαρχει ενημερωνεται ο χρηστης μεσω toast μηνυματος.
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameChanged){
                    name=nameText.getText().toString();
                }else{
                    name=previousName;
                }

                if (fileNameIsOkay(name)){
                    Intent myIntent=new Intent(getApplicationContext(),VoiceRecordActivity.class);
                    if(!name.equals(previousName)){
                        File newFile= new File(folder + "/" + name + ".mp3");
                        if(newFile.exists()){
                           toast.cancel();
                           toast=Toast.makeText(CustomizeItemActivity.this,R.string.file_exists_toast,Toast.LENGTH_LONG);
                           toast.show();
                            return;
                        }
                        myIntent.putExtra("previousName",previousName);
                    }
                    if(colorChanged){
                        item.setBackgroundColor(red,green,blue);
                    }
                    if(textColorChanged){
                        item.setTextColor(textColor);
                    }


                    myIntent.putExtra("finished",true);
                    myIntent.putExtra("index",index);
                    myIntent.putExtra("customItem", item);
                    //χρησιμοποιειται το flag αυτο ετσι ωστε να ερθει στην αρχη της ουρας το instance που υπαρχει ηδη
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                    toast.cancel();
                    finish();

                }else{
                    toast.cancel();
                    toast=Toast.makeText(CustomizeItemActivity.this,R.string.WrongInputMessage,Toast.LENGTH_SHORT);
                    toast.show();
                    //Toast.makeText(getApplicationContext(), R.string.WrongInputMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //ακυρωνει τις οποιες αλλαγες
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(getApplicationContext(),VoiceRecordActivity.class);
                myIntent.putExtra("cancelled",true);
                myIntent.putExtra("index",index);
                myIntent.putExtra("customItem", item);

                //χρησιμοποιειται το flag αυτο ετσι ωστε να ερθει στην αρχη της ουρας το instance που υπαρχει ηδη
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(myIntent);
                toast.cancel();
                finish();

            }
        });

        //επαναφερει το αντικειμενο στην αρχικη του κατασταση,δεν αλλαζει το ονομα
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(getApplicationContext(),VoiceRecordActivity.class);
                myIntent.putExtra("Reset", true);
                myIntent.putExtra("index",index);
                myIntent.putExtra("customItem",item);

                //χρησιμοποιειται το flag αυτο ετσι ωστε να ερθει στην αρχη της ουρας το instance που υπαρχει ηδη
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
                toast.cancel();
                finish();
            }
        });
    }

    /**
     * Αρχικοποιηση των previewText,preview και nameText.
     * Αν το χρωμα κειμενου ειναι αλλαγμενο στο αντικειμενο
     * το previewText και το nameText παιρνουν το χρωμα αυτο.
     * Αν το backgroundColor ειναι αλλαγμενο στο αντικειμενο
     * το preview παιρνει το χρωμα αυτο
     */
    private void initViews(){
        previewText =findViewById(R.id.previewText);
        preview =findViewById(R.id.preView);
        nameText=findViewById(R.id.nameText);
        nameText.setText(previousName);
        previewText.setText(previousName);

        if(item.isTextColorEdited()){
            previewText.setTextColor(item.getTextColor());
            nameText.setTextColor(item.getTextColor());
        }

        if(item.isBackgroundColorEdited()){
            preview.setBackgroundColor(item.getBackgroundColor());
        }

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //Καθε φορα που αλλαζει το κειμενο ενημερωνεται το previewText και το ονομα του αντικειμενου
            @Override
            public void afterTextChanged(Editable s) {
                String t=nameText.getText().toString();
                previewText.setText(t);
                name=t;
                nameChanged=true;
                item.setName(name);
            }
        });
    }

    /**
     * Αν εχει ερθει ενα αντικειμενο το οποιο εχει
     * τροποποιημενο background χρωμα,δινεται στα
     * seekBars η αναλογη τιμη
     */
    private void setColors(){
        red=item.getRed();
        green=item.getGreen();
        blue=item.getBlue();

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

    }

    /**
     * Override του listener του seekBar
     * @param seekBar Το seekBar στο οποιο εγινε η αλλαγη
     * @param progress Η νεα τιμη του seekBar
     * @param fromUser Δεν χρησιμοποιειται
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.red_seek_bar: red=progress; break;
            case R.id.green_seek_bar: green=progress; break;
            case R.id.blue_seek_bar: blue=progress; break;
        }
        colorChanged=true;
        preview.setBackgroundColor(Color.rgb(red,green,blue));
    }

    /**
     * Δεν υλοποιει καποια λειτουργια,απαιτειται να γινει override
     * @param seekBar Δεν χρησιμοποιειται
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //doNothing
    }

    /**
     * Δεν υλοποιει καποια λειτουργια,απαιτειται να γινει override
     * @param seekBar Δεν χρησιμοποιειται
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //doNothing
    }

    /**
     * Ελεγχος αν το string περιεχει μονο γραμματα,νουμερα ή τους χαρακτηρες " (,),_".
     * @param string Το ονομα που θα δωθει στο αρχειο
     * @return true οταν δεν περιεχει ειδικους χαρακτηρες, false οταν περιεχει
     */
    private boolean fileNameIsOkay(String string) {
        if (string == null || string.equals("")) { // ελεγχει αν το String ειναι null ή κενο
            return false;
        }
        int len = string.length();
        for (int i = 0; i < len; i++) {

            char c = string.charAt(i);
            if ((!Character.isLetterOrDigit(c))) {
                if (c != '(' && c != ')' && c!='_') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Γινεται override για να μην γινεται καποια ενεργεια
     * οταν πατηθει το κουμπι back
     */
    @Override
    public void onBackPressed(){
        //doNothing
    }
}