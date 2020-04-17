package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
class FullBudgetPot(

  @Ignore
  override var id: Long,

  @Ignore
  override var priority: Int,

  @Relation(
    parentColumn = "category_id",
    entityColumn = "id",
    entity = Category::class
  )
  var category: Category?

) : BudgetPot(),
  Parcelable {

  @Ignore
  constructor() : this(
    0,
    0,
    null
  )
}
