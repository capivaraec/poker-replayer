package com.capivaraec.pokerreplayer.filebrowser;

import java.io.File;
import java.io.FileFilter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.capivaraec.pokerreplayer.R;
import com.capivaraec.pokerreplayer.utils.Cache;

public class FileBrowserActivity extends Activity {

	private static final String DIRECTORY = "directory";
    private ActionBar actionBar;
	private ListView listView;
	private File currentDirectory;
	private File[] files;
	private final FileFilter filter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			boolean directory = pathname.isDirectory();
			boolean txt = pathname.getName().endsWith(".txt");
			boolean hidden = pathname.getName().startsWith(".");
			return (directory || txt) && !hidden;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_browser);

        actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

		listView = (ListView) findViewById(R.id.listView);

		SharedPreferences settings = getSharedPreferences(Cache.PREFS_NAME, 0);
		String path = settings.getString(DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath());

		currentDirectory = new File(path);

        if (actionBar != null) {
            getActionBar().setSubtitle(path);
        }

		showDirectory();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				File file = files[position];
				if (file.isDirectory()) {
					currentDirectory = file;
					showDirectory();
				} else {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("result", file);
					setResult(RESULT_OK, returnIntent);
					finish();

					SharedPreferences settings = getSharedPreferences(Cache.PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(DIRECTORY, currentDirectory.getAbsolutePath());

					editor.apply();
				}
			}
		});
	}

	private void showDirectory() {
		files = currentDirectory.listFiles(filter);

		if (files == null) {
            files = new File[0];
        }

        FileBrowserAdapter adapter = new FileBrowserAdapter(FileBrowserActivity.this, files);
		listView.setAdapter(adapter);

        if (actionBar != null) {
            actionBar.setSubtitle(currentDirectory.getAbsolutePath());
        }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (currentDirectory.getAbsolutePath().equals("/")) {
				NavUtils.navigateUpFromSameTask(this);
			} else {
				currentDirectory = currentDirectory.getParentFile();
				showDirectory();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
