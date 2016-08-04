package com.geval6.praymate.Utlis;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.cacheDir = new File(Environment.getExternalStorageDirectory(), "JsonParseTutorialCache");
        } else {
            this.cacheDir = context.getCacheDir();
        }
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        return new File(this.cacheDir, String.valueOf(url.hashCode()));
    }

    public void clear() {
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
