package com.alinaincorporated.diploma.ui.transactions.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alinaincorporated.diploma.database.TransactionEntity
import com.alinaincorporated.diploma.ui.transactions.TransactionCategoryMapper

class TransactionAdapter(
    private val transactions: List<TransactionEntity>,
    private val categoryMapper: TransactionCategoryMapper,
) : RecyclerView.Adapter<TransactionListViewHolder>() {

    override fun getItemCount(): Int = transactions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        return TransactionListViewHolder.create(parent, categoryMapper)
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        val transactionBean: TransactionEntity = transactions[position]
        holder.bind(transactionBean)
    }
}

