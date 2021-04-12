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

public class CustomArrayAdapter extends ArrayAdapter<CustomItem> {
    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CustomItem> customLists ) {
        super(context, resource, customLists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if (listItemView == null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.line_layout,parent,false);
        }

        CustomItem currentItem= getItem(position);
        ImageView image=(ImageView)listItemView.findViewById(R.id.image);
        TextView desc=listItemView.findViewById(R.id.desc);
        desc.setText(currentItem.getDesc());

        return listItemView;

    }

}