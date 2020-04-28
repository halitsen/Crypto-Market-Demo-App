package halit.sen.cryptomarket.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.databinding.ActivityDetailBinding
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.SharedPreference
import halit.sen.cryptomarket.viewModel.DetailViewModel
import halit.sen.cryptomarket.viewModel.DetailViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private var coin : Coin? = null
    private lateinit var preferences: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariables()
        setArrows()

        binding.backIcon.setOnClickListener {
            finish()
        }
        binding.addFav.setOnClickListener {
            viewModel.onFavoriteClick()
        }
    }
    private fun setArrows(){
            if((coin?.quote?.usd?.percentChangePerHour)?.let { it.toDouble() >= 0}!!){
                binding.oneHourPercentArrow.setImageResource(R.drawable.green_arrow_icon)
            }else{
                binding.oneHourPercentArrow.setImageResource(R.drawable.red_arrow_icon)
            }
            if((coin?.quote?.usd?.percentChangePerWeek)?.let { it.toDouble() >= 0 }!!){
                binding.oneWeekPercentArrow.setImageResource(R.drawable.green_arrow_icon)
            }else{
                binding.oneWeekPercentArrow.setImageResource(R.drawable.red_arrow_icon)
            }
            if((coin?.quote?.usd?.percentChangePerDay)?.let { it.toDouble() >= 0 }!!){
                binding.oneDayPercentArrow.setImageResource(R.drawable.green_arrow_icon)
            }else{
                binding.oneDayPercentArrow.setImageResource(R.drawable.red_arrow_icon)
            }
    }

    private fun initVariables(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        preferences = SharedPreference(this)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            coin = bundle.getParcelable("coin") as Coin?
        }
        val viewModelFactory = DetailViewModelFactory(this,coin,preferences)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.detailViewModel = viewModel
        binding.setLifecycleOwner(this)
        setSupportActionBar(binding.detailToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }
}
