package halit.sen.cryptomarket.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.Const.Companion.FAV_COINS
import halit.sen.cryptomarket.utils.Const.Companion.PERCENTAGE_CHOICE
import halit.sen.cryptomarket.utils.Const.Companion.PREFERENCE_NAME

class SharedPreference(context: Context) {



    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCoins(): ArrayList<Coin> {
        val coins = ArrayList<Coin>()
        val gson = Gson()
        val json = preference.getString(FAV_COINS,null)

        if(json == null){
            return coins
        }else{
            return gson.fromJson<ArrayList<Coin>>(json, object: TypeToken<ArrayList<Coin>>(){}.type)
        }
    }

    fun setCoins(coinList: ArrayList<Coin>){
        val editor = preference.edit()
        val gson = Gson()
        val json = gson.toJson(coinList)
        editor.putString(FAV_COINS, json)
        editor.apply()
    }

    fun setpercentageChoice(choice: String){
        val editor = preference.edit()
        editor.putString(PERCENTAGE_CHOICE, choice)
        editor.apply()
    }

    fun getpercentageChoice():String? {
        return preference.getString(PERCENTAGE_CHOICE, "")
    }

}