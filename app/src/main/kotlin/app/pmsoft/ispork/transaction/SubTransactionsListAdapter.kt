package app.pmsoft.ispork.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.FullBudgetPotAnnotation
import app.pmsoft.ispork.data.FullSubTransaction

class SubTransactionsListAdapter(
  private val subTransactionListHandler: SubTransactionListHandler<FullSubTransaction, FullBudgetPotAnnotation>
) : RecyclerView.Adapter<SubTransactionListViewHolder>(),
  Observer<List<SubTransactionEditWrapper>> {

  private var dataSet: List<SubTransactionEditWrapper> = emptyList()

  override fun getItemCount(): Int = dataSet.size

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SubTransactionListViewHolder {
    return SubTransactionListViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(
          R.layout.sub_transaction_edit_fragment,
          parent,
          false
        ),
      subTransactionListHandler
    )
  }

  override fun onBindViewHolder(
    subTransactionListViewHolder: SubTransactionListViewHolder,
    position: Int
  ) {
    subTransactionListViewHolder.setData(
      dataSet[position]
    )
  }

  override fun onChanged(newData: List<SubTransactionEditWrapper>) {
    dataSet = newData
    notifyDataSetChanged()
  }
}
