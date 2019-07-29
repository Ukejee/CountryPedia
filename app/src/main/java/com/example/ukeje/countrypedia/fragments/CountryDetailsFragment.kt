package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.ukeje.countrypedia.databinding.FragmentCountryDetailsBinding
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import java.util.*


class CountryDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentCountryDetailsBinding
    private lateinit var viewModel: SharedFragmentViewModel

    override val fragmentTag: String
        get() = COUNTRY_DETAILS_FRAGMENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountryDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //initialize view model
        viewModel = ViewModelProviders.of(mainActivity).get(SharedFragmentViewModel::class.java)
        viewModel.countryPediaRepository = Objects.requireNonNull(mainActivity).countryPediaRepository
        binding.viewModel = viewModel

        initView()
    }

    private fun initView() {
        AppUtils.debug("country details: ${viewModel.countryDetails}")
        viewModel.splitList(viewModel.countryDetails?.timezones as List<String>)
        viewModel.formattedLanguages = viewModel.formatLanguageResponse(viewModel.countryDetails?.languages)
        viewModel.formattedAltSpelling = viewModel.formatList(viewModel.countryDetails?.altSpellings as List<String>, ", ")
        //empty
    }


    //    public void init() {
    //        updateUI(sharedFragmentViewModel.countryDetails);
    //
    ////        countryPediaRepository = new CountryPediaRepository(getActivity());
    //
    //        binding.backBtn.setClickable(true);
    //        binding.backBtn.setOnClickListener(v -> {
    ////                    onButtonPressed("back");
    //        });
    //
    //        binding.favoriteBtn.setClickable(true);
    //        if (!sharedFragmentViewModel.getFavoriteCountries().contains(binding.countryName.getText())) {
    //            binding.favoriteBtn.setTag(R.drawable.favorite_icon_unselected);
    //            binding.favoriteBtn.setImageResource(R.drawable.favorite_icon_unselected);
    //        } else {
    //            binding.favoriteBtn.setImageResource(R.drawable.favorite_two);
    //            binding.favoriteBtn.setTag(R.drawable.favorite_two);
    //        }
    //
    //        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                if ((int) binding.favoriteBtn.getTag() == R.drawable.favorite_icon_unselected) {
    //                    binding.favoriteBtn.setImageResource(R.drawable.favorite_two);
    //                    binding.favoriteBtn.setTag(R.drawable.favorite_two);
    //                    sharedFragmentViewModel.favoriteCountries.add(binding.countryName.getText().toString());
    ////                    setCountryId();
    //                } else {
    //                    binding.favoriteBtn.setImageResource(R.drawable.favorite_icon_unselected);
    //                    binding.favoriteBtn.setTag(R.drawable.favorite_icon_unselected);
    //                    sharedFragmentViewModel.favoriteCountries.remove(binding.countryName.getText().toString());
    ////                    setCountryId();
    //                }
    //
    //            }
    //        });
    //
    //    }

    //DISPLAY THE RESULTS FROM THE API TO THE UI
    //    public void updateUI(CountryResponse cr) {
    //
    //        binding.countryName.setText(cr.getName());
    //        binding.capitalName.setText("It's capital is " + cr.getCapital());
    //        binding.subregionName.setText(cr.getSubregion());
    //        binding.regionName.setText(cr.getRegion());
    //        binding.countryCode.setText("+" + cr.getCallingCodes().get(0));
    //        binding.currencyCode.setText(cr.getCurrencies().get(0).getCode());
    //        binding.currencyName.setText(cr.getCurrencies().get(0).getName());
    //        binding.countryPopulation.setText(cr.getPopulation().toString());
    //        binding.countryLanguage.setText(cr.getLanguages().get(0).getName());
    //
    //        if (!cr.getLanguages().isEmpty()) {
    //
    //            for (int i = 1; i < cr.getLanguages().size(); i++) {
    //                binding.countryLanguage.append(", ");
    //                binding.countryLanguage.append(cr.getLanguages().get(i).getName());
    //            }
    //        }
    //
    //
    //        if (!cr.getAltSpellings().isEmpty()) {
    //            binding.countryAltSpelling.setText(cr.getAltSpellings().get(0));
    //        }
    //
    //        for (int i = 1; i < cr.getAltSpellings().size(); i++) {
    //            binding.countryAltSpelling.append(", ");
    //            binding.countryAltSpelling.append(cr.getAltSpellings().get(i));
    //            if (i > cr.getAltSpellings().size()) {
    //                binding.countryLanguage.append(", ");
    //            }
    //        }
    //
    //        if (cr.getLatlng().size() >= 2) {
    //            binding.countryCoordinates.setText(String.format("%s(Lat)%s(Long)", cr.getLatlng().get(0).toString(), cr.getLatlng().get(1).toString()));
    //
    //        }
    //
    //        //THERE'S A BUG HERE; USE SWITCH STATEMENTS TO FIX
    //        binding.countryTimeZones.setText(cr.getTimezones().get(0));
    //
    //
    //        if (cr.getTimezones().size() <= 6) {
    //            binding.countryTimeZones.setGravity(Gravity.CENTER);
    //            binding.timezoneSepartingLine.setVisibility(View.GONE);
    //            for (int i = 1; i < cr.getTimezones().size(); i++) {
    //                binding.countryTimeZones.append("\n");
    //                binding.countryTimeZones.append(cr.getTimezones().get(i));
    //            }
    //        }
    //
    //        if (cr.getTimezones().size() > 6) {
    //            for (int i = 1; i < 6; i++) {
    //                binding.countryTimeZones.append("\n");
    //                binding.countryTimeZones.append(cr.getTimezones().get(i));
    //            }
    //            for (int i = 6; i < cr.getTimezones().size(); i++) {
    //                binding.countryTimeZoneTwo.append(cr.getTimezones().get(i));
    //                binding.countryTimeZoneTwo.append("\n");
    //            }
    //        }
    //
    //
    //    }

    //THIS METHOD UPDATES THE VALUE OF THE FAVORITE COLUMN IN THE DATABASE
    //    public void setFavoriteCountry() {
    /*new AsyncTask<Void, Void, Country>() {
            @Override
            protected Country doInBackground(Void...voids) {
                return countryRepository.getCountryDetailsFromApi(countryDbId);
            }

            @Override
            protected void onPostExecute(Country country) {
                favoriteCountry = country;

                if((int)binding.favoriteBtn.getTag() == R.drawable.favorite_two){
                    favoriteCountry.setFavorite(true);
                    countryRepository.updateCountry(favoriteCountry);
                    getAppUtils().showMessage("Country Added to favorite");
                }
                if((int)binding.favoriteBtn.getTag() == R.drawable.favorite_icon_unselected){
                    favoriteCountry.setFavorite(false);
                    countryRepository.updateCountry(favoriteCountry);
                    getAppUtils().showMessage("Country Has Been Removed From Favorites");

                }

            }
        }.execute();
*/

    //    }

    //    public void setCountryId() {
    //
    //        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
    //
    //            @Override
    //            protected Integer doInBackground(Void... voids) {
    //                return countryRepository.getCountryId(binding.countryName.getText().toString());
    //            }
    //
    //            @Override
    //            protected void onPostExecute(Integer countryId) {
    //                countryDbId = countryId.intValue();
    //                setFavoriteCountry();
    //            }
    //
    //        };
    //
    //        task.execute();
    //    }
}// Required empty public constructor
