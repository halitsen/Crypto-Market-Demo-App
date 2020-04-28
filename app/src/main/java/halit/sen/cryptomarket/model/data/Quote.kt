package halit.sen.cryptomarket.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quote(
    @SerializedName("USD") val usd: Usd?
) : Parcelable