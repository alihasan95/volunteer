package com.teaml.iq.volunteer.ui.signin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.teaml.iq.volunteer.R


/**
 * A simple [Fragment] subclass.
 */
class SigninFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

}// Required empty public constructor
