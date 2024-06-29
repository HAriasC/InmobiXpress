package com.inmobixpress.inmobixpress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _bottomBarVisible = MutableLiveData<Boolean>()
    val bottomBarVisible: LiveData<Boolean> = _bottomBarVisible

    private val _contactBottomBarVisible = MutableLiveData<Boolean>()
    val contactBottomBarVisible: LiveData<Boolean> = _contactBottomBarVisible

    fun onVisibleChanged(visible: Boolean) {
        _bottomBarVisible.value = visible
    }

    fun onVisibleContactBarChanged(visible: Boolean) {
        _contactBottomBarVisible.value = visible
    }
}