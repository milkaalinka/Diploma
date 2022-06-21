package com.alinaincorporated.diploma.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id") val id: Int = 0,
    @ColumnInfo(name = "transaction_amount") val amount: Float,
    @ColumnInfo(name = "transaction_description") val description: String? = null,
    @ColumnInfo(name = "transaction_datetime") val dateTime: Long,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "is_expense") val isExpense: Boolean,
) {

    val absAmount: Float
        get() = if (isExpense) -amount else amount
}