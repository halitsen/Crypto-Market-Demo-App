package halit.sen.cryptomarket.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import halit.sen.cryptomarket.model.data.Coin

class DetailViewModelFactory(private val coin: Coin) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(coin) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
