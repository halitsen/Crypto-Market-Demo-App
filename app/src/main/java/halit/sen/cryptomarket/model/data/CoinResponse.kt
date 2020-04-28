package halit.sen.cryptomarket.model.data

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("status") val status: Status?,
    @SerializedName("data") val coins: ArrayList<Coin>?
)


