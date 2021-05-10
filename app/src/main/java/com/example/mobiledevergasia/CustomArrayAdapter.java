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
    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CustomItem> customItems) {
        super(context, resource, customItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if (listItemView == null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);
        }

        CustomItem currentItem= getItem(position);
        TextView desc=listItemView.findViewById(R.id.desc);
        desc.setText(currentItem.getDesc());
        ImageView imageView=listItemView.findViewById(R.id.soundOnImageView);
        imageView.setImageResource(R.drawable.sound_on_image);

        return listItemView;

    }

}