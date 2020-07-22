package app.pmsoft.ispork.transaction

import androidx.lifecycle.ViewModelProvider
import app.pmsoft.ispork.*
import app.pmsoft.ispork.data.FullTransactionDefinition

class TransactionListActivity
  : AbstractListActivity<FullTransactionDefinition, TransactionListViewHolder, TransactionListViewAdapter>() {

  override val activityActions: List<ActivityAction<FullTransactionDefinition>> = listOf(
    GenericCreateAction(
      TransactionEditActivity::class.java,
      R.string.transaction_create_activity_title,
      RequestCodes.TRANSACTION_CREATION_REQUEST_CODE,
      "transaction"
    )
  )
  override val selectedActions: List<SelectedListItemAction<FullTransactionDefinition>> = listOf(
    GenericEditAction(
      TransactionEditActivity::class.java,
      R.string.transaction_edit_activity_title,
      RequestCodes.TRANSACTION_EDITING_REQUEST_CODE,
      "transaction"
    ),
    GenericDeleteAction(
      R.plurals.title_dialog_transaction_deletion,
      R.plurals.transaction_deletion_message
    )
  )

  override fun createViewAdapter(): TransactionListViewAdapter {
    return TransactionListViewAdapter(this)
  }

  override fun createViewModel(): ListViewModel<FullTransactionDefinition> {
    return ViewModelProvider(this).get(BookedTransactionListViewModel::class.java)
  }
}
