package halit.sen.cryptomarket.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
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

        @SuppressLint("ResourceAsColor")
        fun openInfoDialog(context: Context, content: String, title: String) {
            val dialog = MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText("OK")
                .positiveColor(R.color.black)
                .onPositive { dialog1, which -> dialog1.dismiss() } //todo day mode da 'OK' text görünmüyor..
                .show()
            dialog.titleView.textSize = 16f
            dialog.contentView!!.textSize = 14f
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