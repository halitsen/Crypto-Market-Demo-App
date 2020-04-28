package halit.sen.cryptomarket.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coin(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("last_updated") val lastUpdated: String?,
    @SerializedName("max_supply") val maxSupply: String?,
    @SerializedName("total_supply") val totalSupply: String?,
    @SerializedName("quote") val quote: Quote?
) : Parcelable