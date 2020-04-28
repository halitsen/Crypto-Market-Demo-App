package halit.sen.cryptomarket.model.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CoinResponse(
    val status: Status?,
    @SerializedName("data") val coins: ArrayList<Coin>
)

data class Status(
    val timestamp: String,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
    val elapsed: Int,
    @SerializedName("credit_count") val creditCount: Int
)

data class Coin(
    val id: Int,
    val name: String,
    val symbol: String,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("max_supply") val maxSupply: String,
    @SerializedName("total_supply") val totalSupply: String,
    val quote: Quote?
):Serializable

data class Quote(@SerializedName("USD") val usd: Usd):Serializable

data class Usd(
    val price: String,
    @SerializedName("volume_24h") val volume24Hour: String,
    @SerializedName("percent_change_1h") val  percentChangePerHour: String,
    @SerializedName("percent_change_24h") val percentChangePerDay: String,
    @SerializedName("percent_change_7d") val percentChangePerWeek: String,
    @SerializedName("market_cap") val marketCap: String
    ):Serializable


