package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
class FullBudgetPotAnnotation(

  @Ignore
  override var id: Long,
  @Ignore
  override var subTransactionId: Long,
  @Ignore
  override var amount: Long,
  @Relation(
    entityColumn = "id",
    parentColumn = "budget_pot_id",
    entity = BudgetPot::class
  )
  var budgetPot: FullBudgetPot?,
  @Ignore
  override var notes: String?
) : BudgetPotAnnotation(),
  Parcelable {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    null,
    null
  )

  override var budgetPotId: Long?
    get() = budgetPot?.id
    set(value) {
      super.budgetPotId = value
    }
}
