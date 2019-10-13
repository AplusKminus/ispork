package app.pmsoft.ispork.util

import androidx.lifecycle.MutableLiveData

class ZeroIsNullLongLiveData(initialValue: Long?) : MutableLiveData<Long?>(initialValue?.takeUnless { it == 0L }) {

  constructor() : this(null)

  override fun setValue(value: Long?) {
    super.setValue(value?.takeUnless { it == 0L })
  }

  override fun postValue(value: Long?) {
    super.postValue(value?.takeUnless { it == 0L })
  }
}
