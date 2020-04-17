package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
  tableName = "savingGoal",
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
open class SavingGoal(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  open var name: String,

  @ColumnInfo(name = "rate")
  open var rate: Long?,

  @ColumnInfo(name = "target_amount")
  open var targetAmount: Long?,

  @ColumnInfo(name = "target_date")
  open var targetDate: Date?,

  @ColumnInfo(name = "notes")
  open var notes: String?,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?
) : Parcelable,
  ISporkEntry {

  @Ignore
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

@Parcelize
class FullSavingGoal(

  @Ignore
  override var id: Long,

  @Ignore
  override var name: String,

  @Ignore
  override var rate: Long?,

  @Ignore
  override var targetAmount: Long?,

  @Ignore
  override var targetDate: Date?,

  @Ignore
  override var notes: String?,

  @Relation(
    entity = BudgetPot::class,
    entityColumn = "id",
    parentColumn = "budget_pot_id"
  )
  var budgetPot: FullBudgetPot?
) : SavingGoal() {

  @Ignore
  constructor() : this(
    0,
    "",
    0,
    0,
    null,
    null,
    null
  )

  override var budgetPotId: Long?
    get() = budgetPot?.id
    set(value) {
      super.budgetPotId = value
    }
}
