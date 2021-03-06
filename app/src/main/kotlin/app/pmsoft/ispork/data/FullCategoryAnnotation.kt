package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
class FullCategoryAnnotation(

  @Ignore
  override var id: Long,
  @Ignore
  override var subTransactionId: Long,
  @Ignore
  override var amount: Long,
  @Relation(
    entityColumn = "id",
    parentColumn = "category_id"
  )
  var category: Category?,
  @Ignore
  override var notes: String?
) : CategoryAnnotation(),
  Parcelable {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    null,
    null
  )

  override var categoryId: Long?
    get() = category?.id
    set(value) {
      super.categoryId = value
    }
}
