package com.silver.github.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.silver.github.model.User
import io.reactivex.disposables.CompositeDisposable

class UserListDataSourceFactory(
    private val query: String,
    private val disposable: CompositeDisposable
) : DataSource.Factory<Int, User>() {

    val dataSource = MutableLiveData<UserListDataSource>()

    override fun create(): DataSource<Int, User> {
        val dataSource = UserListDataSource(query, disposable)
        this.dataSource.postValue(dataSource)
        return dataSource
    }
}
