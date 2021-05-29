package com.example.mobiledevergasia;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.appbar.AppBarLayout;


/**
 * Κλαση για τον χειρισμο του Toolbar. Το toolbar υλοποιει 3 λειτουργειες.
 * 1)Καταμετρηση και εμφανιση του πληθους επιλεγμενων στοιχειων απο το gridview
 * 2)Ακυρωση επιλογης πολλαπλων αντικειμενων
 * 3)Διαγραφη των επιλεγμενων αντικειμενων
 *
 *              Μεταβλητες :
 * cancelImageView : ImageView για την ακυρωση του toolbar
 * editImageView : ImageView για την τροποποιηση αντικειμενου
 * deleteImageView : ImageView για την διαγραφη των επιλεγμενων αντικειμενων
 * counterTextView : TextView για την εμφανιση του πληθους των επιλεγμενων αντικειμενων
 * itemsSelectedTextView : TextView που γραφει "Επιλεγμενα αντικειμενα",χρησιμοποιειται για την αλλαγη γλωσσας του
 * appBarLayout : AppBarLayout,περιερχει το toolbar,χρησιμοποιειται για την εμφανιση και αποκρυψη του
 * listener : CustomToolbarListener χρησιμοποιειται απο τον CustomListHandler για την επικοινωνια των 2.
 * καλειται οταν πατιεται ειτε η cancelImageView,ειτε η deleteImageView
 * counter : int μετραει το πληθος των στοιχειων προς διαγραφη και το εμφανιζει μεσω του counterTextView
 */
public class    CustomToolbarHandler {

    private final ImageView cancelImageView,deleteImageView, editImageView;
    private  TextView counterTextView,itemsSelectedTextView;
    private final AppBarLayout appBarLayout;
    private CustomToolbarListener listener;
    private int counter;

    /**Constructor της κλασης.
     * @param view Το view στο οποιο βρισκεται το toolbar,δηλαδη το voice_record_activity
     *             χρησιμοποιεται στην αρχικοποιση των imageView,textView και appBarLayout.
     */
    public CustomToolbarHandler(View view){
        cancelImageView=view.findViewById(R.id.cancelImageView);
        deleteImageView=view.findViewById(R.id.deleteImageView);
        editImageView =view.findViewById(R.id.editImageView);
        appBarLayout=view.findViewById(R.id.appbarlayout);
        counterTextView=view.findViewById(R.id.counterTextView);
        itemsSelectedTextView=view.findViewById(R.id.itemsSelectedTextView);

        counter=0;

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                listener.onDeleteImagePressed();
            }
        });

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelImagePressed();
            }
        });

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditImagePressed();
            }
        });

    }

    /**
     @param listener Interface της κλασης,αρχικοποιεται απο την CustomListHandler κλαση.
      *                Χρησιμοποιεται για την επικοινωνια του CustomToolbarHandler και
      *                του CustomListHandler. Συγκριμενα για την περιπτωση που πατηθει
      *                 η cancelImageView,που ακυρωνει την επιλογη αντικειμενων, ή στην
      *                 περιπτωση που πατηθει η deleteImageView,που διαγραφει τα
      *                 επιλεγμενα αντικειμενα.
     */
    public void setListener(CustomToolbarListener listener){
        this.listener=listener;
    }

    /**
     * Εμφανιση του toolbar,ο counter παιρνει την τιμη 0
     */
    public void show(){
        appBarLayout.setVisibility(View.VISIBLE);
        counter=0;
        itemsSelectedTextView.setText(R.string.items_selected);
    }

    /**
     * Αποκρυψη του toolbar,χρησιμοποιεται το View.GONE αντι
     * του View.INVISIBLE ετσι ωστε να μην πιανει χωρο στην οθονη
     * οταν δεν ειναι ορατο. Στην συνεχεια καλειται η clear()
     */
    public void hide(){
        appBarLayout.setVisibility(View.GONE);
    }

    /**
     * Εμφανιζει την εικονα editImageView
     * Η εικονα αυτη ειναι ορατη μονο οταν
     * ειναι επιλεγμενο ενα και μονο ενα
     * αντικειμενο
     */
    public void showEditImage(){
        editImageView.setVisibility(View.VISIBLE);
    }

    /**
     * Κρυβει την εικονα editImageView
     * Η εικονα αυτη ειναι ορατη μονο οταν
     * ειναι επιλεγμενο ενα και μονο ενα
     * αντικειμενο
     */
    public void hideEditImage(){
        editImageView.setVisibility(View.GONE);
    }

    /**
     * Αυξηση του counter και προσαρμογη του μηνυματος
     */
    public void increase(){
        counter++;
        String temp="" + counter;
        counterTextView.setText(temp);
    }

    /**
     * Μειωση του counter και προσαρμογη του μηνυματος
     */
    public void decrease(){
        counter--;
        String temp="" + counter;
        counterTextView.setText(temp);
    }

    /**
     * Επιστρεφει την τιμη του counter
     * @return η τιμη του counter
     */
    public int getCounter(){
        return counter;
    }

    /**
     * Interface της κλασης,αρχικοποιεται απο την CustomListHandler κλαση.
     * Χρησιμοποιεται για την επικοινωνια του CustomToolbarHandler και
     * του CustomListHandler. Συγκριμενα για την περιπτωση που πατηθει
     *  η cancelImageView, ακυρωνει την επιλογη αντικειμενων, στην
     *  περιπτωση που πατηθει η deleteImageView, καλειται το ConfirmDeleteDialog
     *  και στην περιπτωση που πατηθει η editImageView ανοιγει το CustomizeItemActivity
     */
    public interface CustomToolbarListener {
         void onDeleteImagePressed() ;
         void onCancelImagePressed();
         void onEditImagePressed();

    }
}
