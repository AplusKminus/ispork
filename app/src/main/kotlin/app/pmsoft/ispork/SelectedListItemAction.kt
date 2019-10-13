package app.pmsoft.ispork

import android.content.Context
import android.content.Intent
import android.view.Menu
import app.pmsoft.ispork.data.ISporkEntry

interface SelectedListItemAction<E : ISporkEntry> {

  val requestCode: Int
  val finishesActionMode: Boolean
    get() = true

  val menuItemId: Int
  fun addMenuItem(
    menu: Menu,
    context: Context
  )

  fun appliesToNItems(n: Int): Boolean

  fun apply(
    items: List<E>,
    activity: AbstractListActivity<E, *, *>
  )

  fun handleActivityResult(
    data: Intent?,
    resultCode: Int,
    viewModel: ListViewModel<E>
  )
}
