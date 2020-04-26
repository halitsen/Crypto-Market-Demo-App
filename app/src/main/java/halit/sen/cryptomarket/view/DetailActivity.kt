package halit.sen.cryptomarket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.databinding.ActivityCoinsBinding
import halit.sen.cryptomarket.databinding.ActivityDetailBinding
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.viewModel.CoinsViewModel
import halit.sen.cryptomarket.viewModel.DetailViewModel
import halit.sen.cryptomarket.viewModel.DetailViewModelFactory

class DetailActivity : AppCompatActivity() {

    lateinit var viewModel: DetailViewModel
    lateinit var binding: ActivityDetailBinding
    lateinit var coin : Coin
    private lateinit var preferences: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        preferences = SharedPreference(this)

        preferences.getCoins()
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            coin = bundle.getSerializable("coin") as Coin
        }
        val viewModelFactory = DetailViewModelFactory(coin,preferences)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.detailViewModel = viewModel
        binding.setLifecycleOwner(this)
        setSupportActionBar(binding.detailToolbar);
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.backIcon.setOnClickListener {
            finish()
        }

        binding.addFav.setOnClickListener {
            viewModel.onFavoriteClick()
        }

    }
}
