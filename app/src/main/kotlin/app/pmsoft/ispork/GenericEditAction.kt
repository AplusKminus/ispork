package app.pmsoft.ispork

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.data.ISporkEntry

class GenericEditAction<E : ISporkEntry>(
  private val editActivityClass: Class<out AppCompatActivity>,
  private val titleStringId: Int,
  override val requestCode: Int,
  private val intentExtraName: String
) : SelectedListItemAction<E> {
  override val menuItemId: Int = R.id.action_edit_item

  override fun addMenuItem(
    menu: Menu,
    context: Context
  ) {
    menu.add(
      Menu.NONE,
      menuItemId,
      Menu.NONE,
      R.string.edit
    ).also {
      it.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      it.icon = context.resources.getDrawable(
        R.drawable.ic_edit_white_24dp,
        context.theme
      )
    }
  }

  override fun appliesToNItems(n: Int): Boolean {
    return n == 1
  }

  override fun apply(
    items: List<E>,
    activity: AbstractListActivity<E, *, *>
  ) {
    val intent = Intent(
      activity,
      editActivityClass
    )
    intent.putExtra(
      "title",
      activity.getString(titleStringId)
    )
    intent.putExtra(
      intentExtraName,
      items[0] as Parcelable
    )
    activity.startActivityForResult(
      intent,
      requestCode
    )
  }

  override fun handleActivityResult(
    data: Intent?,
    resultCode: Int,
    viewModel: ListViewModel<E>
  ) {
    if (resultCode == Activity.RESULT_OK) {
      data?.getParcelableExtra<E>(intentExtraName)?.let {
        viewModel.persist(it)
      }
    }
  }
}
