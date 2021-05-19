package com.example.mobiledevergasia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Κλαση για τον χειρισμο των αντικειμενων
 * του gridview,δηλαδη των ηχογραφησεων
 * Πιο συγκερκιμενα, χειριζεται την αναπαραγωγη και το σταματημα τους και
 * την λειτουργια πολλαπλων αντικειμενων(για πιθανη διαγραφη τους)
 *          Μεταβλητες :
 *
 * CustomToolbarHandler : Κλαση για τον χειρισμο του toolbar,καλειται για στην επιλογη πολλαπλων αντικειμενων
 * customListListener : Interface της κλασης CustomItem χρ
 * myGridView : GridView το οποιο χειριζεται η κλαση. Τα αντικειμενα του ειναι CustomItem
 * myList : ArrayList με τα στοιχεια CustomItem που βρισκονται αποθηκευμενα
 * arrayAdapter : CustomArrayAdapter προστιθεται στο myGridView και του προσθετει τα CustomItem
 * itemCheckedColor : πρασινο χρωμα που δηλωνει οτι το στοιχειο εχει επιλεγθει
 * defaultBackground : Το defaultBackground χρωμα των στοιχειων.
 * counter : int το οποιο μετραει τον αριθμο επιλεγμενων αντικειμενων
 * itemsPlaying : int το οποιο μετραει τον αριθμο των αντικειμενων που κανουν αναπαραγωγη
 * toCheck : boolean που ενεργοποιει την επιλογη πολλαπλων αντικειμενων
 *
 *
 */
public class CustomListHandler  {
    private  CustomToolbarHandler customToolbarHandler;
    private CustomListListener customListListener;
    private final GridView myGridView;

    private static ArrayList<CustomItem> myList,filesToDelete;
    private final CustomArrayAdapter arrayAdapter;

    private final int itemCheckedColor;
    private final Drawable defaultBackground;

    private int counter=0,itemsPlaying=0;
    private boolean toCheck=false;

    private Context context;

    /**
     * Constructor της κλασης
     * Δημιουργει το myGridView,τον customToolbarHandler,την myList και οριζει τα
     * itemCheckedColor & defaultBackground
     * @param view Το view στο οποιο βρισκεται το myGridView,δηλαδη το voice_record_activity
     * @param context
     */
    public CustomListHandler(View view, Context context){
        this.context=context;
        myGridView =view.findViewById(R.id.gridView);

        itemCheckedColor=context.getResources().getColor(R.color.green_200);
        defaultBackground =context.getResources().getDrawable(R.drawable.item_gradient);

        initCustomToolbarHandler(view);
        initArrayLists(view);

        arrayAdapter = new CustomArrayAdapter(context,0,myList);
        myGridView.setAdapter(arrayAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomItem item = (CustomItem)parent.getItemAtPosition(position);
                clicked(view,item);
            }
        });

        myGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!toCheck){
                    CustomItem item = (CustomItem)parent.getItemAtPosition(position);
                    longClick(view,item);
                    return true;
                }
                return false;

            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!toCheck){
                    for (int i=0;i<myList.size();i++){
                        CustomItem item=myList.get(i);
                        View tempView=myGridView.getChildAt(i);
                        if (item.isBackgroundColorEdited()){
                            int color=item.getBackgroundColor();
                            tempView.setBackgroundColor(color);
                        }else{
                            // tempView.setBackground(defaultBackground);
                        }
                        if(item.isTextColorEdited()){
                            System.out.println("~~~\n" + item.getDesc() );
                            TextView textView=tempView.findViewById(R.id.desc);
                            textView.setTextColor(item.getTextColor());
                        }else{
                            TextView textView=tempView.findViewById(R.id.desc);
                            textView.setTextColor(Color.WHITE);
                        }
                        item.setView(tempView);
                    }
                }
            }
        });
    }

    /**
     * Δημιουργια του customToolbarHandler
     * @param view  Το view στο οποιο βρισκεται το toolbar,δηλαδη το voice_record_activity
     *              χρησιμοποιεται στην αρχικοποιση των imageView,textView και appBarLayout.
     */
    private void initCustomToolbarHandler(View view){
        CustomToolbarHandler.CustomToolbarListener customToolbarListener = new CustomToolbarHandler.CustomToolbarListener() {
            @Override
            public void onCancelImagePressed() {
                cancel();
            }

            @Override
            public void onDeleteImagePressed() { delete(); }

            @Override
            public void onEditImagePressed() { startCustomizeItemActivity(); }
        };
        customToolbarHandler=new CustomToolbarHandler(view);
        customToolbarHandler.setListener(customToolbarListener);
        customToolbarHandler.setContext(context);
    }

    /**
     * Δημιουργια της λιστας myList
     * @param view Το view στο οποιο βρισκεται το toolbar,δηλαδη το voice_record_activity
     *             χρησιμοποιεται στην αρχικοποιση των imageView,textView και appBarLayout.
     */
    private void initArrayLists(View view){
        myList=new ArrayList<>();

        File directory = new File(view.getContext().getExternalFilesDir(null) + "/MyRecording/"+"");
        File[] files = directory.listFiles();

        int fileLength=files.length;
        //βαζει στην λιστα ολα τα αρχεια που υπαρχουν αποθηκευμενα
        for (int i=0;i<fileLength;i++){

            myList.add(new CustomItem(files[i].toString(),"" + i)); //Το i θα αλλαξει οταν βαλουμε την βαση
            myList.get(i).setListener(new CustomItem.customItemListener() {
                @Override
                public void onItemFinished() {
                    itemsPlaying--;
                    customListListener.onStopPlaying();
                }
            });
        }

        filesToDelete=new ArrayList<>();
    }

    /**
     *
     * @return Επιστρεφει True οταν ειναι ενεργοποιημενη
     * η επιλογη πολλαπλων αντικειμενων
     * False οταν δεν ειναι
     */
    public boolean isToCheck(){
        return toCheck;
    }

    /**
     * καλειται οταν πατιεται ενα item
     * Ελεγχει αν ειναι ενεργοποιημενη η επιλογη πολλαπλων αντικειμενων
     *      αν οχι,ελεγχει αν το αντικειμενο αναπαραγεται,
     *          αν ναι το σταματαει με την stop() της CustomItem
     *          αν οχι,το ξεκιναει με την start(String path) της CustomItem
     *      Αν ειναι ενεργοποιημενη η επιλογη πολλαπλων αντικειμενων
     *          ελεγχει αν το αντικειμενο ειναι επιλεγμενο με την isChecked() της CustomItem
     *                  αν ειναι το αποεπιλεγει και στην συνεχεια
     *                      ελεγχει αν υπαρχουν αλλα επιλεγμενα αντικειμενα
     *                          αν δεν υπαρχουν θετει την toCheck = false και κρυβει το toolbar μεσω της
     *                          hide() του CustomToolbarHandler
     *
     *                  αν το αντικειμενο δεν ειναι επιλεγμενο το επιλεγει.
     * @param view το View του αντικειμενου που πατηθηκε,χρησιμοποιειται για την αλλαγη χρωματος
     * @param item το CustomItem το οποιο επιλεγθηκε
     */
    private void clicked(View view, CustomItem item){
        System.out.println(item.toString());
        if(!toCheck){
            if (!item.isPlaying()){
                item.start(item.getPath());
                itemsPlaying++;
                customListListener.onStartPlaying();
            }else{
                item.stop();
                itemsPlaying--;
                customListListener.onStopPlaying();
            }
        }else{
            if (item.isChecked()){
                itemUncheck(view,item);
                if (!areItemsChecked()){
                    toCheck=false;
                    customToolbarHandler.hide();
                }
            }else{
                itemCheck(view,item);
            }
            if (customToolbarHandler.getCounter()>1){
                customToolbarHandler.hideEditImage();
            }else{
                customToolbarHandler.showEditImage();
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Καλειται οταν γινεται longclick σε καποιο αντικειμενο για να το επιλεξει
     * και ενεργοποιειται η επιλογη πολλαπλων αντικειμενων
     * @param view το View του αντικειμενου που πατηθηκε,χρησιμοποιειται για την αλλαγη χρωματος
     * @param item το CustomItem το οποιο επιλεγθηκε
     */
    private void longClick(View view, CustomItem item){
        stop();
        toCheck=true;
        customToolbarHandler.show();
        itemCheck(view,item);
    }

    /**
     * Ελεγχος αν υπαρχουν επιλεγμενα αντικειμενα
     * @return true αν υπαρχουν , false αν δεν υπαρχουν.
     */
    private boolean areItemsChecked(){
        return counter != 0;
    }

    /**
     * Αποεπιλεγει το αντικειμενο και επαναφερει το χρωμα του στο default
     * @param view το View του αντικειμενου που πατηθηκε,χρησιμοποιειται για την αλλαγη χρωματος
     * @param item το CustomItem το οποιο επιλεγθηκε
     */
    private void itemUncheck(View view, CustomItem item){
        counter--; //μειωνονται τα επιλεγμενα αντικειμενα
        if (item.isBackgroundColorEdited()){
            int color=item.getBackgroundColor();
            view.setBackgroundColor(color);
        }else{
           view.setBackground(defaultBackground); //επιστρεφει το χρωμα στο default
        }
        item.uncheck(); // αποεπιλεγει το αντικειμενο
        customToolbarHandler.decrease();
        filesToDelete.remove(item);
    }

    /**
     * Επιλεγεται το αντικειμενο και αλλαζει το χρωμα του στο itemCheckedColor
     * @param view το View του αντικειμενου που πατηθηκε,χρησιμοποιειται για την αλλαγη χρωματος
     * @param item το CustomItem το οποιο επιλεγθηκε
     */
    private void itemCheck(View view, CustomItem item){
        counter++; //αυξανονται τα επιλεγμενα αντικειμενα
        view.setBackgroundColor(itemCheckedColor); //γινεται πρασσινο το αντικειμενο για να δειξει οτι εχει επιλεγθει
        item.check(); //επιλεγεται το αντικειμενο
        customToolbarHandler.increase();
        filesToDelete.add(item);
    }

    /**
     * Προσθέτει το αρχειο στη λιστα καθως δημιουργει
     * και τον customItemListener του
     * και το εμφανιζει
     * @param path το μονοπατι του αρχειου
     * @param desc χρησιμοποιειται στην εμφανιση του
     */
    public void addToList(String path, String desc){
        CustomItem.customItemListener customItemListener=new CustomItem.customItemListener() {
            @Override
            public void onItemFinished() {
                itemsPlaying--;
                customListListener.onStopPlaying();
            }
        };
        myList.add(new CustomItem(path,desc));
        myList.get(myList.size() - 1 ).setListener(customItemListener);
        arrayAdapter.notifyDataSetChanged();
    }

    //TODO
    public void replace(int i,CustomItem item){
        item.setListener(new CustomItem.customItemListener() {
            @Override
            public void onItemFinished() {
                itemsPlaying--;
                customListListener.onStopPlaying();
            }
        });
        myList.set(i,item);

        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Σταματανε να παιζουν ολα για να γινει ηχογραφηση
     */
    protected void stop(){
        itemsPlaying=0;
        for (int i = 0; i < myList.size(); i++) {
            CustomItem item = myList.get(i);
            if (item.isPlaying()) {
                item.stop();
               // myGridView.getChildAt(i).findViewById(R.id.soundOnImageView).setVisibility(View.GONE);
            }
            customListListener.onStopPlaying();
        }

    }

    /**
     * Καλειται απτην onDeletePressed του CustomToolbarListener
     * Διαγραφει τα επιλεγμενα αντικειμενα απτην myList και το myGridView και τα αντιστοιχα αρχεια τους
     * Αποεπιλεγει ολα τα αντικειμα του myGridView επειδη με την διαγραφη τους απτην
     * λιστα συχνα καποια επαιρναν την θεση καποια διαγραμενου αρχειου και εμεναν επιλεγμενα
     * Απενεργοποιει την επιλογη πολλαπλων αντικειμενων και κανει reset τον counter
     */
    private void delete(){
        int i=0;

        for (CustomItem item : filesToDelete) {
            new File(item.getPath()).delete();
        }

        while(i<myList.size()){
            if (myList.get(i).isChecked()){
                myList.remove(i);
            }else{
                i++;
            }
        }
        for (i=0;i<myList.size();i++){
            View view = myGridView.getChildAt(i);
            itemUncheck(view,myList.get(i));
        }
        toCheck=false;
        arrayAdapter.notifyDataSetChanged();
        counter=0;
    }

    /**
     *  Καλειται απτην onCancelPressed του CustomToolbarListener και την backPressed()
     *  Αποεπιλεγει ολα τα αντικειμενα
     *  Κρυβει το toolbar
     *  Απενεργοποιει την επιλογη πολλαπλων αντικειμενων και κανει reset τον counter
     */
    public void cancel(){
        customToolbarHandler.hide();
        for (int i=0;i<myList.size();i++){
            View view=myGridView.getChildAt(i);
            itemUncheck(view,myList.get(i));
        }
        toCheck=false;
        counter=0;
    }

    //TODO
    private void startCustomizeItemActivity(){
        CustomItem myItem=null;
        int index=0;
        for (int i = 0; i < myList.size(); i++) {
            CustomItem item = myList.get(i);
            if (item.isChecked()) {
                myItem = item;
                index=i;
                break;
            }
        }
        Intent intent=new Intent(context.getApplicationContext(),CustomizeItemActivity.class);
        intent.putExtra("item",myItem);
        intent.putExtra("index",index);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     *
     * @return Επιστρεφει True αν γινεται αναπαραγωγη καποιου αντικειμενου, False αν οχι
     */
    public boolean areItemsPlaying(){
        return itemsPlaying > 0;
    }

    /**
     * Καλειται απτην VoiceRecordActivity οταν πατιεται το back button.
     * Αν ειναι ενεργη η επιλογη πολλαπλων αντικειμενων την απενεργοποιει
     */
    public void backPressed(){
        if(toCheck=true){
            cancel();
        }
    }

    /**
     * αρχικοποιηση του customListListener
     * @param customListListener Interface το οποιο πρεπει να γινει override
     */
    public void setCustomListListener(CustomListListener customListListener){
        this.customListListener =customListListener;
    }

    //TODO
    public void setAutoLooping(Boolean b){
        for (CustomItem item : myList){
            item.setToAutoLoop(b);
        }
    }

    /**
     * interface της κλασης,χρησιμοποιειται απο την VoiceRecordActivity
     * η συναρτηση onStartPlaying καλειται οταν ενα αντικειμενο ξεκιναει την αναπαραγωγη
     * η συναρτηση onStopPlaying καλειται οταν αντικειμενο σταματησει την αναπαραγωγη,
     * ειτε λογω χρηστη ειτε επειδη τελειωσε
     */
    public interface CustomListListener{
        void onStartPlaying();
        void onStopPlaying();
    }


}
