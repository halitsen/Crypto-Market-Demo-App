package halit.sen.cryptomarket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.viewModel.CoinsViewModel

class CoinsActivity : AppCompatActivity() {

    lateinit var viewModel: CoinsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coins)
        viewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        viewModel.refresh()
        //observeViewModel()

    }

    private fun observeViewModel(){

        viewModel.coins.observe(this, Observer { coins ->

        })

        viewModel.coinsLoadError.observe(this, Observer{ error ->

        })
        viewModel.isLoading.observe(this, Observer { isLoading ->

        })

    }
}
