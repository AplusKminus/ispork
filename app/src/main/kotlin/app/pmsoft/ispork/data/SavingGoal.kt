package app.pmsoft.ispork.data

import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * A saving goal defines a budgeting process where budget is accumulated until target conditions are met.
 *
 * A saving goal is defined by exactly two of
 * - Saving rate
 * - Target date
 * - Target amount
 *
 * It can be split into parallel [sub goals][SavingGoalSplit] which are not milestones or stages, but rather a division
 * into categories. Each split has its own [BudgetPot].
 *
 * A saving goal can only be split if the saving rate is undefined and the target date fixed. Each split gets its own
 * target amount.
 */
@Entity(
  tableName = "saving_goals"
)
@Parcelize
open class SavingGoal(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  open var name: String,

  @ColumnInfo(name = "rate")
  open var rate: Amount?,

  @Embedded(prefix = "interval_")
  open var interval: Interval,

  @ColumnInfo(name = "target_date")
  open var targetDate: Date?,

  @ColumnInfo(name = "notes")
  open var notes: String?

) : ISporkEntry {

  @Ignore
  constructor() : this(
    0,
    "",
    null,
    Interval(1),
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
  override var rate: Amount?,
  @Ignore
  override var interval: Interval,
  @Ignore
  override var targetDate: Date?,
  @Ignore
  override var notes: String?,
  @Relation(
    entity = SavingGoalSplit::class,
    entityColumn = "saving_goal_id",
    parentColumn = "id"
  )
  var splits: List<SavingGoalSplit>
) : SavingGoal() {

  @Ignore
  constructor() : this(
    0,
    "",
    null,
    Interval(1),
    null,
    null,
    emptyList()
  )
}
