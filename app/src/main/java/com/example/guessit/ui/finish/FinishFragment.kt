package com.example.guessit.ui.finish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.guessit.R
import com.example.guessit.databinding.FragmentFinishBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FinishFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinishFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentFinishBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish,
                        container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = FinishFragment()
    }
}