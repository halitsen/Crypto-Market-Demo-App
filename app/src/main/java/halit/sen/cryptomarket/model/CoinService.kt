package halit.sen.cryptomarket.model

import halit.sen.cryptomarket.di.DaggerAppComponent
import halit.sen.cryptomarket.model.data.CoinResponse
import io.reactivex.Observable
import javax.inject.Inject

class CoinService {

    @Inject
    lateinit var api: CoinApi

    init {
       DaggerAppComponent.create().inject(this)
    }

    fun getCoins(): Observable<CoinResponse> {
        return api.getCoins()
    }
}