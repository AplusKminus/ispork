package app.pmsoft.ispork.transaction

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import app.pmsoft.ispork.AbstractViewHolder
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullTransaction
import app.pmsoft.ispork.util.CurrencyHandler
import app.pmsoft.ispork.view.CategoryDisplayLabel

class TransactionListViewHolder(
  view: View,
  selectionHandler: SelectionHandler<FullTransaction>
) : AbstractViewHolder<FullTransaction>(
  view,
  selectionHandler
) {
  private val transferAmountLabel: TextView = view.findViewById(R.id.transaction_list_item_transfer_amount_label)
  private val externalAmountLabel: TextView = view.findViewById(R.id.transaction_list_item_external_amount_label)
  private val notesLabel: TextView = view.findViewById(R.id.transaction_list_item_notes_label)
  private val originsLabel: TextView = view.findViewById(R.id.transaction_list_item_origins_label)
  private val destinationsLabel: TextView = view.findViewById(R.id.transaction_list_item_destinations_label)
  private val categoryLabel: CategoryDisplayLabel = view.findViewById(R.id.transaction_list_item_category_label)
  private val layout: FrameLayout = view.findViewById(R.id.transaction_entry_layout)
  private val inflowIcon: Drawable = itemView.context.resources.getDrawable(
    R.drawable.ic_inflow_24dp,
    itemView.context.theme
  ).also {
    it.setTint(getColor(R.color.positive_flow_color))
  }
  private val outflowIcon: Drawable = itemView.context.resources.getDrawable(
    R.drawable.ic_outflow_24px,
    itemView.context.theme
  ).also {
    it.setTint(getColor(R.color.negative_flow_color))
  }

  override val backgroundView: View
    get() = layout

  override fun updateViewFromData(e: FullTransaction) {
    if (e.notes.isNullOrBlank()) {
      notesLabel.visibility = View.GONE
    } else {
      notesLabel.text = e.notes
      notesLabel.visibility = View.VISIBLE
    }
    val transferSum = e.getInternalSum()
    val externalSum = e.getExternalSum()

    if (transferSum != 0L) {
      transferAmountLabel.visibility = View.VISIBLE
      transferAmountLabel.text = CurrencyHandler.format(transferSum)
    } else {
      transferAmountLabel.visibility = View.GONE
    }
    if (externalSum != 0L) {
      externalAmountLabel.visibility = View.VISIBLE
      externalAmountLabel.text = CurrencyHandler.format(externalSum)
      if (externalSum > 0) {
        externalAmountLabel.setTextColor(getColor(R.color.positive_flow_color))
        externalAmountLabel.setCompoundDrawablesWithIntrinsicBounds(
          inflowIcon,
          null,
          null,
          null
        )
      } else {
        externalAmountLabel.setTextColor(getColor(R.color.negative_flow_color))
        externalAmountLabel.setCompoundDrawablesWithIntrinsicBounds(
          outflowIcon,
          null,
          null,
          null
        )
      }
    } else {
      externalAmountLabel.visibility = View.GONE
    }
    originsLabel.text = e.getOrigins().joinToString(", ") { it.name }
    destinationsLabel.text = e.getDestinations().joinToString(", ") { it.name }

    categoryLabel.displayCategoryFor(e.subTransactions)
  }
}
