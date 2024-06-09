package com.example.busapp.com.example.seyahatkolaymobileapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.busapp.Sefer
import com.example.seyahatkolaymobileapp.R

class SeferAdapter(private val seferList: MutableList<Sefer>, private val clickListener: (Sefer) -> Unit) :
    RecyclerView.Adapter<SeferAdapter.SeferViewHolder>() {

    class SeferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seferSaat: TextView = itemView.findViewById(R.id.sefer_saat)
        val seferFiyat: TextView = itemView.findViewById(R.id.sefer_fiyat)
        val kalanKoltuk: TextView = itemView.findViewById(R.id.kalan_koltuk)
        val seferFirma: TextView = itemView.findViewById(R.id.sefer_firma)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sefer, parent, false)
        return SeferViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeferViewHolder, position: Int) {
        val sefer = seferList[position]
        holder.seferSaat.text = sefer.saat
        holder.seferFiyat.text = sefer.fiyat
        holder.kalanKoltuk.text = "Son ${sefer.kalan_koltuk} Koltuk"
        holder.seferFirma.text = sefer.firma

        holder.itemView.setOnClickListener {
            clickListener(sefer)
        }
    }

    override fun getItemCount(): Int {
        return seferList.size
    }

    fun updateSeferler(newSeferList: List<Sefer>) {
        seferList.clear()
        seferList.addAll(newSeferList)
        notifyDataSetChanged()
    }
}
