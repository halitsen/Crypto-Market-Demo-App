package halit.sen.cryptomarket.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import halit.sen.cryptomarket.R
import java.text.DecimalFormat

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

        fun getFormattedPrice(price: Double): String{
            val formattedPrice = DecimalFormat("0.000").format(price)
            return "$ $formattedPrice"
        }
        fun getFormattedPercentageValue(percentage: Double):String{
            val formattedPrice = DecimalFormat("0.00").format(percentage)
            return "$formattedPrice %"
        }

        fun getFormattedTime(time:String):String{
            val timeIndex =time.indexOf('T')
            return time.substring(timeIndex+1,timeIndex +9)
        }

        fun onTimerObservableError(throwable: Throwable, context: Context) {
            Toast.makeText(context, "OnError in Observable Timer",
                Toast.LENGTH_LONG).show()
        }


    }
}