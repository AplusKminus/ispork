package app.pmsoft.ispork.data

import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class FullSavingGoal(
  @Ignore
  override var id: Long,
  @Ignore
  override var name: String,
  @Ignore
  override var notes: String?,
  @Relation(
    entityColumn = "id",
    parentColumn = "category_id"
  )
  var category: Category?,
  @Ignore
  override var rate: Long?,
  @Ignore
  override var targetAmount: Long?,
  @Ignore
  override var targetDate: Date?
) : SavingGoal() {

  override var categoryId: Long?
    get() = category?.id
    set(value) {
      super.categoryId = value
    }

  constructor() : this(
    0,
    "",
    null,
    null,
    null,
    null,
    null
  )
}
