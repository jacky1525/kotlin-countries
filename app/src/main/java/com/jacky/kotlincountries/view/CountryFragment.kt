package com.jacky.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jacky.kotlincountries.R
import com.jacky.kotlincountries.databinding.FragmentCountryBinding
import com.jacky.kotlincountries.databinding.FragmentFeedBinding
import com.jacky.kotlincountries.util.getImageFromUrl
import com.jacky.kotlincountries.util.placeholderProgressBar
import com.jacky.kotlincountries.viewmodel.CountryViewModel


class CountryFragment : Fragment() {

    private var fragmentBinding: FragmentCountryBinding? = null

    private lateinit var viewModel: CountryViewModel


    private var countryUuid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCountryBinding.bind(view)
        fragmentBinding = binding
        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }
        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)



        observerLiveData()
    }

    private fun observerLiveData() {

        val binding = fragmentBinding ?: return

        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                binding.countryName.text = country.countryName
                binding.countryCapital.text = country.countryCapital
                binding.countryCurrency.text = country.countryCurrency
                binding.countryLanguage.text = country.countryLanguage
                binding.countryRegion.text = country.countryRegion
                context?.let {
                    binding.countryImage.getImageFromUrl(country.imageUrl, placeholderProgressBar(it))
                }

            }

        })
    }


}