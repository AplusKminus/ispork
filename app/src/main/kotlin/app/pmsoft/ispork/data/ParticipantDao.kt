package app.pmsoft.ispork.data

import androidx.room.*

@Dao
interface ParticipantDao {

  @Query("SELECT id, starting_balance, currency, participant_id, bookedBalance FROM money_bags LEFT JOIN (SELECT SUM(amount_in_booked_currency) as bookedBalance, money_bag_id FROM sub_transactions WHERE booking_date IS NOT NULL GROUP BY money_bag_id) ON (money_bag_id = id) WHERE participant_id = :participantId")
  @Transaction
  fun getMoneyBags(participantId: Long): List<LoadedMoneyBag>

  @Transaction
  fun getAllFull(): List<FullParticipant> = getAll().map {
    FullParticipant(it, getMoneyBags(it.id))
  }

  @Query("SELECT * FROM participants ORDER BY name")
  fun getAll(): List<Participant>

  @Query("SELECT * FROM participants WHERE type IS 'ACCOUNT' ORDER BY name")
  fun getAccounts(): List<Participant>

  @Query("SELECT * FROM participants WHERE type IS 'PAYEE' ORDER BY name")
  fun getPayees(): List<Participant>

  @Query("SELECT * FROM participants WHERE type is 'PERSON' ORDER BY name")
  fun getPersons(): List<Participant>

  @Query("SELECT * FROM participants WHERE id IS :id LIMIT 1")
  @Transaction
  fun getById(id: Long): Participant?

  @Transaction
  fun getFullById(id: Long): FullParticipant? {
    return FullParticipant(getById(id) ?: return null, getMoneyBags(id))
  }

  @Update
  fun update(participant: Participant)

  @Update
  fun update(moneyBag: MoneyBag)

  @Insert
  fun insert(participant: Participant): Long

  @Insert
  fun insert(moneyBag: MoneyBag): Long

  @Insert
  fun insertAll(vararg participants: Participant)

  @Delete
  fun delete(participant: List<Participant>)

  @Delete
  fun delete(participant: Participant)
}
