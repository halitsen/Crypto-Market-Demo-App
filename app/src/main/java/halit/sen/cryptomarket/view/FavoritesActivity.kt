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
import com.kaopiz.kprogresshud.KProgressHUD
import halit.sen.cryptomarket.databinding.ActivityFavoritesBinding
import halit.sen.cryptomarket.utils.AppUtils
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.viewModel.*
import android.app.Activity
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.utils.AppUtils.Companion.onTimerObservableError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class FavoritesActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var progress: KProgressHUD
    private lateinit var coinsAdapter: CoinsAdapter
    private lateinit var preferences: SharedPreference
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites)
        preferences = SharedPreference(this)
        val viewModelFactory = FavoritesViewModelFactory(preferences)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoritesViewModel::class.java)
        disposable = Observable.interval(
            1000, 5000,
            TimeUnit.MILLISECONDS
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ long: Long -> viewModel.refresh(long) }) { throwable ->
                onTimerObservableError(
                    throwable, this
                )
            }
        binding.setLifecycleOwner(this)
        progress = KProgressHUD(this)
        AppUtils.createProgress(progress)
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
                .subscribe({ long: Long -> viewModel.refresh(long) }) { throwable ->
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
            progress.dismiss()
            coins?.let {
                coinsAdapter.data = coins
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
