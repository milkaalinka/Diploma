package com.alinaincorporated.diploma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TransactionEntity::class],
    version = 1,
)
@TypeConverters(
    value = [DatabaseTimeConverter::class]
)
abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        fun create(context: Context): TransactionsDatabase {
            return Room.databaseBuilder(
                context,
                TransactionsDatabase::class.java, "transactions_database"
            )
                .addTypeConverter(DatabaseTimeConverter())
                .build()
        }
    }
}