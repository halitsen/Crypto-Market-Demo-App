package halit.sen.cryptomarket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.databinding.ActivityCoinsBinding
import halit.sen.cryptomarket.databinding.ActivityFavoritesBinding
import halit.sen.cryptomarket.utils.AppUtils
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.viewModel.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var progress: KProgressHUD
    private lateinit var coinsAdapter: CoinsAdapter
    private lateinit var preferences: SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites)
        preferences = SharedPreference(this)
        val viewModelFactory = FavoritesViewModelFactory(preferences)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoritesViewModel::class.java)
        binding.setLifecycleOwner(this)
        progress = KProgressHUD(this)
        AppUtils.createProgress(progress)
        setSupportActionBar(binding.favoritesToolbar);
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        coinsAdapter =
            CoinsAdapter()//todo buraya parametre olarak kullanıcının değişim seçimi gidecek..
        binding.coinRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coinsAdapter
        }

        viewModel.refresh()
        observeViewModel()

        binding.backIcon.setOnClickListener {
            finish()
        }

    }

    private fun observeViewModel() {

        viewModel.coins.observe(this, Observer { coins ->
            progress.dismiss()
            coins?.let {
                coinsAdapter.data = coins
            }
        })

        viewModel.coinsLoadError.observe(this, Observer { error ->
            progress.dismiss()
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            isLoading?.let {
                if (isLoading) {
                    progress.show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorites_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
/*
            R.id.hour -> coinsAdapter.notifyDataSetChanged()
            R.id.weekly -> true
            R.id.daily -> //fdgd
            else -> false
            */
        }

        return super.onOptionsItemSelected(item)
    }
}
