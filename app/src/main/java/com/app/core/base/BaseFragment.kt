package com.app.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.app.core.R
import com.app.core.common.ErrorUtils
import com.app.core.common.Utils
import com.app.core.listener.OnFragmentInteractionListener
import com.app.core.model.Action
import com.app.core.model.NetworkState
import dagger.android.support.DaggerFragment


abstract class BaseFragment<T : BaseViewModel> : DaggerFragment(), Toolbar.OnMenuItemClickListener {

    private var toolbar: Toolbar? = null

    protected var fragmentInteractionListener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getViewModel().setup(arguments)
        setFragmentInteractionListener(getContext())
        setOnBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getFragmentView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setUi()
        observeViewModel()
    }

    override fun onDetach() {
        removeFragmentListener()
        super.onDetach()
    }

    override fun onDestroyView() {
        clear()
        super.onDestroyView()
    }


    override fun onDestroy() {
        super.onDestroy()
        getViewModel().onCleared()
    }

    protected fun checkActivityStatus(navigate: () -> Unit) {
        if (activity?.isDestroyed == false && context != null) {
            navigate.invoke()
        }
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun setupToolbar() {
        if (toolbar == null) {
            toolbar = view?.findViewById(R.id.toolbar)
            toolbar?.run {
                title = getAppBarTitle()
                if (getMenuResId() > 0) {
                    inflateMenu(getMenuResId())
                    setOnMenuItemClickListener(this@BaseFragment)
                }
                if (showNavigationBack()) {
                    navigationIcon =
                        ResourcesCompat.getDrawable(resources, R.drawable.arrow, null);
                    setNavigationOnClickListener { activity!!.onBackPressed() }
                }
            }
        }
    }

    protected open fun clear() {
    }

    protected open fun showNavigationBack(): Boolean {
        return true
    }

    /**
     *  class extending this base can override to do action on back pressed
     */
    protected open fun onBackPressed() {
        fragmentInteractionListener?.getNavController()?.navigateUp()
    }

    private fun removeFragmentListener() {
        fragmentInteractionListener = null
    }

    private fun setFragmentInteractionListener(context: Context?) {
        if (context is OnFragmentInteractionListener) {
            fragmentInteractionListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    protected abstract fun setUi()

    protected abstract fun getMenuResId(): Int

    protected abstract fun getAppBarTitle(): String

    protected abstract fun handleNetworkState(networkState: NetworkState?)

    protected abstract fun handleEvents(action: Action?)

    protected open fun observeViewModel() {
        getViewModel().action.observe(viewLifecycleOwner, Observer {
            handleAction(it)
        })
        getViewModel().networkState.observe(viewLifecycleOwner, Observer {
            handleNetworkState(it)
        })
    }

    protected fun manageUserTouch(networkState: NetworkState?) {
        Utils.setContentLoadingSettings(activity!!.window, networkState)
    }

    private fun handleAction(action: Action?) {
        if (action != null) {
            handleEvents(action)
            ErrorUtils.handleError(
                context = activity!!.applicationContext,
                sharedPreferences = getViewModel().sharedPreferences,
                serverException = action.serverException
            )
        }
    }

    protected fun navigateTo(navId: Int, extras: Bundle? = null) {
        fragmentInteractionListener?.getNavController()?.navigate(navId, extras)
    }

    protected fun navigateUp() {
        fragmentInteractionListener?.getNavController()?.navigateUp()
    }

    protected fun isAnyRequestInProgress(): Boolean {
        return getViewModel().networkState.value?.status == NetworkState.LOADING.status
    }

    protected abstract fun getFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    protected abstract fun getViewModel(): T


}