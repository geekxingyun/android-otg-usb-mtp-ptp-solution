package com.xingyun.smartusbdeviceapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    private final SharedPreferences prefs;

    public AppSettings(Context context) {
        prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
    }

    public boolean showChangelog(int nextNumber) {
        int last = prefs.getInt("internal.last_changelog_number", -1);
        if (last == -1 || nextNumber > last) {
            prefs.edit().putInt("internal.last_changelog_number", nextNumber).apply();
        }
        return last != -1 && nextNumber > last;
    }

    public boolean isGalleryOrderReversed() {
        return prefs.getBoolean("internal.gallery.reverse_order", false);
    }

    public void setGalleryOrderReversed(boolean reversed) {
        prefs.edit().putBoolean("internal.gallery.reverse_order", reversed).apply();
    }

    public int getShowCapturedPictureDuration() {
        return getIntFromStringPreference("liveview.captured_picture_duration", -1);
    }

    public boolean isShowCapturedPictureNever() {
        return getShowCapturedPictureDuration() == -2;
    }

    public boolean isShowCapturedPictureDurationManual() {
        return getShowCapturedPictureDuration() == -1;
    }

    public int getNumPicturesInStream() {
        return getIntFromStringPreference("picturestream.num_pictures", 6);
    }

    public boolean isShowFilenameInStream() {
        return prefs.getBoolean("picturestream.show_filename", true);
    }

    public int getCapturedPictureSampleSize() {
        return getIntFromStringPreference("memory.picture_sample_size", 2);
    }

    private int getIntFromStringPreference(String key, int defaultValue) {
        try {
            String value = prefs.getString(key, null);
            if (value != null) {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            // nop
        }
        return defaultValue;
    }
}
