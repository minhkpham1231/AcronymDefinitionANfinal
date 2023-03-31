package com.example.acronymdefinitionapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.acronymdefinitionapp.adapter.AcronymDefAdapter
import com.example.acronymdefinitionapp.databinding.FragmentMainBinding
import com.example.acronymdefinitionapp.model.Definition
import com.example.acronymdefinitionapp.utils.RequestState
import com.example.acronymdefinitionapp.viewmodel.AcronymViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AcronymViewModel::class.java]
    }

    private val acronymAdapter by lazy {
        AcronymDefAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.acronymViewModel = this@MainFragment.viewModel
        binding.lifecycleOwner = this@MainFragment
        binding.acronymAdapter = this@MainFragment.acronymAdapter
        binding.searchAcronym.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    viewModel.handleSearch(it)
                }
                binding.searchAcronym.clearFocus()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }
}

@BindingAdapter("setAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: AcronymDefAdapter?
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("submitList")
fun submitList(recyclerView: RecyclerView, state: RequestState?) {
    val adapter = recyclerView.adapter as? AcronymDefAdapter

    when (state) {
        is RequestState.SUCCESS<*> -> {
            val definitions = (state.definitions as List<Definition>).firstOrNull()?.lfs ?: listOf()
            adapter?.setDataDefinition(definitions)
        }
        is RequestState.ERROR -> {
            AlertDialog.Builder(recyclerView.context)
                .setTitle("Error has occurred")
                .setMessage(state.exception.localizedMessage)
                .setNegativeButton("DISMISS") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            recyclerView.visibility = View.GONE
        }
        is RequestState.LOADING -> {
            recyclerView.visibility = View.GONE
        }
        else -> {
            // no-op
        }
    }
}

@BindingAdapter("setProgressLoad")
fun setProgressLoad(bar: ProgressBar, isLoading: Boolean) {
    if (!isLoading) {
        bar.visibility = View.GONE
    } else {
        bar.visibility = View.VISIBLE
    }
}