package app.pmsoft.ispork

interface SelectionHandler<E> {

  fun onClick(element: E)

  fun onLongClick(element: E)

  fun isSelected(element: E): Boolean

  enum class Mode {
    NONE, SINGLE, MULTI
  }
}
