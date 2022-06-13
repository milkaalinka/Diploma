package com.alinaincorporated.diploma.ui.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionEntity

class TransactionAdapter(private val transaction: List<TransactionEntity>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCategory: TextView
        val textViewDate: TextView
        val textViewAmount: TextView

        init {
            textViewCategory = view.findViewById(R.id.transactTextName)
            textViewDate = view.findViewById(R.id.transactDate)
            textViewAmount = view.findViewById(R.id.transactAmount)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textViewCategory.text = transaction[position].toString()
        viewHolder.textViewDate.text = transaction[position].toString()
        viewHolder.textViewAmount.text = transaction[position].toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = transaction.size

}
