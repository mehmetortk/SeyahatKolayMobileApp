package com.example.busapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seyahatkolaymobileapp.R

class SeferFragment : Fragment() {

    companion object {
        private const val SEFER_KEY = "sefer"

        fun newInstance(sefer: Sefer): SeferFragment {
            val fragment = SeferFragment()
            val args = Bundle()
            args.putParcelable(SEFER_KEY, sefer)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var sefer: Sefer
    private lateinit var recyclerView: RecyclerView
    private lateinit var koltukAdapter: KoltukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sefer = arguments?.getParcelable(SEFER_KEY) ?: throw IllegalArgumentException("Sefer is required")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sefer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_koltuk)
        recyclerView.layoutManager = GridLayoutManager(context, 4) // 4 columns for seats

        val koltukList = mutableListOf<Koltuk>() // Load your seat data here
        koltukAdapter = KoltukAdapter(koltukList)
        recyclerView.adapter = koltukAdapter
    }
}
