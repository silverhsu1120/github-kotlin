package com.silver.github.viewmodel

import android.os.Handler
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.silver.github.model.ApiState
import com.silver.github.model.User
import com.silver.github.ui.UserListDataSource
import com.silver.github.ui.UserListDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class UserListViewModel : ViewModel() {

    private lateinit var factory: UserListDataSourceFactory

    private val phrase = MutableLiveData<String>()
    private val disposable = CompositeDisposable()
    private val handler = Handler()

    var userList: LiveData<PagedList<User>>? = null
    val blank = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    init {
        userList = Transformations.switchMap(phrase) {
            blank.value = it.isEmpty()
            empty.value = false
            if (it.isEmpty()) {
                null
            } else {
                val query = "$it in:login"
                factory = UserListDataSourceFactory(query, disposable)
                val config = PagedList.Config.Builder().setPageSize(30).build()
                LivePagedListBuilder(factory, config)
                    .setBoundaryCallback(object : PagedList.BoundaryCallback<User>() {
                        override fun onZeroItemsLoaded() {
                            super.onZeroItemsLoaded()
                            empty.value = true
                        }
                    })
                    .build()
            }
        }
        blank.value = true
    }

    fun afterTextChanged(s: Editable?) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            phrase.value = s.toString()
        }, 500)
    }

    fun getApiState(): LiveData<ApiState> {
        return Transformations.switchMap(phrase) {
            Transformations.switchMap<UserListDataSource,
                    ApiState>(factory.dataSource, UserListDataSource::apiState)
        }
    }

    fun reload() {
        phrase.value?.let { phrase.value = it }
    }

    fun retry() {
        factory.dataSource.value?.retry()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
