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

public class FileBrowserAdapter extends ArrayAdapter<File> {

	private Context context;
	private int layoutResourceId;
	private File[] files;

	public FileBrowserAdapter(Context context, int layoutResourceId, File[] files) {
		super(context, layoutResourceId, files);

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.files = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FileHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

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
