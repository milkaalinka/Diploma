package com.alinaincorporated.diploma.ui.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionEntity
import com.alinaincorporated.diploma.databinding.ActivityMainBinding.inflate
import com.alinaincorporated.diploma.databinding.FragmentAddTransactionBinding.inflate
import com.alinaincorporated.diploma.databinding.ListItemViewBinding
import com.alinaincorporated.diploma.databinding.ListItemViewBinding.inflate

class TransactionAdapter(private val transactionList: List<TransactionEntity>) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val itemBinding = ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transactionBean: TransactionEntity = transactionList[position]
        holder.bind(transactionBean)
    }

    override fun getItemCount(): Int = transactionList.size

    class TransactionHolder(private val itemBinding: ListItemViewBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(transactionBean: TransactionEntity) {
            itemBinding.transactTextName.text = transactionBean.categoryId.toString()
            itemBinding.transactDate.text = transactionBean.dateTime.toString()
            itemBinding.transactAmount.text = transactionBean.amount.toString()
        }
    }
}

