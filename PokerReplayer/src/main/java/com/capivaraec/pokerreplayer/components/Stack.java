package com.capivaraec.pokerreplayer.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

public class Stack extends LinearLayout {

    private Context context;
    private boolean stackGoesToRight;
    private RelativeLayout layoutStack;
    private TextView tvStack;

    public Stack(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.stack, this);

        layoutStack = (RelativeLayout) findViewById(R.id.layout_stack);
        tvStack = (TextView) findViewById(R.id.tv_stack);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.Stack);
        stackGoesToRight = arr.getBoolean(R.styleable.Stack_stackGoesToRight, true);

        arr.recycle();
    }

    public void setStack(float stack) {
        //TODO: limpar stacks
        //TODO: lógica para empilhar as fichas
        //TODO: identificar se novas pilhas vão para a direita ou esquerda
    }
}
