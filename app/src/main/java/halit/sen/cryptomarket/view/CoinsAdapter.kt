package halit.sen.cryptomarket.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import halit.sen.cryptomarket.utils.AppUtils.Companion.getFormattedPrice
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.utils.AppUtils.Companion.getFormattedPercentageValue
import halit.sen.cryptomarket.utils.AppUtils.Companion.getFormattedTime
import halit.sen.cryptomarket.utils.SharedPreference
import kotlinx.android.synthetic.main.coin_list_item.view.*

class CoinsAdapter (val preference: SharedPreference): RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder>(){

    var data = listOf<Coin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CoinsViewHolder (
      LayoutInflater.from(parent.context).inflate(R.layout.coin_list_item,parent,false)
    )


    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        holder.bind(data.get(position),preference)
        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            detailIntent.putExtra("coin", data.get(position))
            holder.itemView.context.startActivity(detailIntent)
        }
    }


    class CoinsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val coinName = view.coin_name
        val coinSymbol = view.coin_symbol
        val lastUpdate = view.coin_last_update
        val coinPrice = view.coin_price
        val coinArrow = view.coin_percentage_arrow
        val coinChangePercentage = view.coin_change_percentage

        fun bind(coin: Coin, preferences:SharedPreference){

            when(preferences.getpercentageChoice()){
                "perHour" ->{
                    coinChangePercentage.text = getFormattedPercentageValue((coin.quote!!.usd.percentChangePerHour).toDouble())
                    if((coin.quote.usd.percentChangePerHour).toDouble() > 0){
                        coinArrow.setImageResource(R.drawable.green_arrow_icon)
                    }else{
                        coinArrow.setImageResource(R.drawable.red_arrow_icon)
                    }
                }
                "daily"->{
                    coinChangePercentage.text = getFormattedPercentageValue((coin.quote!!.usd.percentChangePerDay).toDouble())
                    if((coin.quote.usd.percentChangePerDay).toDouble() > 0){
                        coinArrow.setImageResource(R.drawable.green_arrow_icon)
                    }else{
                        coinArrow.setImageResource(R.drawable.red_arrow_icon)
                    }
                }
                "weekly"->{
                    coinChangePercentage.text = getFormattedPercentageValue((coin.quote!!.usd.percentChangePerWeek).toDouble())
                    if((coin.quote.usd.percentChangePerWeek).toDouble() > 0){
                        coinArrow.setImageResource(R.drawable.green_arrow_icon)
                    }else{
                        coinArrow.setImageResource(R.drawable.red_arrow_icon)
                    }
                }
                else ->{
                    coinChangePercentage.text = getFormattedPercentageValue((coin.quote!!.usd.percentChangePerHour).toDouble())
                    if((coin.quote.usd.percentChangePerHour).toDouble() > 0){
                        coinArrow.setImageResource(R.drawable.green_arrow_icon)
                    }else{
                        coinArrow.setImageResource(R.drawable.red_arrow_icon)
                    }
                }
            }
            coinName.text = coin.name
            coinSymbol.text = coin.symbol
            coinPrice.text = getFormattedPrice((coin.quote.usd.price).toDouble())
            lastUpdate.text = getFormattedTime(coin.lastUpdated)
        }

    }

}