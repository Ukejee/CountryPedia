package com.example.ukeje.countrypedia.database

import android.os.Handler
import com.example.ukeje.countrypedia.utils.AppUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-23 10:46
 */
class Sample (private val countryPediaDatabase: CountryPediaDatabase) {

    fun db(){
        val compositeDisposable = CompositeDisposable()

        //sample country
        val sampleCountry = Country("Togo", "Lome", "228")
        val sampleCountry2 = Country("Nigeria", "Abuja", "224")


        compositeDisposable.add(countryPediaDatabase.countryDao().insertCountry(sampleCountry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    AppUtils.debug("country inserted")
                }, { error -> AppUtils.error("error on country inserted: $error") }))


        compositeDisposable.add(countryPediaDatabase.countryDao().insertCountry(sampleCountry2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    AppUtils.debug("country inserted2")
                }, { error -> AppUtils.error("error on country inserted: $error") }))

        //test query
        compositeDisposable.add(countryPediaDatabase.countryDao().fetchAllCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    AppUtils.info("fetchAllCountries. all the countries: ${it.size}; first item: $it")
                }, { error -> AppUtils.error("error on fetchAllCountries: $error") }))


        //test query by name
        compositeDisposable.add(countryPediaDatabase.countryDao().fetchCountryByName(sampleCountry.name!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    AppUtils.info("fetchCountryByName, country: $it")
                }, { error -> AppUtils.error("error on fetchAllCountries: $error") }))


        AppUtils.debug("numeric codel: ${sampleCountry.numericCode}")
        //test query by Id
        compositeDisposable.add(countryPediaDatabase.countryDao().fetchCountryByNumericCode(sampleCountry.numericCode!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    AppUtils.info("fetchCountryByNumericCode, country: $it")
                }, { error -> AppUtils.error("error on fetchAllCountries: $error") }))

        //test update
        Handler().postDelayed({

            //change a value in sampleCountry
            sampleCountry.capital = "Japan"

            compositeDisposable.add(countryPediaDatabase.countryDao().insertCountry(sampleCountry)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        AppUtils.debug("country updated")
                    }, { error -> AppUtils.error("error on country inserted: $error") }))
        }, 5000)

        //test delete
        Handler().postDelayed({
            compositeDisposable.add(countryPediaDatabase.countryDao().deleteCountry(sampleCountry)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                AppUtils.debug("country deleted")
                            },
                            { error -> AppUtils.error("error on country inserted: $error") }))
        }, 10000)


        compositeDisposable.add(countryPediaDatabase.countryDao().fetchAllCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            AppUtils.info("fetchAllCountries-2. all the countries: ${it.size}; first item: ${it}")
                        },
                        { error -> AppUtils.error("error on fetchAllCountries: $error") }))

        AppUtils.info("########################################################################################")



        //sample favourite
        val favouriteCountry = Favourite.buildFavouriteWithCountry(sampleCountry)

        //test insert for Favourite Table
        compositeDisposable.add(countryPediaDatabase.favoriteDao().insertFavourite(favouriteCountry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            AppUtils.info("favourite country inserted")
                        },
                        { error -> AppUtils.error("error on favourite country inserted: $error") }))

        compositeDisposable.add(countryPediaDatabase.favoriteDao().fetchAllFavourites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            AppUtils.debug("fetch all favourite countries: ${it.size}, $it")
                        },
                        {
                            error ->
                            AppUtils.error("error on fetch all favourite countries: $error")
                        }
                ))


        Handler().postDelayed({

//            favouriteCountry.name = "Togooo"

            compositeDisposable.add(countryPediaDatabase.favoriteDao().insertFavourite(favouriteCountry)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                AppUtils.info("favourite country inserted")
                            },
                            { error -> AppUtils.error("error on favourite country inserted: $error") }))

        }, 5000)

        compositeDisposable.add(countryPediaDatabase.favoriteDao().deleteFavourite(favouriteCountry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            AppUtils.debug("delete favourite country")
                        },
                        { error -> AppUtils.error("error on delete favourite country: $error")}
                ))
    }
}