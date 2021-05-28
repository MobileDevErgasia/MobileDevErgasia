package com.example.mobiledevergasia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmDeleteDialog  extends AppCompatDialogFragment {

    private ConfirmDeleteListener listener;
    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customView = inflater.inflate(R.layout.delete_dialog_layout, null); //δημιουργεια του dialog_layout
        builder.setView(customView)
                .setTitle(R.string.delete_dialog) //Τιτλος
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.deleteCancelled(); //δηλωνει οτι πατηθηκε το cancel και σε αυτη την περιπτωση δεν αποθηκευεται η ηχογραφηση
                    }
                }) //δημιουργια cancel button και λειτουργικοτηας του
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.deleteFiles();
                    }
                }); //δημιουργια delete button



        AlertDialog confirmDeleteDialog = builder.create();
        confirmDeleteDialog.setCanceledOnTouchOutside(false);

        return confirmDeleteDialog;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * αρχικοποιηση του listener
     *
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmDeleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement saveDialogListener");
        }

    }

    /**
     * Interface που χρησιμοποιειται για να μεταφερουμε δεδομενα στο VoiceRecordActivity που δημιουργει το dialog μας
     */
    public interface ConfirmDeleteListener {
        void deleteFiles(); //Δειτε VoiceRecordActivity
        void deleteCancelled(); //Δειτε VoiceRecordActivity
    }
}
