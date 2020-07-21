package app.pmsoft.ispork.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import app.pmsoft.ispork.AbstractListViewAdapter
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullTransactionDefinition

class TransactionListViewAdapter(
  private val selectionHandler: SelectionHandler<FullTransactionDefinition>
) : AbstractListViewAdapter<FullTransactionDefinition, TransactionListViewHolder>() {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): TransactionListViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(
        R.layout.list_item_transaction,
        parent,
        false
      )
    return TransactionListViewHolder(
      view,
      selectionHandler
    )
  }
}
