package halit.sen.cryptomarket.model

import io.reactivex.Single
import retrofit2.http.GET

interface CoinApi {


    @GET("v1/cryptocurrency/listings/latest?start=1&convert=USD")
    fun getCoins(): Single<CoinResponse>



}