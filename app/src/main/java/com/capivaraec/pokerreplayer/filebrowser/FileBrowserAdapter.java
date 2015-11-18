package com.capivaraec.pokerreplayer.filebrowser;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capivaraec.pokerreplayer.R;

class FileBrowserAdapter extends ArrayAdapter<File> {

	private final Context context;
	private final File[] files;

	public FileBrowserAdapter(Context context, File[] files) {
		super(context, R.layout.file_browser_item, files);

		this.context = context;
		this.files = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FileHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.file_browser_item, parent, false);

			holder = new FileHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

			row.setTag(holder);
		} else {
			holder = (FileHolder) row.getTag();
		}

		File file = files[position];
		holder.txtTitle.setText(file.getName());
		if (file.isDirectory()) {
			holder.imgIcon.setBackgroundResource(R.drawable.folder_icon);
		} else {
			holder.imgIcon.setBackgroundResource(R.drawable.file_icon);
		}

		return row;
	}

	private static class FileHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}
}
