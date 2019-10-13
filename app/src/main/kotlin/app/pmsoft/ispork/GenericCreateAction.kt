package app.pmsoft.ispork

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.data.ISporkEntry

class GenericCreateAction<E : ISporkEntry>(
  private val creationActivityClass: Class<out AppCompatActivity>,
  private val titleStringId: Int,
  override val requestCode: Int,
  private val intentExtraName: String
) : ActivityAction<E> {
  override val menuItemId: Int = R.id.action_create_item

  override fun addMenuItem(
    menu: Menu,
    context: Context
  ) {
    menu.add(
      Menu.NONE,
      menuItemId,
      Menu.NONE,
      R.string.create
    ).also {
      it.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      it.icon = context.resources.getDrawable(
        R.drawable.ic_add_box_white_24dp,
        context.theme
      )
    }
  }

  override fun apply(
    activity: AbstractListActivity<E, *, *>
  ) {
    val intent = Intent(
      activity,
      creationActivityClass
    )
    intent.putExtra(
      "title",
      activity.getString(titleStringId)
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
