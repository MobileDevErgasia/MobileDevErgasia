package com.example.mobiledevergasia;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomListHandler {

    private final ArrayList<CustomItem> myList;
    private GridView myGridView;
    private CustomArrayAdapter arrayAdapter;
    private MediaPlayer mediaPlayer;

    private boolean playing=false;

    public CustomListHandler(View view, Context context){
        myGridView =view.findViewById(R.id.listView);
        myList=new ArrayList<>();

        File directory = new File(view.getContext().getExternalFilesDir(null) + "/MyRecording/"+"");
        File[] files = directory.listFiles();

        int fileLength=files.length;
        //βαζει στην λιστα ολα τα αρχεια που υπαρχουν αποθηκευμενα
        for (int i=0;i<fileLength;i++){
            myList.add(new CustomItem(files[i].toString(),"" + i)); //Το i θα αλλαξει οταν βαλουμε την βαση

        }

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomItem item = (CustomItem)parent.getItemAtPosition(position);
                if (!item.isPlaying()){
                    item.start();
                }else{
                    item.stop();
                }
            }
        });
        arrayAdapter = new CustomArrayAdapter(context,0,myList);
        myGridView.setAdapter(arrayAdapter);
    }

    public void add(String path,String desc){
        myList.add(new CustomItem(path,desc));
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Σταματανε να παιζουν ολα για να γινει ηχογραφηση
     */
    protected void stop(){
        for(CustomItem item : myList){ //ειναι for-each loop,για καθε item της myList,δες αν παιζει και αν παιζει σταματα το.
            if (item.isPlaying()){
                item.stop();
            }
        }
    }


    /**
     * Αποδεσμευση του mediaplayer.Χρησιμοποιειται απο την VoiceRecordActivity
     */
    protected void clear(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
