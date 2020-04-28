package halit.sen.cryptomarket.di

import dagger.Module
import dagger.Provides
import halit.sen.cryptomarket.model.CoinApi
import halit.sen.cryptomarket.model.CoinService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class AppModule {

    @Provides
    fun provideCoinsApi(): CoinApi {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("X-CMC_PRO_API_KEY", "d7d03b17-6592-4b81-823c-32c3c0691346")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build().create(CoinApi::class.java)
    }

    @Provides
    fun provideCoinService(): CoinService {
        return CoinService()
    }

    companion object {
        private const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    }
}