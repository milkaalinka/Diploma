package com.alinaincorporated.diploma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TransactionEntity::class],
    version = 3,
)
abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        fun create(context: Context): TransactionsDatabase {
            return Room.databaseBuilder(
                context,
                TransactionsDatabase::class.java, "transactions_database"
            )   .fallbackToDestructiveMigration()
                .build()
        }
    }
}