package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.SharedPreference

class DetailViewModelFactory(private val coin: Coin, private val preferences: SharedPreference) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(coin, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
