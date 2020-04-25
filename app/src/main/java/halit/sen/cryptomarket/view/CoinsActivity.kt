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
import halit.sen.cryptomarket.utils.AppUtils
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.databinding.ActivityCoinsBinding
import halit.sen.cryptomarket.viewModel.CoinsViewModel

class CoinsActivity : AppCompatActivity() {
    lateinit var viewModel: CoinsViewModel
    lateinit var binding: ActivityCoinsBinding
    private lateinit var progress: KProgressHUD
    private lateinit var coinsAdapter: CoinsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coins)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        viewModel.refresh()
        progress = KProgressHUD(this)
        AppUtils.createProgress(progress)
        coinsAdapter =
            CoinsAdapter()//todo buraya parametre olarak kullanıcının değişim seçimi gidecek..

        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.coinRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coinsAdapter
        }
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite) {
            //viewModel.onProfileClicked()
        }
        return super.onOptionsItemSelected(item)
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
}
