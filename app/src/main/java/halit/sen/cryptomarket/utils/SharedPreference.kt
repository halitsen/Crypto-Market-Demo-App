package halit.sen.cryptomarket.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import halit.sen.cryptomarket.model.data.Coin

class SharedPreference(context: Context) {

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCoins(): ArrayList<Coin> {
        val coins = ArrayList<Coin>()
        val gson = Gson()
        val json = preference.getString(FAV_COINS, null)

        if (json == null) {
            return coins
        } else {
            return gson.fromJson(json, object : TypeToken<ArrayList<Coin>>() {}.type)
        }
    }

    fun setCoins(coinList: ArrayList<Coin>) {
        val editor = preference.edit()
        val gson = Gson()
        val json = gson.toJson(coinList)
        editor.putString(FAV_COINS, json)
        editor.apply()
    }

    fun setPercentageChoice(choice: String) {
        val editor = preference.edit()
        editor.putString(PERCENTAGE_CHOICE, choice)
        editor.apply()
    }

    fun getPercentageChoice(): String? {
        return preference.getString(PERCENTAGE_CHOICE, "")
    }

    companion object {
        const val PER_HOUR = "perHour"
        const val DAILY = "daily"
        const val WEEKLY = "weekly"
        const val PREFERENCE_NAME = "cryptoSharedPreference"
        const val FAV_COINS = "coins"
        const val PERCENTAGE_CHOICE = "choice"
    }

}