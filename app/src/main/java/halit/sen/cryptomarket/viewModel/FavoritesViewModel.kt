package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.model.CoinService
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.model.data.CoinResponse
import halit.sen.cryptomarket.utils.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoritesViewModel(val preferences: SharedPreference) : ViewModel() {

    private val coinService = CoinService()

    private val _coins = MutableLiveData<ArrayList<Coin>>()
    val coins
        get() = _coins

    fun refresh(long: Long) {
        getCoins(long)
    }

    private fun getCoins(long: Long) {
        val observable = coinService.getCoins()
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .map { result: CoinResponse -> filterFavoriteCoins(result.coins) }
            .subscribe({ coins ->
                _coins.value = coins
            }) { error -> //do nothing on error
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