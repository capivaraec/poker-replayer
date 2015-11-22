package com.capivaraec.pokerreplayer.components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by marcelobogdanovicz on 11/22/15.
 */
public class HandInfoAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    private ArrayList<HashMap<String, String>> list;

    public HandInfoAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        super(context, R.layout.hand_info_item, list);

        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FileHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.hand_info_item, parent, false);

            holder = new FileHolder();
            holder.tvTitle = (TextView) row.findViewById(R.id.tv_title);
            holder.tvDescription = (TextView) row.findViewById(R.id.tv_description);

            row.setTag(holder);
        } else {
            holder = (FileHolder) row.getTag();
        }

        holder.tvTitle.setText(list.get(position).get("title") + ": ");
        holder.tvDescription.setText(list.get(position).get("description"));

        return row;
    }

    private static class FileHolder {
        TextView tvTitle;
        TextView tvDescription;
    }

}
