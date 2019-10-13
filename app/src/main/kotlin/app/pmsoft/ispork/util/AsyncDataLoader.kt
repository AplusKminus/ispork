package app.pmsoft.ispork.util

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData

class AsyncDataLoader<E>(
  private val supplier: () -> List<E>,
  private val targetData: MutableLiveData<List<E>>
) : AsyncTask<Void, Void, List<E>>() {
  override fun doInBackground(vararg params: Void?): List<E> = supplier()

  override fun onPostExecute(result: List<E>?) {
    targetData.value = result
  }
}
