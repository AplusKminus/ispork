package app.pmsoft.ispork.data

import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
class FullScheduledTransaction(

  @Ignore
  override var id: Long,

  @Ignore
  override var name: String,

  @Relation(
    entityColumn = "scheduled_transaction_id",
    parentColumn = "id"
  )
  var occurrences: List<ScheduledOccurrence>,

  @Relation(
    entityColumn = "scheduled_transaction_id",
    parentColumn = "id",
    entity = SubTransactionTemplate::class
  )
  var subTransactionTemplates: List<FullSubTransactionTemplate>

) : ScheduledTransaction() {

  @Ignore
  constructor() : this(
    0,
    "",
    emptyList(),
    emptyList()
  )
}

@Parcelize
class FullSubTransactionTemplate(

  @Ignore
  override var id: Long,

  @Ignore
  override var amount: Long,

  @Relation(
    entityColumn = "id",
    parentColumn = "participant_id",
    entity = Participant::class
  )
  var participant: Participant?,

  @Relation(
    entityColumn = "sub_transaction_template_id",
    parentColumn = "id",
    entity = BudgetPotAnnotationTemplate::class
  )
  var budgetPotAnnotationTemplates: List<FullBudgetPotAnnotationTemplate>,

  @Ignore
  override var notesTemplate: String?
) : SubTransactionTemplate() {

  @Ignore
  constructor() : this(
    0,
    0,
    null,
    emptyList(),
    null
  )

  override var participantId: Long?
    get() = participant?.id
    set(value) {
      super.participantId = value
    }
}

@Parcelize
class FullBudgetPotAnnotationTemplate(

  @Ignore
  override var id: Long,

  @Ignore
  override var amount: Long,

  @Relation(
    entityColumn = "id",
    parentColumn = "budget_pot_id",
    entity = BudgetPot::class
  )
  var budgetPot: FullBudgetPot?,

  @Ignore
  override var notesTemplate: String?
) : BudgetPotAnnotationTemplate() {

  @Ignore
  constructor() : this(
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
