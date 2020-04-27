package halit.sen.cryptomarket.di

import dagger.Component
import halit.sen.cryptomarket.model.CoinService
import halit.sen.cryptomarket.viewModel.CoinsViewModel
import halit.sen.cryptomarket.viewModel.FavoritesViewModel

@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(service: CoinService)

    fun inject(service: CoinsViewModel)

    fun inject(service: FavoritesViewModel)



}