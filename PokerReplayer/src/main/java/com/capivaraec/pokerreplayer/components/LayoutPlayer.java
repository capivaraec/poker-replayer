package com.capivaraec.pokerreplayer.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;
import com.capivaraec.pokerreplayer.enums.ActionID;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class LayoutPlayer extends LinearLayout {

    private final String allIn = "All in";
    private String name;
    private final TextView tvName;
    private final TextView tvStack;
    private final Context context;

    public LayoutPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.player, this);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvStack = (TextView) findViewById(R.id.tv_stack);
        this.context = context;
    }

    public void setName(String name) {
        tvName.setText(name);
        this.name = name;
    }

    public void setStack(float stack) {//TODO: verificar se é cash e colocar a moeda correta (dólar, euro etc)
        if (stack == 0) {
            setStack(allIn);
            return;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String strStack = "$ " + decimalFormat.format(stack);
        tvStack.setText(strStack);
    }

    private void setStack(String text) {
        tvStack.setText(text);
    }

    private void changeAction(String action) {
        tvName.setText(action);
        tvName.setTextColor(Color.BLUE);

        Timer timer = new Timer();
        timer.schedule(new ActionTask(tvName), 1000);
    }

    public void setAction(ActionID action) {
        String strAction = null;

        switch (action) {
            case CHECK:
                strAction = "Check";
                break;
            case BET:
            case BET_ALL_IN:
                strAction = "Bet";
                break;
            case RAISE:
            case RAISE_ALL_IN:
                strAction = "Raise";
                break;
            case CALL:
            case CALL_ALL_IN:
                strAction = "Call";
                break;
            case FOLD:
                strAction = "Fold";
                break;
        }

        switch (action) {
            case ALL_IN:
            case BET_ALL_IN:
            case RAISE_ALL_IN:
            case CALL_ALL_IN:
                setStack(allIn);
                break;
        }

        if (strAction != null) {
            changeAction(strAction);
        }

        // check, call, fold, raise -> azul em cima
        // sitting out, all in -> amarelo em baixo
    }

    private class ActionTask extends TimerTask {

        private final TextView textView;

        public ActionTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void run() {
            ((Activity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    textView.setText(name);
                    textView.setTextColor(Color.BLACK);
                }
            });
        }

    }
}
