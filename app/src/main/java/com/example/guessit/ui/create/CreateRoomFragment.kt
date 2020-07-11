package com.example.guessit.ui.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.guessit.R
import com.example.guessit.databinding.FragmentCreateRoomBinding


/**
 * A simple [Fragment] subclass.
 * Use the [CreateRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateRoomFragment : Fragment() {

    private lateinit var binding: FragmentCreateRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_room,
            container, false)

        binding.createRoom.setOnClickListener {
            val action = CreateRoomFragmentDirections.actionCreateRoomFragmentToWaitingFragment(
                binding.player.text.toString()
            )
            action.player1 = binding.player.text.toString()
            findNavController().navigate(action)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateRoomFragment()
    }
}