package halit.sen.cryptomarket.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.databinding.ActivityCoinsBinding
import halit.sen.cryptomarket.utils.AppUtils.Companion.hasNetwork
import halit.sen.cryptomarket.utils.AppUtils.Companion.onTimerObservableError
import halit.sen.cryptomarket.utils.AppUtils.Companion.openInfoDialog
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.viewModel.CoinsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CoinsActivity : AppCompatActivity() {
    lateinit var viewModel: CoinsViewModel
    lateinit var binding: ActivityCoinsBinding
    private lateinit var coinsAdapter: CoinsAdapter
    private lateinit var preferences: SharedPreference
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coins)
        preferences = SharedPreference(this)
        binding.lifecycleOwner = this
        if(!hasNetwork(this)){
            openInfoDialog(this,"Check your internet connection and try again!!","Error")
        }
        viewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
         disposable = Observable.interval(1000, 5000,
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ long -> viewModel.refresh() }) { throwable: Throwable -> onTimerObservableError(throwable,this) }
        coinsAdapter =
            CoinsAdapter(preferences)

        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.coinRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coinsAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
            setHasFixedSize(true)
        }

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (disposable.isDisposed) {
            disposable = Observable.interval(1000, 5000,
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ long -> viewModel.refresh() }) { throwable: Throwable -> onTimerObservableError(throwable,this) }
        }
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    private fun observeViewModel() {
        viewModel.coins.observe(this, Observer { coins ->
            coins?.let {
                coinsAdapter.data = coins
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.favorite -> {
                val favoritesIntent = Intent(this, FavoritesActivity::class.java)
                favoritesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(favoritesIntent)
            }
            R.id.hour -> {
                preferences.setpercentageChoice("perHour")
                coinsAdapter.notifyDataSetChanged()
            }
            R.id.daily -> {
                preferences.setpercentageChoice("daily")
                coinsAdapter.notifyDataSetChanged()

            }
            R.id.weekly -> {
                preferences.setpercentageChoice("weekly")
                coinsAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
