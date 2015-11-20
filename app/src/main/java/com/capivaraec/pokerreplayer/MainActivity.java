package com.capivaraec.pokerreplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Player player1 = (Player) findViewById(R.id.player_1);
        player1.setName("Marcelo_CAP");
        player1.setStack(1200);
    }

    public void openFileBrowser(View v) {
        //TODO: exibir alert com as opções de chooser (dispositivo, Dropbox, Google Drive ou OneDrive)
        openDropbox();

//        Intent intent = new Intent(this, FileBrowserActivity.class);
//        startActivityForResult(intent, 1);
    }

    private void openDropbox() {
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
