package ejiancai.baseutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 描述：判断网络的辅助类
 *
 * @author Cuizhen
 * @date 2018/7/20-下午2:21
 */
public class NetUtils {

    /**
     * 判断是否有网络
     *
     * @return 返回值
     */
    public static boolean isConnected() {
        if (Utils.getAppContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) Utils.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return networkInfo.isAvailable();
                }
            }
        }
        return false;
    }

    public static boolean doWithCheckNet() {
        if (isConnected()) {
            return true;
        }
        return false;
    }
}
