package com.sezer.kirpitci.collection.di

import com.sezer.kirpitci.collection.ui.features.MainActivity
import com.sezer.kirpitci.collection.ui.features.admin.addcard.AdminAddCardFragment
import com.sezer.kirpitci.collection.ui.features.admin.addcard.AdminAddCardModule
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.AdminViewCardFragment
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.AdminViewCardModule
import com.sezer.kirpitci.collection.ui.features.admin.viewusers.AdminViewUsersModule
import com.sezer.kirpitci.collection.ui.features.admin.viewusers.ViewUsersFragment
import com.sezer.kirpitci.collection.ui.features.login.LoginFragment
import com.sezer.kirpitci.collection.ui.features.login.LoginModule
import com.sezer.kirpitci.collection.ui.features.registration.RegistirationModule
import com.sezer.kirpitci.collection.ui.features.registration.RegistrationFragment
import com.sezer.kirpitci.collection.ui.features.splash.SplashModule
import com.sezer.kirpitci.collection.ui.features.splash.SplashScreenFragment
import com.sezer.kirpitci.collection.ui.features.user.home.UserFragment
import com.sezer.kirpitci.collection.ui.features.user.home.UserHomeModule
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.BeerFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.BeerModule
import com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis.GeneralAnalysisFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis.GeneralAnalysisModule
import com.sezer.kirpitci.collection.ui.features.user.ui.home.HomeModule
import com.sezer.kirpitci.collection.ui.features.user.ui.home.HomePageFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.analytics.AnalysticsModule
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.analytics.PersonalAnalytics
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo.PersonalInfo
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo.PersonalInfoModule
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings.PersonalSettingsModule
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        FactoryModule::class,
        FireBaseModule::class,
        NetworkModule::class,
        AdminAddCardModule::class,
        AdminViewCardModule::class,
        AdminViewUsersModule::class,
        LoginModule::class,
        RegistirationModule::class,
        SplashModule::class,
        UserHomeModule::class,
        BeerModule::class,
        HomeModule::class,
        PersonalInfoModule::class,
        AnalysticsModule::class,
        PersonalSettingsModule::class,
        GeneralAnalysisModule::class
    ]
)
interface AppComponent {
    fun inject(loginFragment: LoginFragment)
    fun inject(adminAddCardFragment: AdminAddCardFragment)
    fun inject(adminViewCardFragment: AdminViewCardFragment)
    fun inject(viewUsersFragment: ViewUsersFragment)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(splashScreenFragment: SplashScreenFragment)
    fun inject(userFragment: UserFragment)
    fun inject(beerFragment: BeerFragment)
    fun inject(homePageFragment: HomePageFragment)
    fun inject(personalInfo: PersonalInfo)
    fun inject(personalAnalytics: PersonalAnalytics)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(generalAnalysisFragment: GeneralAnalysisFragment)
}
