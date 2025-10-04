package com.example.prediai.core.di

import android.content.Context
import android.content.SharedPreferences
//import com.example.prediai.data.repository.AuthRepositoryImpl
import com.example.prediai.data.repository.ChatRepositoryImpl
//import com.example.prediai.data.repository.QuestionnaireRepositoryImpl
//import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.ChatRepository
//import com.example.prediai.domain.repository.QuestionnaireRepository
import com.google.ai.client.generativeai.GenerativeModel
//import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.prediai.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    @Provides
    @Singleton
    fun provideChatRepository(generativeModel: GenerativeModel): ChatRepository {
        return ChatRepositoryImpl(generativeModel)
    }

//    @Provides
//    @Singleton
//    fun provideFirebaseAuth(): FirebaseAuth {
//        return FirebaseAuth.getInstance()
//    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

//    @Provides
//    @Singleton
//    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
//        return AuthRepositoryImpl(firebaseAuth)
//    }

//    @Provides
//    @Singleton
//    fun provideQuestionnaireRepository(sharedPreferences: SharedPreferences): QuestionnaireRepository {
//        return QuestionnaireRepositoryImpl(sharedPreferences)
//    }
}