package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 * A saving goal split is a mandatory part of a [SavingGoal].
 *
 * In case the saving goal targets different [categories][Category], several splits are used to set a unique target
 * amount for each associated [BudgetPot].
 */
@Entity(
  tableName = "saving_goal_splits",
  foreignKeys = [
    ForeignKey(
      entity = BudgetPot::class,
      parentColumns = ["id"],
      childColumns = ["budget_pot_id"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = SavingGoal::class,
      parentColumns = ["id"],
      childColumns = ["saving_goal_id"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("budget_pot_id"), Index("saving_goal_id")]
)
@Parcelize
open class SavingGoalSplit(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "saving_goal_id")
  open var savingGoalId: Long,

  @ColumnInfo(name = "target_amount")
  open var targetAmount: Amount?,

  @ColumnInfo(name = "budget_pot_id")
  open var budgetPotId: Long?,

  @ColumnInfo(name = "notes")
  open var notes: String?
) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    0,
    null,
    null,
    null
  )
}

@Parcelize
class FullSavingGoalSplit(

  @Ignore
  override var id: Long,

  @Ignore
  override var targetAmount: Amount?,

  @Ignore
  override var savingGoalId: Long,

  @Ignore
  override var notes: String?,

  @Relation(
    entity = BudgetPot::class,
    entityColumn = "id",
    parentColumn = "budget_pot_id"
  )
  var budgetPot: FullBudgetPot?
) : SavingGoalSplit() {

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
