package app.pmsoft.ispork.category

import androidx.lifecycle.ViewModelProvider
import app.pmsoft.ispork.*
import app.pmsoft.ispork.data.Category

class CategoryListActivity
  : AbstractListActivity<Category, CategoryListViewHolder, CategoryListViewAdapter>() {

  override val activityActions: List<ActivityAction<Category>> = listOf(
    GenericCreateAction(
      CategoryEditActivity::class.java,
      R.string.category_create_activity_title,
      RequestCodes.CATEGORY_CREATION_REQUEST_CODE,
      "category"
    )
  )
  override val selectedActions: List<SelectedListItemAction<Category>> = listOf(
    GenericEditAction(
      CategoryEditActivity::class.java,
      R.string.category_edit_activity_title,
      RequestCodes.CATEGORY_EDITING_REQUEST_CODE,
      "category"
    ),
    GenericDeleteAction(
      R.plurals.title_dialog_category_deletion,
      R.plurals.category_deletion_message
    )
  )

  override fun createViewAdapter(): CategoryListViewAdapter {
    return CategoryListViewAdapter(this)
  }

  override fun createViewModel(): ListViewModel<Category> {
    return ViewModelProvider(this).get(CategoryListViewModel::class.java)
  }
}
