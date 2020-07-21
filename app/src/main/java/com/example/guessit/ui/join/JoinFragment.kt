package com.example.guessit.ui.join

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.guessit.R
import com.example.guessit.databinding.FragmentCreateRoomBinding
import com.example.guessit.databinding.FragmentJoinBinding
import com.example.guessit.model.Game
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_join.*

/**
 * A simple [Fragment] subclass.
 * Use the [JoinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JoinFragment : Fragment() {

    private lateinit var binding: FragmentJoinBinding
    private lateinit var viewmodel: JoinViewModel
    private lateinit var database: DatabaseReference
    private lateinit var game: Game

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_join,
            container,
            false
        )

        viewmodel = ViewModelProviders.of(this).get(JoinViewModel::class.java)

        binding.viewModel = viewmodel

        database = FirebaseDatabase.getInstance().reference.child("games")

        binding.joinRoom.setOnClickListener {
            viewmodel.setPlayer(binding.player2.text.toString())
            viewmodel.setCode(binding.code.text.toString())
            viewmodel.join()
        }

        viewmodel.joinError.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(context, "Game not found", Toast.LENGTH_LONG).show()
            } else {
                val action = JoinFragmentDirections.actionJoinFragmentToGameFragment(viewmodel.code.value)
                action.isJoiner = true
                action.isCreator = false
                findNavController().navigate(action)
            }
        })

        viewmodel.gameFull.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(context, "Game is full", Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = JoinFragment()
    }
}