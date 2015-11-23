package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

import java.text.DecimalFormat;

public class LayoutPlayer extends LinearLayout {

    private final TextView tvName;
    private final TextView tvStack;

    public LayoutPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.player, this);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvStack = (TextView) findViewById(R.id.tv_stack);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setStack(float stack) {//TODO: verificar se é cash e colocar a moeda correta (dólar, euro etc)
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String strStack = "$ " + decimalFormat.format(stack);
        tvStack.setText(strStack);
    }
}
