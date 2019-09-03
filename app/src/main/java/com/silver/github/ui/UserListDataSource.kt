package com.silver.github.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.silver.github.App
import com.silver.github.R
import com.silver.github.model.ApiState
import com.silver.github.model.User
import com.silver.github.model.GithubService
import com.silver.github.util.ToastUtil
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class UserListDataSource(
    private val query: String,
    private val disposable: CompositeDisposable
) : PageKeyedDataSource<Int, User>() {

    val apiState = MutableLiveData<ApiState>()

    private var retry: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        apiState.postValue(ApiState.LOADING)
        GithubService.apiUser.search(query, 1, params.requestedLoadSize)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                apiState.postValue(ApiState.SUCCESS)
                callback.onResult(it.items, null, 2)
            }, {
                ToastUtil.s(it.message ?: App.getContext().getString(R.string.unknown_error))
                apiState.postValue(ApiState.ERROR)
                retry = Completable.fromAction { loadInitial(params, callback) }
            }).addTo(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        apiState.postValue(ApiState.LOADING)
        GithubService.apiUser.search(query, params.key, params.requestedLoadSize)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                apiState.postValue(ApiState.SUCCESS)
                callback.onResult(it.items, params.key + 1)
            }, {
                ToastUtil.s(it.message ?: App.getContext().getString(R.string.unknown_error))
                apiState.postValue(ApiState.ERROR)
                retry = Completable.fromAction { loadAfter(params, callback) }
            }).addTo(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {

    }

    fun retry() {
        retry?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()
            ?.addTo(disposable)
    }
}
