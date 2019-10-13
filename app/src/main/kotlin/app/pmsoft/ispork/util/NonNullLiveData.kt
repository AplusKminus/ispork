package app.pmsoft.ispork.util

import androidx.lifecycle.LiveData

open class NonNullLiveData<T : Any>(initialValue: T) : LiveData<T>(initialValue) {
  override fun getValue(): T {
    return super.getValue()!!
  }
}
