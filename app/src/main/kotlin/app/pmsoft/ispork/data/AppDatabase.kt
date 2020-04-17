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
    Participant::class,
    Category::class,
    BudgetPot::class,
    ScheduledTransaction::class,
    ScheduledOccurrence::class,
    SubTransactionTemplate::class,
    BudgetPotAnnotationTemplate::class,
    SpendingBuffer::class,
    Transaction::class,
    SubTransaction::class,
    BudgetPotAnnotation::class,
    SavingGoal::class],
  version = 1
)
@TypeConverters(
  TimestampConverter::class,
  IntervalUnit.Companion.Converter::class
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun categoryDao(): CategoryDao

  abstract fun transactionDao(): TransactionDao

  abstract fun subTransactionDao(): SubTransactionDao

  abstract fun participantDao(): ParticipantDao

  abstract fun budgetPotAnnotationDao(): BudgetPotAnnotationDao

  abstract fun savingGoalDao(): SavingGoalDao

  abstract fun scheduledTransactionDao(): ScheduledTransactionDao

  abstract fun scheduledOccurrenceDao(): ScheduledOccurrenceDao
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
