package com.example.guessit.ui.waiting

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.guessit.R
import com.example.guessit.databinding.FragmentWaitingBinding
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 * Use the [WaitingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingFragment : Fragment() {

    private lateinit var viewModel: WaitingViewModel
    private lateinit var viewModelFactory: WaitingViewModelFactory
    private lateinit var database: DatabaseReference
    private lateinit var childEventListener: ChildEventListener

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

        childEventListener = object: ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val action = WaitingFragmentDirections.actionWaitingFragmentToGameFragment(viewModel.code.value)
                action.isJoiner = false
                action.isCreator = true
                findNavController().navigate(action)
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        }

        database.child(viewModel.code.value!!).addChildEventListener(childEventListener)



        return binding.root
    }

    override fun onStop(){
        super.onStop()
        database.child(viewModel.code.value!!).removeEventListener(childEventListener)
        //viewModel.deleteRoom()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        database.child(viewModel.code.value!!).removeEventListener(childEventListener)
//        viewModel.deleteRoom()
//    }

    companion object {
        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}