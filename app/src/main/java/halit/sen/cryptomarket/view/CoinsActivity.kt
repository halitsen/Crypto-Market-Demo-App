package halit.sen.cryptomarket.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kaopiz.kprogresshud.KProgressHUD
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.databinding.ActivityCoinsBinding
import halit.sen.cryptomarket.utils.AppUtils
import halit.sen.cryptomarket.utils.AppUtils.Companion.hasNetwork
import halit.sen.cryptomarket.utils.AppUtils.Companion.onTimerObservableError
import halit.sen.cryptomarket.utils.AppUtils.Companion.openInfoDialog
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.utils.SharedPreference.Companion.DAILY
import halit.sen.cryptomarket.utils.SharedPreference.Companion.PER_HOUR
import halit.sen.cryptomarket.utils.SharedPreference.Companion.WEEKLY
import halit.sen.cryptomarket.viewModel.CoinsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CoinsActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinsViewModel
    private lateinit var binding: ActivityCoinsBinding
    private lateinit var coinsAdapter: CoinsAdapter
    private lateinit var preferences: SharedPreference
    private lateinit var disposable: Disposable
    private lateinit var progress: KProgressHUD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVariables()
        viewModel.initialRequest()
        setDisposable()
        observeViewModel()
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

    private fun observeViewModel() {
        viewModel.coins.observe(this, Observer { coins ->
            coins?.let {
                coinsAdapter.data = coins
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
            binding.errorText.visibility = (if (isError) {
                View.VISIBLE
            } else {
                View.GONE
            })
        })
    }

    private fun setDisposable() {
        disposable = Observable.interval(
            1000, 5000,
            TimeUnit.MILLISECONDS
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewModel.refresh() }) { throwable: Throwable ->
                onTimerObservableError(
                    throwable,
                    this
                )
            }
    }

    private fun initVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coins)
        preferences = SharedPreference(this)
        binding.lifecycleOwner = this
        progress = KProgressHUD(this)
        AppUtils.createProgress(progress)
        if (!hasNetwork(this)) {
            openInfoDialog(
                this,
                getString(R.string.internet_connection_warning_text),
                getString(R.string.error)
            )
        }
        viewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        coinsAdapter = CoinsAdapter(preferences)
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.coinRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coinsAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
            setHasFixedSize(true)
        }
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
