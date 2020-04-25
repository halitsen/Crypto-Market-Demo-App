package halit.sen.cryptomarket.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import halit.sen.cryptomarket.R
import halit.sen.cryptomarket.model.data.Coin
import kotlinx.android.synthetic.main.coin_list_item.view.*
import java.text.DecimalFormat

class CoinsAdapter (): RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder>(){

    //todo parametre olarak gelen change tercihi 3 alandan hangisinin visible olacağını kontrol edecek sadece

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
        holder.bind(data.get(position))
    }


    class CoinsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val coinName = view.coin_name
        val coinSymbol = view.coin_symbol
        val lastUpdate = view.coin_last_update
        val coinPrice = view.coin_price
        val coinArrow = view.coin_percentage_arrow
        val coinChangePercentage = view.coin_change_percentage

        fun bind(coin: Coin){

            val price  = java.lang.Double.valueOf(coin.quote.usd.price)
            val formattedPrice = DecimalFormat("0.000").format(price)

            val floatIntex = coin.quote.usd.percentChangePerDay.indexOf('.')
            val formattedDailyChange = coin.quote.usd.percentChangePerDay
                .substring(0, floatIntex + 3)

            val timeIndex =coin.lastUpdated.indexOf('T')
            val formattedTime = coin.lastUpdated.substring(timeIndex+1,timeIndex +9)

            coinName.text = coin.name
            coinSymbol.text = coin.symbol
            coinPrice.text = "$ $formattedPrice"
            lastUpdate.text = formattedTime
            //todo burası kullanıcı seçimine göre günlük,saatlik,haftalık olarak değişecek (shared preferences ta tutulacak.)
            coinChangePercentage.text = "$formattedDailyChange %"
            if((coin.quote.usd.percentChangePerDay).toDouble() > 0){
                coinArrow.setImageResource(R.drawable.green_arrow_icon)
            }else{
                coinArrow.setImageResource(R.drawable.red_arrow_icon)
            }


        }

    }

}