package app.pmsoft.ispork.util

import androidx.lifecycle.LiveData
import kotlin.reflect.KProperty

open class NonNullLiveData<T : Any>(initialValue: T) : LiveData<T>(initialValue) {
  override fun getValue(): T {
    return super.getValue()!!
  }

  operator fun getValue(
    thisRef: Any?,
    property: KProperty<*>
  ): T {
    return this.value
  }
}
