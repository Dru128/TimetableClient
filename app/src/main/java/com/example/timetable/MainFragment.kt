package com.example.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.data.BusData
import com.example.timetable.data.BusStop
import com.example.timetable.data.Repository
import com.example.timetable.firebase.BusFireBase
import com.google.firebase.firestore.GeoPoint


class MainFragment : Fragment()
{

    private var db = BusFireBase()
    private var adapter = RecyclerAdapterMarshrut{ findNavController().navigate(R.id.action_mainFragment_to_mapsFragment) }
    private var recyclerView: RecyclerView? = null
    private var parent: ConstraintLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        var root = inflater.inflate(R.layout.fragment_main, container, false)

        parent = root.findViewById(R.id.parentMainFragment)
        recyclerView = root.findViewById(R.id.recucler_View_Mainfrag)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        getData()

        return root
    }

    private fun getData() // получение данных и загрузка в список
    {
        db.ref
            .get()
            .addOnSuccessListener { result ->
                val data: MutableList<BusData> = mutableListOf()
                for (doc in result)
                    data.add(
                        doc.toObject(BusData::class.java)
                    )

                // здесь уже данные успешно получены
                parent?.removeView(parent?.findViewById(R.id.progressMainFragment))
                adapter.dataset = data
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                Repository.busData = data[0] // это для теста, при вызове карт передавайте данные (Repository/busdata)

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // всё плохо
                Toast.makeText(context,it.message.toString() , Toast.LENGTH_LONG).show()

            }
    }
}