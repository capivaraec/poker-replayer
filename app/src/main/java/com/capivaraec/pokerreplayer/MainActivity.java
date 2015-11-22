package com.capivaraec.pokerreplayer;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.capivaraec.pokerreplayer.components.HandInfo;
import com.capivaraec.pokerreplayer.components.Player;
import com.capivaraec.pokerreplayer.filebrowser.FileBrowserActivity;
import com.capivaraec.pokerreplayer.history.History;
import com.capivaraec.pokerreplayer.history.HistoryReader;
import com.capivaraec.pokerreplayer.utils.Cache;
import com.dropbox.chooser.android.DbxChooser;

import java.io.File;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    private static final int DBX_CHOOSER_REQUEST = 0;
    private static final int DEVICE_CHOOSER_REQUEST = 1;
    private DbxChooser mChooser;
    private Dialog mBottomSheetDialog;
    private boolean mShowingBack;
    private final Handler mHandler = new Handler();
    private static HandInfo handInfo;
    private File file;
    private ProgressDialog progress;
    private History history;
    private int currentHand;
    private int currentAction;
    private Player[] players;
    private Button btnPreviousHand;
    private Button btnPreviousAction;
    private Button btnNextAction;
    private Button btnNextHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setBottomSheet();

        handInfo = (HandInfo) findViewById(R.id.hand_info);
        btnPreviousHand = (Button) findViewById(R.id.button_previous_hand);
        btnPreviousAction = (Button) findViewById(R.id.button_previous_action);
        btnNextAction = (Button) findViewById(R.id.button_next_action);
        btnNextHand = (Button) findViewById(R.id.button_next_hand);

        String filePath = Cache.getFilePath(this);
        if (filePath != null) {
            File file = new File(filePath);
            history = Cache.readHistory(this, file.getName());
            if (history == null) {
                return;
            }
            currentHand = Cache.getCurrentHand(this);
            setPlayers();
            showNavigationBar();
            readHand();
        }
    }

    private void showNavigationBar() {
        btnPreviousHand.setVisibility(View.VISIBLE);
        btnPreviousAction.setVisibility(View.VISIBLE);
        btnNextAction.setVisibility(View.VISIBLE);
        btnNextHand.setVisibility(View.VISIBLE);
        handInfo.setVisibility(View.VISIBLE);
    }

    private void setBottomSheet() {
        mBottomSheetDialog = new Dialog(MainActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.bottom_sheet);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setPlayers() {
        players = new Player[history.getNumPlayers()];
        int[] positions = getPlayersPositions(history.getNumPlayers());

        for(int x = 0; x <= history.getNumPlayers() - 1; x++) {
            String playerId = "player_" + positions[x];
            int resID = getResources().getIdentifier(playerId, "id", getPackageName());
            players[x] = (Player) findViewById(resID);
            players[x].setVisibility(View.VISIBLE);
        }
    }

    private int[] getPlayersPositions(int numPlayers) {
        int[] positions = null;
        switch (numPlayers) {
            case 2:
                positions = new int[]{3, 7};
                break;
            case 3:
                positions = new int[]{3, 5, 7};
                break;
            case 4:
                positions = new int[]{1, 4, 6, 9};
                break;
            case 5:
                positions = new int[]{1, 3, 5, 7, 9};
                break;
            case 6:
                positions = new int[]{2, 3, 5, 7, 8, 10};
                break;
            case 7:
                positions = new int[]{1, 3, 4, 5, 6, 7, 9};
                break;
            case 8:
                positions = new int[]{1, 2, 3, 4, 6, 7, 8, 9};
                break;
            default:
                positions = new int[numPlayers];
                for (int x = 0; x < numPlayers; x++) {
                    positions[x] = x + 1;
                }
        }

        return positions;
    }

    public void openBottomSheet(View v) {
        mBottomSheetDialog.show();
    }

    private void closeBottomSheet() {
        mBottomSheetDialog.hide();
    }

    public void openDeviceBrowser(View v) {
        closeBottomSheet();
        Intent intent = new Intent(this, FileBrowserActivity.class);
        startActivityForResult(intent, DEVICE_CHOOSER_REQUEST);
    }

    public void openDropboxBrowser(View v) {
        closeBottomSheet();
        if (mChooser == null) {
            mChooser = new DbxChooser("xwctj62gvp3l598");
        }
        mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(MainActivity.this, DBX_CHOOSER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            startProgress();

            boolean open = false;
            if (requestCode == DBX_CHOOSER_REQUEST) {
                DbxChooser.Result result = new DbxChooser.Result(data);

                file = new File(result.getLink().getPath());
                open = true;
            } else if (requestCode == DEVICE_CHOOSER_REQUEST) {
                file = (File) data.getSerializableExtra("result");
                open = true;
            } else {
                super.onActivityResult(requestCode, resultCode, data);
                progress.dismiss();
            }

            if (open) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        history = HistoryReader.readFile(file);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                newHistory();
                            }
                        });
                    }
                };

                thread.start();
            }
        }
    }

    private void newHistory() {
        if (history != null) {
            Cache.writeHistory(this, file, history);
        }
        recreate();
    }

    private void readHand() {
        setButtonsEnabled();
        handInfo.setBlinds(currentAction, currentHand, 0, this);
    }

    private void readAction() {
        setButtonsEnabled();
    }

    private void setButtonsEnabled() {
        btnPreviousHand.setEnabled(false);
        btnPreviousAction.setEnabled(false);
        btnNextHand.setEnabled(false);
        btnNextAction.setEnabled(false);

        if (currentHand != 0) {
            btnPreviousHand.setEnabled(true);
        }

        if (currentHand < history.getHands().size() - 1) {
            btnNextHand.setEnabled(true);
        }

        if (currentAction != 0) {
            btnPreviousAction.setEnabled(true);
        }

        if (currentAction < history.getHand(currentHand).getActions().size() - 1) {
            btnNextAction.setEnabled(true);
        }
    }

    public void previousHand(View v) {
        currentHand--;
        currentAction = 0;

        Cache.setCurrentHand(this, currentHand);
        readHand();
    }

    public void previousAction(View v) {
        currentAction--;
        readAction();
    }

    public void nextAction(View v) {
        currentAction++;
        readAction();
    }

    public void nextHand(View v) {
        currentHand++;
        currentAction = 0;

        Cache.setCurrentHand(this, currentHand);
        readHand();
    }

    private void startProgress() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle(getString(R.string.progress_title));
            progress.setMessage(getString(R.string.progress_message));
        }

        progress.show();
    }
}
