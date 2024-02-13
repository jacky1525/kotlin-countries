package com.jacky.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jacky.kotlincountries.R
import com.jacky.kotlincountries.adapter.CountryAdapter
import com.jacky.kotlincountries.databinding.FragmentFeedBinding
import com.jacky.kotlincountries.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private var fragmentBinding: FragmentFeedBinding? = null

    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFeedBinding.bind(view)
        fragmentBinding = binding

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        binding.countryList.layoutManager = LinearLayoutManager(context)
        binding.countryList.adapter = countryAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
                binding.countryList.visibility = View.GONE
                binding.countryError.visibility = View.GONE
                binding.countryLoading.visibility = View.VISIBLE
                viewModel.refreshData()
                binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()


        /*val button = binding.fragmentButton
        button.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            Navigation.findNavController(it).navigate(action)
        }
*/

    }

    private fun observeLiveData() {
        // def binding
        val binding = fragmentBinding ?: return


        // countries
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                binding.countryList.visibility = View.VISIBLE
                countryAdapter.uptadeCountryList(countries)
            }

        })

        // error
        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    binding.countryError.visibility = View.VISIBLE
                    binding.countryLoading.visibility = View.GONE
                    binding.countryList.visibility = View.GONE
                } else {
                    binding.countryError.visibility = View.GONE

                }
            }

        })

        // loading
        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryList.visibility = View.GONE
                    binding.countryError.visibility = View.GONE

                }
                else{
                    binding.countryLoading.visibility = View.GONE
                }
            }

        })
    }

}