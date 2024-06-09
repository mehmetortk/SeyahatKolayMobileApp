package com.example.busapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seyahatkolaymobileapp.R

class KoltukAdapter(private val koltukList: List<Koltuk>) :
    RecyclerView.Adapter<KoltukAdapter.KoltukViewHolder>() {

    class KoltukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val koltukNumara: TextView = itemView.findViewById(R.id.koltuk_numara)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KoltukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_koltuk, parent, false)
        return KoltukViewHolder(view)
    }

    override fun onBindViewHolder(holder: KoltukViewHolder, position: Int) {
        val koltuk = koltukList[position]
        holder.koltukNumara.text = koltuk.numara


        if (koltuk.dolu) {
            holder.itemView.setBackgroundResource(R.color.colorSeatUnavailable)
        } else {
            holder.itemView.setBackgroundResource(R.color.colorSeatAvailable)
        }

        holder.itemView.setOnClickListener {
            if (!koltuk.dolu) {
                // Handle seat selection
                koltuk.dolu = true
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return koltukList.size
    }
}
