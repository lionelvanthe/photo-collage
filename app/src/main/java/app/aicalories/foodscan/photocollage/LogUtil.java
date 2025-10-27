package app.aicalories.foodscan.photocollage;

import android.util.Log;

import androidx.annotation.Nullable;


public class LogUtil {

    public static void theNv(@Nullable String log) {
        if (BuildConfig.DEBUG) {
            final StackTraceElement stackTrace = new Exception().getStackTrace()[1];

            String fileName = stackTrace.getFileName();
            if (fileName == null) fileName = "";

            final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                    + stackTrace.getLineNumber() + ")";
            Log.d("Thenv", info + ": " + log);
        }
    }
}
