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
    }

    public void setStack(float stack) {//TODO: verificar se é cash e colocar a moeda correta (dólar, euro etc)
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String strStack = "$ " + decimalFormat.format(stack);
        tvStack.setText(strStack);
    }

    private void setStack(String text) {
        tvStack.setText(text);
    }

    private void changeAction(String action) {
        String name = tvName.getText().toString();
        tvName.setText(action);
        tvName.setTextColor(Color.BLUE);

        Timer timer = new Timer();
        timer.schedule(new ActionTask(name, tvName), 1000);
    }

    public void setAction(ActionID action) {
        String strAction = null;
        boolean up = true;

        switch (action) {
            case BET:
                strAction = "Bet";
                break;
            case RAISE:
                strAction = "Raise";
                break;
            case CALL:
                strAction = "Call";
                break;
            case FOLD:
                strAction = "Fold";
                break;
            case ALL_IN:
                strAction = "All in";
                up = false;
                break;
        }

        if (strAction != null) {
            if (up) {
                changeAction(strAction);
            } else {
                setStack(strAction);
            }
        }

        // check, call, fold, raise -> azul em cima
        // sitting out, all in -> amarelo em baixo
    }

    private class ActionTask extends TimerTask {

        private String playerName;
        private TextView textView;

        public ActionTask(String playerName, TextView textView) {
            this.playerName = playerName;
            this.textView = textView;
        }

        @Override
        public void run() {
            ((Activity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    textView.setText(playerName);
                    textView.setTextColor(Color.BLACK);
                }
            });
        }

    }
}
