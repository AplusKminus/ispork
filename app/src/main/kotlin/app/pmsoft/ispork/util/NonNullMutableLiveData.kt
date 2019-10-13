package app.pmsoft.ispork.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlin.reflect.KProperty

class NonNullMutableLiveData<T : Any>(
  initialValue: T
) : MutableLiveData<T>(initialValue) {

  override fun getValue(): T {
    return super.getValue()!!
  }

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): T {
    return this.value
  }

  fun asImmutable(): NonNullLiveData<T> {
    return object : NonNullLiveData<T>(this.value) {
      override fun getValue(): T {
        return this@NonNullMutableLiveData.value
      }

      override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>
      ) {
        this@NonNullMutableLiveData.observe(
          owner,
          observer
        )
      }

      override fun hasObservers(): Boolean {
        return this@NonNullMutableLiveData.hasObservers()
      }

      override fun removeObservers(owner: LifecycleOwner) {
        this@NonNullMutableLiveData.removeObservers(owner)
      }

      override fun observeForever(observer: Observer<in T>) {
        this@NonNullMutableLiveData.observeForever(observer)
      }

      override fun removeObserver(observer: Observer<in T>) {
        this@NonNullMutableLiveData.removeObserver(observer)
      }

      override fun hasActiveObservers(): Boolean {
        return this@NonNullMutableLiveData.hasActiveObservers()
      }
    }
  }
}
