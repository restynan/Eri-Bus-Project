package com.emito.eribus.fragment

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.emito.eribus.R
import kotlinx.android.synthetic.main.fragment_book.*


class bookFragment : Fragment() {

    private lateinit var  values : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_book, container, false)


        return view
    }



}