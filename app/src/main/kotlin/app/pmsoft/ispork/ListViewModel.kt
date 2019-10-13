package app.pmsoft.ispork

import androidx.lifecycle.LiveData

interface ListViewModel<E> {

  val data: LiveData<List<E>>

  fun delete(elements: List<E>)

  fun persist(element: E): Long
}
