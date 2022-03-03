package com.nino.blindbox.ui.fragment.showFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nino.blindbox.R
import com.nino.blindbox.blindbox.box
import com.nino.blindbox.blindbox.series
import com.nino.blindbox.ui.adapter.seriesAdapter
import java.util.*


class ppmtFragment: Fragment() {
    private var seriesRecycleView: RecyclerView? = null
    private var seriesAdapter: seriesAdapter ? = null
    private var boxes= LinkedList<box>()
    private var datas= LinkedList<series>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.show_fragment_detail, null)

        initView(view)
        initData()
        return view
    }
    fun initView(view: View){
        seriesRecycleView = view.findViewById(R.id.seriesRecyclerView)
    }

    fun initData(){
        datas.add(series("Dimoo",5,false,R.drawable.card_dimoo_example))
        datas.add(series("SkullPanda",5,false,R.drawable.card_dimoo_example))
        seriesAdapter = seriesAdapter(datas)
        seriesRecycleView?.layoutManager = GridLayoutManager(context, 1)
        seriesRecycleView?.adapter = seriesAdapter
    }
    fun initBoxes(){
        boxes.add(box("dimoo",true,R.drawable.card_dimoo_example))
        boxes.add(box("dimoo",true,R.drawable.card_dimoo_example))
        boxes.add(box("dimoo",true,R.drawable.card_dimoo_example))
        boxes.add(box("dimoo",true,R.drawable.card_dimoo_example))
        boxes.add(box("dimoo",true,R.drawable.card_dimoo_example))
    }
}