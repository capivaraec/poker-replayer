package com.capivaraec.pokerreplayer;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final int DBX_CHOOSER_REQUEST = 0;
    private static final int DEVICE_CHOOSER_REQUEST = 1;
    private DbxChooser mChooser;
    private Dialog mBottomSheetDialog;
    private boolean mShowingBack;
    private Handler mHandler = new Handler();
    private static HandInfo handInfo;
    private File file;
    private ProgressDialog progress;
    private History history;
    private int currentHand;
    private Player[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setBottomSheet();

        handInfo = (HandInfo) findViewById(R.id.hand_info);

        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        String filePath = Cache.getFilePath(this);
        if (filePath != null) {
            File file = new File(filePath);
            history = Cache.readHistory(this, file.getName());
            if (history == null) {
                return;
            }
            currentHand = Cache.getCurrentHand(this);
            setPlayers();
            //TODO: readHand();
        }
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

        for(int x = 1; x <= history.getNumPlayers(); x++) {
            String playerId = "player_" + x;
            int resID = getResources().getIdentifier(playerId, "id", getPackageName());
            players[x - 1] = (Player) findViewById(resID);
            players[x - 1].setVisibility(View.VISIBLE);
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
        startActivityForResult(intent, DEVICE_CHOOSER_REQUEST);
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
        //TODO: salvar histórico atual para ser aberto após recriação
        recreate();
    }

    public void previousHand(View v) {
        Cache.setCurrentHand(this, currentHand);
    }

    public void previousAction(View v) {
    }

    public void nextAction(View v) {
    }

    public void nextHand(View v) {
        Cache.setCurrentHand(this, currentHand);
    }

    private void startProgress() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
        }

        progress.show();
    }

    public void flipCard(View v) {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                        R.anim.card_flip_left_in, R.anim.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, new CardBackFragment())

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {
        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragment extends Fragment {
        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }
}
