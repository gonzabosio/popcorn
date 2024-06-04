package com.popcorn.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.popcorn.MoviesViewModel
import com.popcorn.fire.AuthProcess
import com.popcorn.ui.auth.RegisterScreen
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
@InstallIn(SingletonComponent::class)
object MoviesModule {
    @Provides
    fun provideFBAuth (): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    fun provideContext (@ApplicationContext context: Context): Context {
        return context
    }
    @Provides
    fun provideRegisterScreen (authProcess: AuthProcess): RegisterScreen {
        return RegisterScreen(authProcess)
    }
}