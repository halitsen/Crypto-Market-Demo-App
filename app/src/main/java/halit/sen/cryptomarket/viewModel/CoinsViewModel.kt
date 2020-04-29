package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.di.DaggerAppComponent
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.model.data.CoinResponse
import halit.sen.cryptomarket.model.CoinService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CoinsViewModel : ViewModel() {

    @Inject
    lateinit var coinService: CoinService

    var coins = MutableLiveData<ArrayList<Coin>>()
        private set
    var isLoading = MutableLiveData<Boolean>()
        private set
    var isError = MutableLiveData<Boolean>()
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
            }) {
                //do not update UI when error occured every 5 sec
            }
    }

    fun initialRequest() {
        isLoading.value = true
        coinService
            .getCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result: CoinResponse -> result.coins }
            .subscribe({ coinList ->
                coins.value = coinList
                isLoading.value = false
                isError.value=false
            }) { error -> //does nothing on error
                isLoading.value = false
                isError.value = true
            }
    }
}