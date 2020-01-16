package com.app.core.listener

import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

interface OnFragmentInteractionListener {

    fun getNavController(): NavController?

    fun getBottomNavView(): BottomNavigationView?

}