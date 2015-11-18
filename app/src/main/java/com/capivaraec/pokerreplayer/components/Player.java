package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

public class Player extends LinearLayout {

    public String name;
    public String stack;
    private TextView tvName;
    private TextView tvStack;

    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.player, this);

        tvName = (TextView) findViewById(R.id.name);
        tvStack = (TextView) findViewById(R.id.stack);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setStack(float stack) {
        String strStack = "$ " + stack;
        tvStack.setText(strStack);
    }
}
