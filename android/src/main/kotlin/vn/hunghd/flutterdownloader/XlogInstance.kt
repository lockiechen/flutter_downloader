import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import android.os.Environment
import com.tencent.mars.BuildConfig

object XLogManager {

    init {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");

        val LOG_PATH = Environment.getExternalStorageDirectory().absolutePath + "/Android/com.devopsapp/xlog"
        val CACHE_PATH = Environment.getExternalStorageDirectory().absolutePath + "/Android/com.devopsapp/cacheXlog"

        val xlogInstance = Xlog();
        Log.setLogImp(xlogInstance);

        Log.setConsoleLogOpen(BuildConfig.DEBUG);

        val appenderMode = if (BuildConfig.DEBUG) Xlog.AppednerModeSync else Xlog.AppednerModeAsync;
        Log.appenderOpen(Xlog.LEVEL_ALL, appenderMode, CACHE_PATH, LOG_PATH, "DEVOPS_APP", 0);

    }

    fun close() {
        Log.appenderClose()
    }
}