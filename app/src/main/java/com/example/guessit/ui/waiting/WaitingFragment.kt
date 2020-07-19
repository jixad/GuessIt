package com.example.guessit.ui.waiting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.guessit.databinding.FragmentWaitingBinding
import com.google.firebase.database.*
import com.example.guessit.R


/**
 * A simple [Fragment] subclass.
 * Use the [WaitingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingFragment : Fragment() {

    private lateinit var viewModel: WaitingViewModel
    private lateinit var viewModelFactory: WaitingViewModelFactory
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.deleteGame()
            findNavController().navigate(WaitingFragmentDirections.actionWaitingFragmentToMainFragment())
        }
        callback.isEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentWaitingBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_waiting,
            container,
            false
        )

        database = FirebaseDatabase.getInstance().reference.child("games")

        viewModelFactory = WaitingViewModelFactory(WaitingFragmentArgs.fromBundle(requireArguments()).player1)
        viewModel =ViewModelProviders.of(this, viewModelFactory).get(WaitingViewModel::class.java)

        binding.viewModel = viewModel

        val loadingImage: ImageView = binding.imageView
        Glide.with(this).load(R.drawable.loading_gif).into(loadingImage)

        val player2Listener = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                database.child("games").removeEventListener(this)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value!=null){
                    val action = WaitingFragmentDirections.actionWaitingFragmentToGameFragment(viewModel.code.value)
                    action.isJoiner = false
                    action.isCreator = true
                    findNavController().navigate(action)
                }
            }
        }

        database.child(viewModel.code.value!!).child("player2").addValueEventListener(
            player2Listener
        )

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}