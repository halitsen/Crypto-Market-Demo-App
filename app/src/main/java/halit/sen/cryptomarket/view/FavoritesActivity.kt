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
import halit.sen.cryptomarket.databinding.ActivityFavoritesBinding
import halit.sen.cryptomarket.utils.AppUtils
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.viewModel.*
import android.app.Activity
import android.view.View
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.utils.AppUtils.Companion.onTimerObservableError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class FavoritesActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var coinsAdapter: CoinsAdapter
    private lateinit var preferences: SharedPreference
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites)
        preferences = SharedPreference(this)
        if(!AppUtils.hasNetwork(this)){
            AppUtils.openInfoDialog(this, "Check your internet connection and try again!!", "Error")
        }
        if(preferences.getCoins().size == 0){
            binding.emptyFavText.visibility = View.VISIBLE
        }
        val viewModelFactory = FavoritesViewModelFactory(preferences)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoritesViewModel::class.java)
        disposable = Observable.interval(
            1000, 5000,
            TimeUnit.MILLISECONDS
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ long -> viewModel.refresh() }) { throwable ->
                onTimerObservableError(
                    throwable, this
                )
            }
        binding.setLifecycleOwner(this)
        setSupportActionBar(binding.favoritesToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        coinsAdapter =
            CoinsAdapter(preferences)
        binding.coinRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coinsAdapter
        }
        observeViewModel()

        binding.backIcon.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (disposable.isDisposed) {
            disposable = Observable.interval(
                1000, 5000,
                TimeUnit.MILLISECONDS
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ long -> viewModel.refresh() }) { throwable ->
                    onTimerObservableError(
                        throwable,
                        this
                    )
                }
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
                if(coins.size==0){
                    binding.coinRecyclerView.visibility = View.GONE
                    binding.emptyFavText.visibility = View.VISIBLE
                }else{
                    binding.coinRecyclerView.visibility = View.VISIBLE
                    binding.emptyFavText.visibility = View.GONE
                    coinsAdapter.data = coins
                }

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorites_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            halit.sen.cryptomarket.R.id.hour -> {
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
