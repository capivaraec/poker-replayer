package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

public class Player extends LinearLayout {

    private String name;
    private String stack;
    private final TextView tvName;
    private final TextView tvStack;

    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.player, this);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvStack = (TextView) findViewById(R.id.tv_stack);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setStack(float stack) {
        String strStack = "$ " + stack;
        tvStack.setText(strStack);
    }
}
