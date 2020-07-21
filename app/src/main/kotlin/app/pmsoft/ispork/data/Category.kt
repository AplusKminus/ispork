package app.pmsoft.ispork.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * A category serves as an aggregator for [budget pots][BudgetPot].
 *
 * There is no limit to the number of budget pots that can be associated with one category.
 */
@Entity(
  tableName = "categories"
)
@Parcelize
class Category(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  var name: String
) : ISporkEntry {

  constructor() : this(
    0,
    ""
  )
}
