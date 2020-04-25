package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.AppUtils

class DetailViewModel(val coin: Coin): ViewModel(){


    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _price = MutableLiveData<String>()
    val price: LiveData<String>
        get() = _price

    private val _oneHourChange = MutableLiveData<String>()
    val oneHourChange: LiveData<String>
        get() = _oneHourChange

    private val _dailyChange = MutableLiveData<String>()
    val dailyChange: LiveData<String>
        get() = _dailyChange

    private val _weeklyChange = MutableLiveData<String>()
    val weeklyChange: LiveData<String>
        get() = _weeklyChange

    private val _totalSupply = MutableLiveData<String>()
    val totalSupply: LiveData<String>
        get() = _totalSupply

    private val _maxSupply = MutableLiveData<String>()
    val maxSupply: LiveData<String>
        get() = _maxSupply

    private val _dailyVolume = MutableLiveData<String>()
    val dailyVolume: LiveData<String>
        get() = _dailyVolume

    private val _marketCap = MutableLiveData<String>()
    val marketCap: LiveData<String>
        get() = _marketCap

    private val _addFavText = MutableLiveData<String>()
    val addFavText: LiveData<String>
        get() = _addFavText

    init {
        _price.value = AppUtils.getFormattedPrice((coin.quote.usd.price).toDouble())
        _title.value = coin.name
        _oneHourChange.value = AppUtils.getFormattedPercentageValue((coin.quote.usd.percentChangePerHour).toDouble())
        _dailyChange.value = AppUtils.getFormattedPercentageValue((coin.quote.usd.percentChangePerDay).toDouble())
        _weeklyChange.value = AppUtils.getFormattedPercentageValue((coin.quote.usd.percentChangePerWeek).toDouble())
        _dailyVolume.value = AppUtils.getFormattedPercentageValue((coin.quote.usd.volume24Hour).toDouble())
        _marketCap.value = AppUtils.getFormattedPercentageValue((coin.quote.usd.marketCap).toDouble())
        _totalSupply.value = coin.totalSupply
        _maxSupply.value = coin.maxSupply
        _addFavText.value = "Add Favorites" //todo favorilerdeyse favoriden çıkar olacak.
    }

}