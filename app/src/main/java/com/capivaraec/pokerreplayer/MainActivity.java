package com.capivaraec.pokerreplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.capivaraec.pokerreplayer.components.Player;
import com.capivaraec.pokerreplayer.filebrowser.FileBrowserActivity;
import com.dropbox.chooser.android.DbxChooser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "preferences";
    private static final String FILE_PATH = "file_path";
    private static final int DBX_CHOOSER_REQUEST = 0;
    private DbxChooser mChooser;
    private Dialog mBottomSheetDialog;

    private Player[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setBottomSheet();

        setPlayers();
    }

    private void setBottomSheet() {
        mBottomSheetDialog = new Dialog(MainActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.bottom_sheet);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setPlayers() {
        //TODO: chamar este método apenas após carregar o torneio (se for 6-max, carregar somente 6 jogadores)
        players = new Player[10];

        for(int x = 1; x <= 10; x++) {
            String playerId = "player_" + x;
            int resID = getResources().getIdentifier(playerId, "id", getPackageName());
            players[x - 1] = (Player) findViewById(resID);
        }
    }

    public void openBottomSheet(View v) {
        mBottomSheetDialog.show();
    }

    public void closeBottomSheet(View v) {
        mBottomSheetDialog.hide();
    }

    public void openDeviceBrowser(View v) {
        closeBottomSheet(null);
        Intent intent = new Intent(this, FileBrowserActivity.class);
        startActivityForResult(intent, 1);
    }

    public void openDropboxBrowser(View v) {
        closeBottomSheet(null);
        if (mChooser == null) {
            mChooser = new DbxChooser("xwctj62gvp3l598");
        }
        mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(MainActivity.this, DBX_CHOOSER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                System.out.println(result.getLink()); //endereço do arquivo no dispositivo

                // Handle the result
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
