package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.model.data.CoinResponse
import halit.sen.cryptomarket.model.CoinService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CoinsViewModel : ViewModel() {

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
            .map { result: CoinResponse -> result.coins }
            .subscribe({ coins ->
                _coins.value = coins
            }) { error -> //does nothing on error}
            }
    }
}