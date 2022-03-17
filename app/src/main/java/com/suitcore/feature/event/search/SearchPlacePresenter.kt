package com.suitcore.feature.event.search

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.suitcore.BaseApplication
import com.suitcore.R
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.model.Place
import com.suitcore.data.remote.services.APIService
import com.suitcore.helper.CommonConstant
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by dodydmw19 on 1/14/19.
 */

class SearchPlacePresenter(var context: Context?) : BasePresenter<SearchPlaceView>, CoroutineScope {

    @Inject
    lateinit var apiService: APIService
    private var mvpView: SearchPlaceView? = null
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext get() =  Dispatchers.IO + job
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun reverseGeoCoder(latitude: Double, longitude: Double) = launch(Dispatchers.Main) {
        val url: String = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude +","+ latitude + ".json" +
                "?access_token=" + CommonConstant.MAP_BOX_TOKEN
        runCatching {
            mvpView?.showDialogLoading(true, context?.getString(R.string.txt_loading))
            apiService.searchPlaceAsync(url).await()
        }.onSuccess { data ->
            if (data.arrayData?.isNotEmpty()!!) {
                //val rest = data.arrayData
                runCatching{
                }.onSuccess {
                    mvpView?.hideLoading()
                    val address: Place? = data.arrayData?.get(0)
                    mvpView?.onAddressReceive(address?.placeName)
                }.onFailure {
                    mvpView?.hideLoading()
                    mvpView?.onPlaceNotFound()
                }
            } else {
                mvpView?.onPlaceNotFound()
            }
        }.onFailure {
            mvpView?.onPlaceNotFound()
        }
    }

//    @SuppressLint("CheckResult")
//    fun reverseGeoCoder(latitude: Double, longitude: Double) {
//        val url: String = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude +","+ latitude + ".json" +
//                "?access_token=" + CommonConstant.MAP_BOX_TOKEN
//
//        mCompositeDisposable?.add(
//                apiService.searchPlaceAsync(url)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .doOnSubscribe {
//                            mvpView?.showDialogLoading(true, context?.getString(R.string.txt_loading))
//                        }
//                        .doOnComplete {
//                            mvpView?.hideLoading()
//                        }
//                        .subscribe({ data ->
//                            if (data != null) {
//                                if (data.arrayData?.isNotEmpty()!!) {
//                                    val address: Place? = data.arrayData?.get(0)
//                                    mvpView?.onAddressReceive(address?.placeName)
//
//                                } else {
//                                    mvpView?.onPlaceNotFound()
//                                }
//                            } else {
//                                mvpView?.onPlaceNotFound()
//                            }
//                        }, {
//                            //mvpView?.hideLoading()
//                            mvpView?.onPlaceNotFound()
//                            mvpView?.hideLoading()
//                        })
//        )
//
//    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: SearchPlaceView) {
        mvpView = view
        // Initialize this presenter as a lifecycle-aware when a view is a lifecycle owner.
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
        mCompositeDisposable.let { mCompositeDisposable?.clear() }
    }

}