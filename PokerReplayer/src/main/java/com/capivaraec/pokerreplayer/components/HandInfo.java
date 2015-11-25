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
    private final ArrayList <HashMap<String, String>> list = new ArrayList<>();
    private int index;
    private int top;
    private final Context context;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private DecimalFormat potOddsFormat = new DecimalFormat("#.#");

    private enum Info {
        BLINDS(0, "Blind"), POT(1, "Pot"), POT_ODDS(2, "Pot odds"), HAND(3, "Hand"), TABLE(4, "Table"), BUY_IN(5, "Buy-in"), GAME(6, "Game"), ROOM(7, "Room"), DATE(8, "Date");

        private final int value;
        private final String title;
        Info(int value, String title) {
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
        StringBuilder blinds = new StringBuilder(decimalFormat.format(smallBlind) + "/" + decimalFormat.format(bigBlind));

        if (ante > 0) {
            blinds.append(" (+").append(decimalFormat.format(ante)).append(")");
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

    public void setTable(String table) {
        HashMap<String, String> hashMap = list.get(Info.TABLE.getValue());
        hashMap.put("description", table);
    }

    public void setPotOdds(float pot, String name, float total, float valueSpent) {
        HashMap<String, String> hashMap = list.get(Info.POT.getValue());
        hashMap.put("description", decimalFormat.format(pot));

        hashMap = list.get(Info.POT_ODDS.getValue());
        if (name == null || total == 0) {
            hashMap.put("description", "");
        } else {
            float toCall = total - valueSpent;
            String percentage = getPercentagePotOdds(pot, toCall);
            String ratio = getRatioPotOdds(pot, toCall);
            String oddsFormat = "%s - %s - %s";
            hashMap.put("description", String.format(oddsFormat, name, percentage, ratio));
        }
    }

    private String getRatioPotOdds(float pot, float toCall) {
        return potOddsFormat.format((pot / toCall)) + ":1";
    }

    private String getPercentagePotOdds(float pot, float toCall) {
        float potTotal = pot + toCall;
        float potOdds = toCall * 100 / potTotal;

        return potOddsFormat.format(potOdds) + "%";
    }

    public void setBuyIn(String buyIn) {
        HashMap<String, String> hashMap = list.get(Info.BUY_IN.getValue());
        hashMap.put("description", buyIn);
    }

    public void setGame(String game) {
        HashMap<String, String> hashMap = list.get(Info.GAME.getValue());
        hashMap.put("description", game);
    }
}
