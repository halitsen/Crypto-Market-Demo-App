package halit.sen.cryptomarket.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.SharedPreference

class DetailViewModelFactory(private val context: Context, private val coin: Coin, private val preferences: SharedPreference) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(context, coin, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
