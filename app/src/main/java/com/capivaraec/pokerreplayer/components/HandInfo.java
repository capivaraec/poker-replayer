package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.capivaraec.pokerreplayer.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class HandInfo extends LinearLayout {

    private final ListView listView;
    private HandInfoAdapter adapter;
    private ArrayList <HashMap<String, String>> list = new ArrayList<>();
    private int index;
    private int top;
    private Context context;

    private enum Info {
        BLINDS(0, "Blind"), POT(1, "Pot"), POT_ODDS(2, "Pot odds"), HAND(3, "Hand"), TABLE(4, "Table"), GAME(5, "Game"), DATE(6, "Date");

        private final int value;
        private final String title;
        private Info(int value, String title) {
            this.value = value;
            this.title = title;
        }

        public int getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }
    }

    public HandInfo(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.hand_info, this);

        listView = (ListView) findViewById(R.id.listView);
        setAdapter(context);
    }

    private void setAdapter(Context context) {
        setTitles();

        adapter = new HandInfoAdapter(context, list);
        listView.setAdapter(adapter);
    }

    private void setTitles() {
        for (Info info : Info.values()) {
            HashMap<String, String> hashMap = new HashMap<>(1);
            hashMap.put("title", info.getTitle());
            list.add(hashMap);
        }
    }

    private void saveScrollPosition() {
        index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
    }

    private void restoreScrollPosition() {
        listView.setSelectionFromTop(index, top);
    }

    public void updateAdapter() {
        saveScrollPosition();

        adapter = new HandInfoAdapter(context, list);
        listView.setAdapter(adapter);

        restoreScrollPosition();
    }

    public void setBlinds(double smallBlind, double bigBlind, double ante) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder blinds = new StringBuilder(decimalFormat.format(smallBlind) + "/" + decimalFormat.format(bigBlind));

        if (ante > 0) {
            blinds.append(" (+" + decimalFormat.format(ante) + ")");
        }

        HashMap<String, String> hashMap = list.get(Info.BLINDS.getValue());
        hashMap.put("description", blinds.toString());
    }

    public void setDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());

        HashMap<String, String> hashMap = list.get(Info.DATE.getValue());
        hashMap.put("description", dateFormat.format(date));
    }

    public void setHandNumber(int handNumber, int totalHands) {
        HashMap<String, String> hashMap = list.get(Info.HAND.getValue());
        hashMap.put("description", ++handNumber + "/" + totalHands);
    }

    public void setTable(int table) {
        HashMap<String, String> hashMap = list.get(Info.TABLE.getValue());
        hashMap.put("description", String.valueOf(table));
    }

    public void setPot(float pot) {
        String strPot = "$ " + pot;
    }

    public void setPotOdds(float potOdds) {
        String strPotOdds = "$ " + potOdds;
    }

    public void setGame(String game) {
        HashMap<String, String> hashMap = list.get(Info.GAME.getValue());
        hashMap.put("description", game);
    }
}
