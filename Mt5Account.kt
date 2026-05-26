package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "mt5_accounts")
data class Mt5Account(
    @PrimaryKey
    val id: String,
    val login: String,
    val aliasName: String,
    val broker: String,
    val accountGroup: String, // e.g., "SMC Core", "Macro Overlay", "Prop Tactical"
    val leverage: Int,        // e.g., 100 for 1:100
    val initialBalance: Double,
    val balance: Double,
    val equity: Double,
    val type: String,         // "LIVE", "DEMO", "INSTITUTIONAL"
    val isCopierActive: Boolean = true,
    val winRatePercent: Double = 64.5,
    val totalTrades: Int = 120,
    val profitFactor: Double = 2.45
)

@Dao
interface Mt5AccountDao {
    @Query("SELECT * FROM mt5_accounts ORDER BY id DESC")
    fun getAllAccountsFlow(): Flow<List<Mt5Account>>

    @Query("SELECT * FROM mt5_accounts")
    suspend fun getAllAccountsDirect(): List<Mt5Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Mt5Account)

    @Update
    suspend fun updateAccount(account: Mt5Account)

    @Delete
    suspend fun deleteAccount(account: Mt5Account)

    @Query("UPDATE mt5_accounts SET balance = :balance, equity = :equity WHERE id = :id")
    suspend fun updateEquityBalance(id: String, balance: Double, equity: Double)
}
