package com.example.mobiledevergasia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Adapter που προτιθεται στο gridView. Προσθετει τα CustomItems.
 */
public class CustomArrayAdapter extends ArrayAdapter<CustomItem> {
    private View view;
    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CustomItem> customItems) {
        super(context, resource, customItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        view=convertView;
        if (view == null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);
        }

        CustomItem currentItem= getItem(position);
        TextView desc=view.findViewById(R.id.desc);
        desc.setText(currentItem.getDesc());
        ImageView imageView=view.findViewById(R.id.soundOnImageView);
        imageView.setImageResource(R.drawable.sound_on_image);

        return view;

    }

}