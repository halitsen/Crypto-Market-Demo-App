package halit.sen.cryptomarket.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.model.Coin
import halit.sen.cryptomarket.model.CoinResponse
import halit.sen.cryptomarket.model.CoinService
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CoinsViewModel : ViewModel() {

    private val coinService = CoinService()

    private val _coins = MutableLiveData<ArrayList<Coin>>()
    val coins
        get() = _coins

    private val _coinsLoadError = MutableLiveData<Boolean>()
    val coinsLoadError
        get() = _coinsLoadError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading
        get() = _isLoading


    fun refresh(){
        fetchCoins()
    }


    private fun fetchCoins(){
        _isLoading.value = true

        coinService.getCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: SingleObserver<CoinResponse>{
                override fun onSuccess(response: CoinResponse?) {
                    _coins.value = response!!.coins
                    _isLoading.value = false
                    _coinsLoadError.value = false
                    Log.i("coins ",response.coins.get(0).name)
                }

                override fun onSubscribe(d: Disposable?) {
                    _isLoading.value = true
                }

                override fun onError(e: Throwable?) {
                    _coinsLoadError.value = true
                    _isLoading.value = false
                }

            })


    }









}