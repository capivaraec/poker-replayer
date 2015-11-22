package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HandInfo extends LinearLayout {

    private String blinds;
    private String pot;
    private String potOdds;
    private final ListView listView;
    private HandInfoAdapter adapter;
    private ArrayList <HashMap<String, String>> list = new ArrayList<>();
    private int index;
    private int top;

    public HandInfo(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.hand_info, this);

        listView = (ListView) findViewById(R.id.listView);
        setAdapter(context);
    }

    private void setAdapter(Context context) {
        HashMap<String, String> hashMap = new HashMap<>(1);
        hashMap.put("title", "Blinds");

        list.add(hashMap);

        hashMap = new HashMap<>(1);
        hashMap.put("title", "Pot");

        list.add(hashMap);

        hashMap = new HashMap<>(1);
        hashMap.put("title", "Pot odds");

        list.add(hashMap);

        hashMap = new HashMap<>(1);
        hashMap.put("title", "teste");

        list.add(hashMap);

        adapter = new HandInfoAdapter(context, list);
        listView.setAdapter(adapter);
    }

    private void saveScrollPosition() {
        index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
    }

    private void restoreScrollPosition() {
        listView.setSelectionFromTop(index, top);
    }

    public void setBlinds(double smallBlind, double bigBlind, double ante, Context context) {
        HashMap<String, String> hashMap = list.get(0);
        hashMap.put("description", smallBlind + "/" + bigBlind);

        saveScrollPosition();

        adapter = new HandInfoAdapter(context, list);
        listView.setAdapter(adapter);

        restoreScrollPosition();
    }

    public void setPot(float pot) {
        String strPot = "$ " + pot;
    }

    public void setPotOdds(float potOdds) {
        String strPotOdds = "$ " + potOdds;
    }
}
