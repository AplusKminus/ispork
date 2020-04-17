package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
  tableName = "bufferDefinition",
  foreignKeys = [
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("budget_pot_id")]
)
@Parcelize
open class SpendingBuffer(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "min")
  open var min: Long,

  @ColumnInfo(name = "max")
  open var max: Long,

  @ColumnInfo(name = "rate")
  open var rate: Long,

  @ColumnInfo(name = "intervalLength")
  open var intervalLength: Int,

  @ColumnInfo(name = "intervalUnit")
  open var intervalUnit: IntervalUnit?,

  @ColumnInfo(name = "notes")
  open var notes: String?,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    0,
    0,
    1,
    null,
    null,
    null
  )
}
