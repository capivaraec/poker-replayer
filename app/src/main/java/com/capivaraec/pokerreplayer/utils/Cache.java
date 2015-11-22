package com.capivaraec.pokerreplayer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;

import com.capivaraec.pokerreplayer.MainActivity;
import com.capivaraec.pokerreplayer.history.History;

public class Cache {

    private static final String FILE_PATH = "file_path";
    public static final String PREFS_NAME = "preferences";
    private static final String CURRENT_HAND = "current_hand";

	private Cache() {
	}

	public static void writeHistory(Context context, File file, History history) {
		try {
			FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(history);
			oos.close();
			fos.close();

            setFilePath(context, file.getPath());
            setCurrentHand(context, 0);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

    public static void setCurrentHand(Context context, int hand) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(CURRENT_HAND, hand);
        editor.commit();
    }

    public static int getCurrentHand(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt(CURRENT_HAND, 0);
    }

    public static void setFilePath(Context context, String path) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(FILE_PATH, path);
        editor.commit();
    }

    public static String getFilePath(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(FILE_PATH, null);
    }

	public static History readHistory(Context context, String fileName) {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object history = ois.readObject();

			return (History)history;
		} catch (Exception e) {
			return null;
		}
	}
}
