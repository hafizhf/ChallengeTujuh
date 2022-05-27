package andlima.hafizhfy.challengetujuh.di
//
//import andlima.hafizhfy.challengetujuh.api.film.FilmApi
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Named
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object UserModule {
//    const val USER_BASE_URL = "https://6254434789f28cf72b5aed6a.mockapi.io/"
//
//    private  val logging : HttpLoggingInterceptor
//        get(){
//            val httpLoggingInterceptor = HttpLoggingInterceptor()
//            return httpLoggingInterceptor.apply {
//                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            }
//        }
//
//    private val client = OkHttpClient.Builder().addInterceptor(logging).build()
//
//    @Provides
//    @Singleton
//    @Named("user")
//    fun provideUserRetrofit(): Retrofit =
//        Retrofit.Builder()
//            .baseUrl(AppModule.USER_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(UserModule.client)
//            .build()
//
//    @Provides
//    @Singleton
//    @Named("user")
//    fun provideUserApi(retrofit: Retrofit): FilmApi =
//        retrofit.create(FilmApi::class.java)
//}