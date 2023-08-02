package com.example.newapppp.ui.redactor

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>(v: T? = null) : MutableLiveData<T>(v) {

    private val pending = AtomicBoolean(v != null)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w("message", "Multiple observers registered but only one will be notified of changes")
        }

        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }
}

@SuppressLint("NullSafeMutableLiveData")
@MainThread
fun SingleLiveEvent<Unit>.emit() {
    this.value = null
}

@MainThread
fun<T> SingleLiveEvent<T>.emit(value: T) {
    this.value = value
}