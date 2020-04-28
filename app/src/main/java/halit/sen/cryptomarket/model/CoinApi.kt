package halit.sen.cryptomarket.model

import halit.sen.cryptomarket.model.data.CoinResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface CoinApi {

    @GET("v1/cryptocurrency/listings/latest?start=1&convert=USD")
    fun getCoins(): Observable<CoinResponse>

}