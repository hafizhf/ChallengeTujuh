package andlima.hafizhfy.challengetujuh.viewmodel

//import andlima.hafizhfy.challengetujuh.model.user.GetUserItem
//import andlima.hafizhfy.challengetujuh.repository.UserRepository
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class LoginViewModel @Inject constructor(api: UserRepository): ViewModel() {
//
//    @Inject
//    lateinit var api: UserRepository
//
//    private val userLiveData = MutableLiveData<List<GetUserItem>>()
//    lateinit var email: LiveData<String>
//
//    fun findUser(email: String) {
//        viewModelScope.launch {
//            val dataUser = api.getUser(email)
//            delay(2000)
//            userLiveData.value = dataUser
//        }
//    }
//
//    init {
//        findUser()
//    }
//
//}