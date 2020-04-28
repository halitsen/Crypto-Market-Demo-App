package halit.sen.cryptomarket.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Usd(
    @SerializedName("price") val price: String?,
    @SerializedName("volume_24h") val volume24Hour: String?,
    @SerializedName("percent_change_1h") val percentChangePerHour: String?,
    @SerializedName("percent_change_24h") val percentChangePerDay: String?,
    @SerializedName("percent_change_7d") val percentChangePerWeek: String?,
    @SerializedName("market_cap") val marketCap: String?
) : Parcelable