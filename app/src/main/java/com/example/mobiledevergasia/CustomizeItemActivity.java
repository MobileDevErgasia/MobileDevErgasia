package com.example.mobiledevergasia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

//TODO
public class CustomizeItemActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar redSeekBar,greenSeekBar,blueSeekbar;
    private TextView preview;
    private EditText nameText;
    private View view;
    private int red,green,blue,textColor,index;
    private boolean colorChanged,textColorChanged,nameChanged;
    private String name,path,previousName;

    private CustomItem item;
    private File folder;


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

    private void initValues(){
        previousName=item.getName();
        path=item.getPath();

        folder=new File(getExternalFilesDir(null) + "/MyRecording/");
        textColor=item.getTextColor();
        nameChanged=false;
        colorChanged=false;
        textColorChanged=false;
    }

    private void initSeekBars(){
        redSeekBar =findViewById(R.id.red_seek_bar);
        greenSeekBar=findViewById(R.id.green_seek_bar);
        blueSeekbar=findViewById(R.id.blue_seek_bar);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekbar.setOnSeekBarChangeListener(this);

        if(item.isBackgroundColorEdited()){
            setColors();
        }
    }

    private void initColorButtons(){

        Button whiteButton =findViewById(R.id.white_button);
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.WHITE;
                preview.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button greenButton =findViewById(R.id.green_button);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.GREEN;
                preview.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button blackButton=findViewById(R.id.black_button);
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.BLACK;
                preview.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button magentaButton =findViewById(R.id.magenta_button);
        magentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;
                textColor=Color.MAGENTA;
                preview.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

        Button redButton=findViewById(R.id.red_button);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textColorChanged=true;

                textColor=Color.RED;
                preview.setTextColor(textColor);
                nameText.setTextColor(textColor);
            }
        });

    }

    private void initActionButtons(){
        Button finishButton=findViewById(R.id.finishButton);
        Button cancelButton=findViewById(R.id.cancelButton);
        Button resetButton=findViewById(R.id.resetButton);

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
                            Toast.makeText(CustomizeItemActivity.this, R.string.file_exists_toast, Toast.LENGTH_SHORT).show();
                            newFile.delete();
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

                    myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(myIntent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), R.string.WrongInputMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(getApplicationContext(),VoiceRecordActivity.class);
                myIntent.putExtra("cancelled",true);
                myIntent.putExtra("index",index);
                myIntent.putExtra("customItem", item);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(myIntent);
                finish();

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(getApplicationContext(),VoiceRecordActivity.class);
                myIntent.putExtra("Reset", true);
                myIntent.putExtra("index",index);
           //     myIntent.putExtra("name",name);
            //    myIntent.putExtra("path",path);
                myIntent.putExtra("customItem",item);

                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void initViews(){
        preview=findViewById(R.id.previewText);
        view=findViewById(R.id.preView);
        nameText=findViewById(R.id.nameText);
        nameText.setText(previousName);
        preview.setText(previousName);

        if(item.isTextColorEdited()){
            preview.setTextColor(item.getTextColor());
        }

        if(item.isBackgroundColorEdited()){
            view.setBackgroundColor(item.getBackgroundColor());
        }

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String t=nameText.getText().toString();
                preview.setText(t);
                name=t;
                nameChanged=true;
                item.setName(name);
            }
        });
    }

    private void setColors(){
        red=item.getRed();
        green=item.getGreen();
        blue=item.getBlue();

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekbar.setProgress(blue);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.red_seek_bar: red=progress; break;
            case R.id.green_seek_bar: green=progress; break;
            case R.id.blue_seek_bar: blue=progress; break;
        }
        colorChanged=true;
        view.setBackgroundColor(Color.rgb(red,green,blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Ελεγχος αν το string περιεχει μονο γραμματα και/ή νουμερα.
     * @param string Το ονομα που θα δωθει στο αρχειο
     * @return true οταν δεν περιεχει ειδικους χαρακτηρες, false οταν περιεχει
     */
    private boolean fileNameIsOkay(String string) {
        if (string == null || string.equals("")) // ελεγχει αν το String ειναι null {
            return false;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            // ελεγχος αν ο χαρακτηρας δεν ειναι γραμμα ή αριθμος
            // αν δεν ειναι τελειωνει ο ελεγχος και επιστρεφει false
            char c = string.charAt(i);
            if ((!Character.isLetterOrDigit(c))) {
                if (c != '(' && c != ')' && c!='_') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        //doNothing
    }
}