package com.silver.github.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.silver.github.R
import com.silver.github.databinding.FragmentUserListBinding
import com.silver.github.model.ApiState
import com.silver.github.viewmodel.UserListViewModel
import kotlinx.android.synthetic.main.fragment_user_list.*
import kotlinx.android.synthetic.main.view_error.*

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding

    private val viewModel by lazy {
        ViewModelProviders.of(this)[UserListViewModel::class.java]
    }

    private val adapter by lazy {
        UserListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_user_list.adapter = adapter

        iv_logo.setOnClickListener {
            viewModel.reload()
        }

        swipe_refresh_layout.setOnRefreshListener {
            viewModel.reload()
        }

        cv_retry_button.setOnClickListener {
            viewModel.retry()
        }

        viewModel.userList?.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.getApiState().observe(this, Observer {
            when (it) {
                ApiState.LOADING -> {
                    viewModel.error.value = false
                }
                ApiState.SUCCESS -> {
                    swipe_refresh_layout.isRefreshing = false
                }
                ApiState.ERROR -> {
                    swipe_refresh_layout.isRefreshing = false
                    viewModel.error.value = true
                }
            }
        })
    }
}
