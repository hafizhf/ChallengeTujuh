package andlima.hafizhfy.challengetujuh.repository
//
//import andlima.hafizhfy.challengetujuh.api.user.UserApiHelper
//import andlima.hafizhfy.challengetujuh.model.user.PutUser
//import andlima.hafizhfy.challengetujuh.model.user.RequestUser
//import javax.inject.Inject
//
//class UserRepository @Inject constructor(private val apiHelper: UserApiHelper) {
//    suspend fun getUser(email: String) = apiHelper.getUser(email)
//    suspend fun postUser(requestUser: RequestUser) = apiHelper.postUser(requestUser)
//    suspend fun updateUser(id: Int, putUser: PutUser) = apiHelper.updateUser(id, putUser)
//}