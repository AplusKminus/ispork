package app.pmsoft.ispork

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import app.pmsoft.ispork.data.ISporkEntry

class GenericDeleteAction<E : ISporkEntry>(
  private val deletionDialogTitleQuantityStringId: Int,
  private val deletionDialogMessageQuantityStringId: Int
) : SelectedListItemAction<E> {
  override val requestCode: Int = RequestCodes.NONE
  override val menuItemId: Int = R.id.action_delete_item

  override fun addMenuItem(
    menu: Menu,
    context: Context
  ) {
    menu.add(
      Menu.NONE,
      menuItemId,
      Menu.NONE,
      R.string.delete
    ).also {
      it.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      it.icon = context.resources.getDrawable(
        R.drawable.ic_delete_white_24dp,
        context.theme
      )
    }
  }

  override fun appliesToNItems(n: Int): Boolean {
    return n > 0
  }

  override fun apply(
    items: List<E>,
    activity: AbstractListActivity<E, *, *>
  ) {
    val copyOfItems = items.toList()
    val resources = activity.getResources()
    AlertDialog.Builder(activity)
      .setTitle(
        resources.getQuantityString(
          deletionDialogTitleQuantityStringId,
          copyOfItems.size,
          copyOfItems.size
        )
      )
      .setMessage(
        resources.getQuantityString(
          deletionDialogMessageQuantityStringId,
          copyOfItems.size,
          copyOfItems.size
        )
      )
      .setPositiveButton(activity.getString(R.string.confirm_deletion)) { _, _ ->
        activity.viewModel.delete(copyOfItems)
      }
      .setNegativeButton(
        activity.getString(R.string.cancel_action),
        null
      )
      .setIcon(android.R.drawable.ic_dialog_alert)
      .show()
  }

  override fun handleActivityResult(
    data: Intent?,
    resultCode: Int,
    viewModel: ListViewModel<E>
  ) {
    // will never be called
  }
}
