package com.example.guessit.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.guessit.R
import com.example.guessit.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: MainFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment,
            container, false)

        binding.start.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCreateRoomFragment())
        }

        binding.join.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToJoinFragment())
        }

        return binding.root
    }



}