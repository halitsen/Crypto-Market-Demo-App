package halit.sen.cryptomarket.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import halit.sen.cryptomarket.model.data.Coin

class SharedPreference(context: Context) {

    val PREFERENCE_NAME = "cryptoSharedPreference"
    val COINS = "coins"
    val CHOICE = "choice"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCoins(): ArrayList<Coin> {
        val coins = ArrayList<Coin>()
        val gson = Gson()
        val json = preference.getString(COINS,null)

        if(json == null){
            return coins
        }else{
            return gson.fromJson<ArrayList<Coin>>(json, object: TypeToken<ArrayList<Coin>>(){}.type)
        }
       // return preference.getString(COINS, "")
    }

    fun setCoins(coinList: ArrayList<Coin>){
        val editor = preference.edit()
        val gson = Gson()
        val json = gson.toJson(coinList)
        editor.putString(COINS, json)
        editor.apply()
    }

    fun setpercentageChoice(choice: String){
        val editor = preference.edit()
        editor.putString(CHOICE, choice)
        editor.apply()
    }

    fun getpercentageChoice():String? {
        return preference.getString(CHOICE, "")
    }

}