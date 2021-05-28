package com.example.mobiledevergasia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom dialog που χρησιμοποιειται για την ονομασια την ηχογραφησης
 * Κανει override την onAttach και την onResume
 *                      Μεταβλητες :
 * fileNameTextView :TextView στο οποιο γραφει ο χρηστης το ονομα που θελει να εχει η ηχογραφηση του
 * listener : SaveDialogListener χρησιμοποιειται για την επικοινωνια με την VoiceRecordActivity
 */
public class SaveDialog extends AppCompatDialogFragment {
    private EditText fileNameTextView;
    private SaveDialogListener listener;
    private File folder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customView = inflater.inflate(R.layout.save_dialog_layout, null); //δημιουργεια του dialog_layout
        builder.setView(customView)
                .setTitle(R.string.save_recording) //Τιτλος
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.cancelled(); //δηλωνει οτι πατηθηκε το cancel και σε αυτη την περιπτωση δεν αποθηκευεται η ηχογραφηση
                    }
                }) //δημιουργια cancel button και λειτουργικοτηας του
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // performOkButtonAction();
                    }
                }); //δημιουργια save button

        fileNameTextView = customView.findViewById(R.id.fileName);
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_H_m", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String text=getString(R.string.defaultName) + "_" + currentDateandTime;
        fileNameTextView.setText(text);

        AlertDialog saveDialog = builder.create();
        saveDialog.setCanceledOnTouchOutside(false);

        saveDialog.getWindow().setGravity(Gravity.BOTTOM);
        return saveDialog;
    }

    /**
     * Ελεγχος αν το string περιεχει μονο γραμματα και/ή νουμερα.
     * @param string Το ονομα που θα δωθει στο αρχειο
     * @return true οταν δεν περιεχει ειδικους χαρακτηρες, false οταν περιεχει
     */
    private boolean fileNameIsOkay(String string) {
        if (string == null) // ελεγχει αν το String ειναι null {
            return false;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            // ελεγχος αν ο χαρακτηρας δεν ειναι γραμμα ή αριθμος
            // αν δεν ειναι τελειωνει ο ελεγχος και επιστρεφει false
            char c=string.charAt(i);
            if ((!Character.isLetterOrDigit(c))) {
                if (c!='_'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Εκτελειται οταν πατιεται το button_positive.
     * Ελεγχει αν το string που εδωσε ο χρηστης περιεχει
     * ειδικους χαρακτηρες,αν ναι του εμφανιζει το καταλληλο μηνυμα
     * και δεν προχωραει στην αποδεσμευσει του dialog οπως ειναι το default του.
     * Αν δεν υπαρχει προβλημα με το string συνεχιζει το προγραμμα.
     */
    private void performOkButtonAction() {
        String filename = fileNameTextView.getText().toString();
        if(fileNameIsOkay(filename)){
            if(!fileExists(filename)){
                listener.saveFileAs(filename);
                dismiss();
            }else{
                Toast.makeText(getActivity(), R.string.file_exists_toast, Toast.LENGTH_SHORT).show();
            }

        }else{
            //wait for correct input
            Toast.makeText(getActivity(), R.string.WrongInputMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Ελεγχει αν υπαρχει αρχειο με το ονομα που εχει δωσει ο χρηστης
     * @param filename το ονομα του αρχειου
     * @return True αν υπαρχει, false αν δεν υπαρχει
     */
    private boolean fileExists(String filename){
        File newFile= new File(folder + "/" + filename + ".mp3");
        if(newFile.exists()){
            newFile.delete();
            return true;
        }
        return false;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        listener.cancelled();
        super.onCancel(dialog);
    }

   /**
    * Γινεται override της onResume για να γινει override η λειτουργια του
    * button_positive
    */
   @Override
   public void onResume() {
       super.onResume();

       AlertDialog alertDialog = (AlertDialog) getDialog();

       Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
       okButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
                performOkButtonAction();
           }
       });
   }


    /**
     * αρχικοποιηση του listener
     *
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        folder=new File(context.getExternalFilesDir(null) + "/MyRecording/");
        try {
            listener = (SaveDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement saveDialogListener");
        }

    }

    /**
     * Interface που χρησιμοποιειται για να μεταφερουμε δεδομενα στο VoiceRecordActivity που δημιουργει το dialog μας
     */
    public interface SaveDialogListener {
        void saveFileAs(String filename); //Δειτε VoiceRecordActivity
        void cancelled(); //Δειτε VoiceRecordActivity
    }
}
