package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
  tableName = "budgetPot",
  foreignKeys = [
    ForeignKey(
      entity = Category::class,
      parentColumns = ["id"],
      childColumns = ["category_id"]
    )
  ],
  indices = [Index("category_id")]
)
@Parcelize
open class BudgetPot(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "priority")
  open var priority: Int,

  @ColumnInfo(name = "category_id")
  open var categoryId: Long?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    null
  )
}
