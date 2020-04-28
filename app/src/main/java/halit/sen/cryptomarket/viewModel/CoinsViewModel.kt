package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.di.DaggerAppComponent
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.model.data.CoinResponse
import halit.sen.cryptomarket.model.CoinService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CoinsViewModel : ViewModel() {

    @Inject
    lateinit var coinService: CoinService

    var coins = MutableLiveData<ArrayList<Coin>>()
    private set

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun refresh() {
        getCoins()
    }

    private fun getCoins() {
        coinService
            .getCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result: CoinResponse -> result.coins }
            .subscribe({ coinList ->
                coins.value = coinList
            }) { error -> //does nothing on error
            }
    }
}