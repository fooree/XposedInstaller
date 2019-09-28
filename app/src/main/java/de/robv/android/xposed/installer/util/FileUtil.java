package de.robv.android.xposed.installer.util;

import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;

import java.io.File;

import static de.robv.android.xposed.installer.XposedApp.TAG;

public class FileUtil {
    public static int setPermissions(String path, int mode, int uid, int gid) {
        try {
            Os.chmod(path, mode);
        } catch (ErrnoException e) {
            Log.w(TAG, "Failed to chmod(" + path + "): " + e);
            return e.errno;
        }

        if (uid >= 0 || gid >= 0) {
            try {
                Os.chown(path, uid, gid);
            } catch (ErrnoException e) {
                Log.w(TAG, "Failed to chown(" + path + "): " + e);
                return e.errno;
            }
        }

        return 0;
    }
    public static boolean deleteContentsAndDir(File dir) {
        if (deleteContents(dir)) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean deleteContents(File dir) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    success &= deleteContents(file);
                }
                if (!file.delete()) {
                    Log.w(TAG, "Failed to delete " + file);
                    success = false;
                }
            }
        }
        return success;
    }
}
