package com.example.youtubesearch

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubesearch.adapter.VideoAdapter
import com.example.youtubesearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = VideoAdapter()

    private val sortOptions = listOf("Relevance", "Date", "Rating", "View Count")
    private val sortValues = listOf("relevance", "date", "rating", "viewCount")
    private var selectedSort = "relevance"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSortSpinner()
        setupSearchButton()
        setupKeyboardSearch()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSortSpinner() {
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSort.adapter = spinnerAdapter

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSort = sortValues[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSearchButton() {
        binding.btnSearch.setOnClickListener {
            triggerSearch()
        }
    }

    private fun setupKeyboardSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                triggerSearch()
                true
            } else {
                false
            }
        }
    }

    private fun triggerSearch() {
        val query = binding.etSearch.text.toString()
        hideKeyboard()
        viewModel.search(query, selectedSort)
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is SearchState.Idle -> {
                    showIdle()
                }
                is SearchState.Loading -> {
                    showLoading()
                }
                is SearchState.Success -> {
                    showResults(state.videos.size)
                    adapter.submitList(state.videos)
                }
                is SearchState.Error -> {
                    showError(state.message)
                }
            }
        }
    }

    private fun showIdle() {
        binding.progressBar.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvResultCount.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvResultCount.visibility = View.GONE
    }

    private fun showResults(count: Int) {
        binding.progressBar.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.tvResultCount.visibility = View.VISIBLE
        binding.tvResultCount.text = "Found $count results"
    }

    private fun showError(errorCode: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvResultCount.visibility = View.GONE
        binding.tvError.visibility = View.VISIBLE
        adapter.submitList(emptyList())

        binding.tvError.text = when (errorCode) {
            "EMPTY_INPUT" -> "Please enter a search term."
            "NO_RESULTS" -> "No videos found. Try a different search."
            "NETWORK_ERROR" -> "Network error. Please check your connection."
            "INVALID_KEY" -> "API key error. Please check your key."
            "SERVER_ERROR" -> "YouTube server error. Please try again later."
            else -> "Something went wrong. Please try again."
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}
