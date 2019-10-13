package app.pmsoft.ispork

import android.content.Context
import android.content.Intent
import android.view.Menu
import app.pmsoft.ispork.data.ISporkEntry

interface ActivityAction<E : ISporkEntry> {

  val requestCode: Int
  val menuItemId: Int

  fun addMenuItem(
    menu: Menu,
    context: Context
  )

  fun apply(activity: AbstractListActivity<E, *, *>)

  fun handleActivityResult(
    data: Intent?,
    resultCode: Int,
    viewModel: ListViewModel<E>
  )
}
