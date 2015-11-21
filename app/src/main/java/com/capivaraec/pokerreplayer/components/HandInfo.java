package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

public class HandInfo extends LinearLayout {

    private String blinds;
    private String pot;
    private String potOdds;
    private final TextView tvBlinds;
    private final TextView tvPot;
    private final TextView tvPotOdds;

    public HandInfo(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.hand_info, this);

        tvBlinds = (TextView) findViewById(R.id.tv_blinds);
        tvPot = (TextView) findViewById(R.id.tv_pot);
        tvPotOdds = (TextView) findViewById(R.id.tv_pot_odds);

        setPot(12);
        setPotOdds(150);
    }

    public void setBlinds(String blinds) {
        tvBlinds.setText(blinds);
    }

    public void setPot(float pot) {
        String strPot = "$ " + pot;
        tvPot.setText(strPot);
    }

    public void setPotOdds(float potOdds) {
        String strPotOdds = "$ " + potOdds;
        tvPotOdds.setText(strPotOdds);
    }
}
