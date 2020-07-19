package com.example.guessit.ui.game

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.guessit.R
import com.example.guessit.databinding.FragmentGameBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var database: DatabaseReference
    private lateinit var playerTurnListener: ValueEventListener
    private lateinit var gameFinishListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = FirebaseDatabase.getInstance().reference.child("games")
        // Inflate the layout for this fragment
        val binding: FragmentGameBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_game,
            container,
            false)

        val viewModelFactory = GameViewModelFactory(
            GameFragmentArgs.fromBundle(requireArguments()).isCreator,
            GameFragmentArgs.fromBundle(requireArguments()).isJoiner,
            GameFragmentArgs.fromBundle(requireArguments()).code)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if(GameFragmentArgs.fromBundle(requireArguments()).isCreator) {
            binding.skipButton.isEnabled = false
            binding.correctButton.isEnabled = false
            binding.startButton.isEnabled = true
        }

        if(GameFragmentArgs.fromBundle(requireArguments()).isJoiner) {
            binding.skipButton.isEnabled = false
            binding.correctButton.isEnabled = false
            binding.startButton.isEnabled = false
        }

        if(GameFragmentArgs.fromBundle(requireArguments()).isCreator){
            viewModel.player2Turn.observe(viewLifecycleOwner, Observer {
                if(it){
                    binding.startButton.isEnabled = false
                    binding.correctButton.isEnabled = false
                    binding.skipButton.isEnabled = false
                }
            })
        }

        playerTurnListener = object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    if (GameFragmentArgs.fromBundle(requireArguments()).isJoiner) {
                        Log.e(TAG, "TRUE")
                        binding.startButton.isEnabled = true
                    }
                }
            }
        }

        gameFinishListener = object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    Log.e(TAG, "FINISH!!!!")
                    if (GameFragmentArgs.fromBundle(requireArguments()).isCreator) {
                        viewModel.endGameForCreator()
                    }
                }
            }
        }

        database.child(viewModel.code.value!!).child("player2Turn").addValueEventListener(playerTurnListener)
        database.child(viewModel.code.value!!).child("gameFinished").addValueEventListener(gameFinishListener)

        database.child(viewModel.code.value!!).child("player1Score").addValueEventListener(
            object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val score: Int? = snapshot.getValue<Int>()
                    viewModel.setPlayer1Score(score!!)
                }
            }
        )

        database.child(viewModel.code.value!!).child("player2Score").addValueEventListener(
            object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val score: Int? = snapshot.getValue<Int>()
                    viewModel.setPlayer2Score(score!!)
                }
            }
        )

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer {
            if (it){
                val action = GameFragmentDirections.actionGameFragmentToFinishFragment(
                    viewModel.code.value!!
                )
                findNavController().navigate(action)
            }
        })

        viewModel.disabledStartButton.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.startButton.isEnabled = false
                binding.skipButton.isEnabled = true
                binding.correctButton.isEnabled = true
            }
        })

        viewModel.player1Score.observe(viewLifecycleOwner, Observer {
            if (GameFragmentArgs.fromBundle(requireArguments()).isCreator)
                binding.scoreText.text = it.toString()
        })

        viewModel.player2Score.observe(viewLifecycleOwner, Observer {
            if (GameFragmentArgs.fromBundle(requireArguments()).isJoiner)
                binding.scoreText.text = it.toString()
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = GameFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "REmoved")
        if (GameFragmentArgs.fromBundle(requireArguments()).isJoiner){
            viewModel.detachListener()
        }
        database.child(viewModel.code.value!!).child("gameFinished").removeEventListener(gameFinishListener)
        database.child(viewModel.code.value!!).removeEventListener(playerTurnListener)
    }
}