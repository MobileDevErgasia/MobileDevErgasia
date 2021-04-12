package com.example.mobiledevergasia;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomListHandler {

    private final ArrayList<CustomItem> myList;
    private ListView myListView;
    private CustomArrayAdapter arrayAdapter;
    private MediaPlayer mediaPlayer;

    private boolean playing=false;

    public CustomListHandler(View view, Context context){
        myListView=view.findViewById(R.id.listView);
        myList=new ArrayList<>();

        File directory = new File(view.getContext().getExternalFilesDir(null) + "/MyRecording/"+"");
        File[] files = directory.listFiles();

        int fileLength=files.length;
        //βαζει στην λιστα ολα τα αρχεια που υπαρχουν αποθηκευμενα
        for (int i=0;i<fileLength;i++){
            myList.add(new CustomItem(files[i].toString(),"" + i)); //Το i θα αλλαξει οταν βαλουμε την βαση

        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomItem item = (CustomItem)parent.getItemAtPosition(position);
                if (!playing){
                    System.out.println(" Ξεκιναω !!!");
                    play(item.getPath());
                }else{
                    stop();
                    System.out.println(" Σταματαω !!!!~");
                }
            }
        });
        arrayAdapter = new CustomArrayAdapter(context,0,myList);
        myListView.setAdapter(arrayAdapter);
    }

    public void add(String path,String desc){
        myList.add(new CustomItem(path,desc));
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Παιζει την ηχογραφηση που πατηθηκε
     * @param filePath το μονοπατι του αρχειου
     */
    protected  void play(String filePath){
        //TODO : otan paizei kati kai patisw record,na stamatisei,ama kanw record na min ginetai tpt na patithoun ta alla.
        //TODO : pote kanw release to mediaplayer.
        mediaPlayer = new MediaPlayer();
        playing=true;

        try{

            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Σταματανε να παιζουν ολα για να γινει ηχογραφηση
     */
    protected void stop(){
        playing=false;
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.release();
                mediaPlayer=null;
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
