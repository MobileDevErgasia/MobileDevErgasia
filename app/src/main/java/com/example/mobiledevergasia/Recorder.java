package com.example.mobiledevergasia;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Κλαση για την υλοποιηση της ηχογραφησης.
 *             Μεταβλητες :
*  mediaRecorder : MediaRecorder μεταβλητη,χρησιμοποιειται για την ηχογραφηση
 *  isRecording :  boolean μεταβλητη ελεγχει αν γινεται ηχογραφηση
 */
public class Recorder  {

    private MediaRecorder mediaRecorder;
    private boolean isRecording;

    public Recorder(){
        mediaRecorder=new MediaRecorder();
        isRecording=false;
    }

    /**
     * Ξεκιναει η ηχογραφηση.
     * @param temporaryFile το προσωρινο ονομα του αρχειου στο οποιο θα αποθηκευτει η ηχογραφηση.
     *                      Το mediarecorder API απαιτει εξαρχης το μονοποτατι
     *      * του αρχειου στο οποιο θα γινει η αποθηκευση,το αρχειο αυτο ομως ενδεχεται να αλλαξει
     *      * ονομα στην συνεχεια,εξου και το temporaryFile
     */
    public void startRecording(File temporaryFile){
        isRecording=true;
        if (mediaRecorder==null){//αρχικοποιηση του mediaRecorder,ειναι η κλαση που χρησιμοποιειται για την ηχογραφηση
            mediaRecorder=new MediaRecorder();
        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(temporaryFile.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Τελος της ηχογραφησης. Απελευθερωνεται ο mediarecorder μεχρι την επομενη χρηση
     */
    public void stopRecording(){
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder=null;

        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        isRecording=false;
    }

    /**
     * Ελεγχος αν γινεται ηχογραφηση
     * @return True αν γινεται ηχογραφηση, false αν δεν γινεται
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Απελευθερωνεται ο mediarecorder. Χρησιμοποιειται απο την VoiceRecordActivity
     * στην περιπτωση που η εφαρμογη περασει στο background
     */
    public void clear(){
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
