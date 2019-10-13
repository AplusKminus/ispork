package app.pmsoft.ispork.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KProperty


operator fun <T> LiveData<T>.getValue(
  thisRef: Any?,
  property: KProperty<*>
): T? {
  return this.value
}

operator fun <T> MutableLiveData<T>.setValue(
  thisRef: Any?,
  property: KProperty<*>,
  value: T?
) {
  this.value = value
}
