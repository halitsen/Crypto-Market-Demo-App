package halit.sen.cryptomarket.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.di.DaggerAppComponent
import halit.sen.cryptomarket.model.CoinService
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.model.data.CoinResponse
import halit.sen.cryptomarket.utils.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoritesViewModel(val preferences: SharedPreference) : ViewModel() {

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
            .map { result: CoinResponse -> filterFavoriteCoins(result.coins!!) }
            .subscribe({ coinList ->
                coins.value = coinList
            }) {
                //do not update UI when error occured every 5sec
            }
    }

    fun initialRequest() {
        isLoading.value = true
        coinService
            .getCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result: CoinResponse -> filterFavoriteCoins(result.coins!!) }
            .subscribe({ coinList ->
                coins.value = coinList
                isLoading.value = false
                isError.value = false
            }) {
                isError.value = true
                isLoading.value = false
            }
    }

    private fun filterFavoriteCoins(response: ArrayList<Coin>): ArrayList<Coin> {
        val userCoins = ArrayList<Coin>()
        val favCoins = preferences.getCoins()
        for (coin in response) {
            for (favCoin in favCoins) {
                if (favCoin.id == coin.id) {
                    userCoins.add(coin)
                }
            }
        }
        return userCoins
    }
}