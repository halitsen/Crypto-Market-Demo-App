package halit.sen.cryptomarket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import halit.sen.cryptomarket.model.CoinService
import halit.sen.cryptomarket.model.data.Coin
import halit.sen.cryptomarket.model.data.CoinResponse
import halit.sen.cryptomarket.viewModel.CoinsViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class CryptoMarketTest {

    @get: Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var coinService: CoinService

    @InjectMocks
    var coinsViewModel = CoinsViewModel()

    private var dummyTestSingle: Observable<CoinResponse>? = null

    @Before
    fun testSetup(){
        MockitoAnnotations.initMocks(this)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {

            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler-> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Test
    fun getCoinsSuccessTest(){
        val dummyCoin = Coin(1,"Bitcoin,","btc","","","",null)
        val dummyCoinList = arrayListOf(dummyCoin)
        val coinResponse = CoinResponse(null,dummyCoinList)
        dummyTestSingle = Observable.just(coinResponse)
        Mockito.`when`(coinService.getCoins()).thenReturn(dummyTestSingle)
        coinsViewModel.refresh()
        assertEquals(1, coinsViewModel.coins.value?.size)
    }

    @Test
    fun getCoinsFailTest(){
        dummyTestSingle = Observable.error(Throwable())
        Mockito.`when`(coinService.getCoins()).thenReturn(dummyTestSingle)
        coinsViewModel.refresh()
        assertEquals(null, coinsViewModel.coins.value?.size)
    }



}