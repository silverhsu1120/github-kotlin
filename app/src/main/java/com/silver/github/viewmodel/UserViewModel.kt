package com.silver.github.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silver.github.model.User

class UserViewModel(user: User) : ViewModel() {

    val name = MutableLiveData<String>()
    val avatar = MutableLiveData<String>()

    init {
        name.value = user.name
        avatar.value = user.avatar
    }
}