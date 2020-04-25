package halit.sen.cryptomarket

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD

class AppUtils {

    companion object {
        fun hasNetwork(context: Context): Boolean {
            var isConnected: Boolean = false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }

        fun showProgress(progress: KProgressHUD){
            progress.show()
        }

        fun hideProgress(progress: KProgressHUD){
            progress.dismiss()
        }

        fun createProgress(progress: KProgressHUD){
            progress
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimaryDark)
                .setWindowColor(R.color.colorPrimaryDark)
                .setLabel("Loading...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        }
        fun noInternetWarning(context: Context){
            Toast.makeText(context,"Check your internet connection!!", Toast.LENGTH_SHORT).show()
        }
    }
}