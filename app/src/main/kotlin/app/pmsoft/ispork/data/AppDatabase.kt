package app.pmsoft.ispork.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.Provides
import javax.inject.Singleton

@Database(
  entities = [
    BudgetEntry::class,
    BudgetPot::class,
    BudgetPotAnnotation::class,
    Category::class,
    MoneyBag::class,
    Participant::class,
    SavingGoal::class,
    SavingGoalSplit::class,
    ScheduledRhythm::class,
    ScheduledTransaction::class,
    SpendingBuffer::class,
    SubTransaction::class,
    TransactionDefinition::class
  ],
  version = 1
)
@TypeConverters(
  TimestampConverter::class,
  Interval.Unit.Converter::class,
  OffsetType.Converter::class,
  CurrencyTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun categoryDao(): CategoryDao

  abstract fun transactionDao(): TransactionDefinitionDao

  abstract fun subTransactionDao(): SubTransactionDao

  abstract fun participantDao(): ParticipantDao

  abstract fun budgetPotAnnotationDao(): BudgetPotAnnotationDao

  abstract fun savingGoalDao(): SavingGoalDao

  abstract fun scheduledTransactionDao(): ScheduledTransactionDao

  abstract fun scheduledRhythmDao(): ScheduledRhythmDao

  abstract fun spendingBufferDao(): SpendingBufferDao
}

@Singleton
@Provides
fun provideDatabase(context: Context): AppDatabase {
  return Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "iSpork.db"
  )
    .allowMainThreadQueries()
    .build()
}
