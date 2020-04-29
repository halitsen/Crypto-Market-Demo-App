package halit.sen.cryptomarket.view

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
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.utils.AppUtils.Companion.onTimerObservableError
import halit.sen.cryptomarket.utils.SharedPreference.Companion.DAILY
import halit.sen.cryptomarket.utils.SharedPreference.Companion.PER_HOUR
import halit.sen.cryptomarket.utils.SharedPreference.Companion.WEEKLY
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
    private lateinit var progress: KProgressHUD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariables()
        viewModel.initialRequest()
        observeViewModel()
        binding.backIcon.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (disposable.isDisposed) {
            setDisposable()
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

    private fun setDisposable() {
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

    private fun initVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites)
        preferences = SharedPreference(this)
        if (!AppUtils.hasNetwork(this)) {
            AppUtils.openInfoDialog(
                this,
                getString(R.string.internet_connection_warning_text),
                getString(R.string.error)
            )
        }
        val viewModelFactory = FavoritesViewModelFactory(preferences)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoritesViewModel::class.java)
        setDisposable()
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
    }

    private fun observeViewModel() {
        viewModel.coins.observe(this, Observer { coins ->
            coins?.let {
                if (coins.size == 0) {
                    binding.coinRecyclerView.visibility = View.GONE
                    binding.emptyFavText.visibility = View.VISIBLE
                } else {
                    binding.coinRecyclerView.visibility = View.VISIBLE
                    binding.emptyFavText.visibility = View.GONE
                    coinsAdapter.data = coins
                }
                binding.errorText.visibility = View.GONE
            }
        })
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading)
                AppUtils.showProgress(progress)
            else
                AppUtils.hideProgress(progress)
        })
        viewModel.isError.observe(this, Observer { isError ->
            (if (isError) {
                binding.errorText.visibility = View.VISIBLE
                binding.emptyFavText.visibility = View.GONE
            } else {
                binding.errorText.visibility = View.GONE
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorites_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.hour -> {
                preferences.setPercentageChoice(PER_HOUR)
                coinsAdapter.notifyDataSetChanged()
            }
            R.id.daily -> {
                preferences.setPercentageChoice(DAILY)
                coinsAdapter.notifyDataSetChanged()

            }
            R.id.weekly -> {
                preferences.setPercentageChoice(WEEKLY)
                coinsAdapter.notifyDataSetChanged()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
