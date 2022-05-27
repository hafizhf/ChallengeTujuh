package andlima.hafizhfy.challengetujuh.func

import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.di.UserClient
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.model.user.GetUserItem
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream

// Function to easy making Toast -------------------------------------------------------------------
fun toast(context: Context, message : String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarLong(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Funtion to easy making Snackbar with custom action ----------------------------------------------
fun snackbarCustom(view: View, message: String, buttonText: String, action: Any.() -> Unit) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction(buttonText) {
        action(true)
    }
    snack.show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarIndefinite(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Function to easy making AlertDialog -------------------------------------------------------------
fun alertDialog(context: Context, title: String, message: String, action: Any.()->Unit) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            action(true)
        }
        .setCancelable(false)
        .show()
}

// Function to hide all error pop up ---------------------------------------------------------------
fun hideAllPopUp(cardView1: CardView, cardView2: CardView) {
    cardView1.visibility = View.GONE
    cardView2.visibility = View.GONE
}

// Function to show error pop up -------------------------------------------------------------------
fun showPopUp(cardViewID: CardView, textViewID: TextView, message: String) {
    cardViewID.visibility = View.VISIBLE
    textViewID.text = message
}

// Function to hide error pop up -------------------------------------------------------------------
fun hidePopUp(cardViewID: CardView) {
    cardViewID.visibility = View.GONE
}

// Function to show password on password EditText --------------------------------------------------
fun showPassword(editText: EditText, imageView: ImageView) {
    val hidden = PasswordTransformationMethod.getInstance()
    val show = HideReturnsTransformationMethod.getInstance()

    if (editText.transformationMethod == hidden) {
        editText.transformationMethod = show
        imageView.setImageResource(R.drawable.ic_eye_off)
    } else {
        editText.transformationMethod = hidden
        imageView.setImageResource(R.drawable.ic_eye)
    }
}

// IMAGE CONVERT METHOD ############################################################################

private fun convertImageToByteArray(imageView: ImageView): ByteArray {
    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

    return stream.toByteArray()
}

fun encodeImageBase64(imageView: ImageView): String {
    return Base64.encodeToString(convertImageToByteArray(imageView), Base64.NO_WRAP)
}

fun decodeBase64Image(base64String: String): Bitmap {
    val imageBytes = Base64.decode(base64String, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

// END OF IMAGE CONVERT METHOD #####################################################################

fun matchUserLocalAndDatabase(context: Context, lifecycleOwner: LifecycleOwner, action: (result: Boolean, response: List<GetUserItem>, message: String) -> Unit) {
    val userManager = UserManager(context)

    userManager.email.asLiveData().observe(lifecycleOwner, { email ->
        userManager.password.asLiveData().observe(lifecycleOwner, { password ->

            if (email != "" && password != "") {
                UserClient.instance.getUser(email)
                    .enqueue(object : retrofit2.Callback<List<GetUserItem>>{
                        override fun onResponse(
                            call: Call<List<GetUserItem>>,
                            response: Response<List<GetUserItem>>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.isEmpty()) {
                                    action(true, emptyList(), "Account banned")
                                } else {
                                    if ( response.body()!![0].email != email || response.body()!![0].password != password) {
                                        action(true, emptyList(), "User data changed, please re-login")
                                    } else {

                                        action(true, response.body()!!, "")
                                    }
                                }
                            }
                        }
                        override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                            action(false, emptyList(), "Connection error")
                        }
                    })
            }
            else {
                action(false, emptyList(), "")
            }
        })
    })
}

fun updateUserDataRealTime(context: Context, isDifferent: Boolean, response: List<GetUserItem>, message: String): Boolean {
    val userManager = UserManager(context)

    if (isDifferent) {
        if (response.isNotEmpty()) {
            GlobalScope.launch {
                userManager.updateCurrentUserRealTimeData(
                    response[0].username,
                    response[0].avatar,
                    response[0].complete_name,
                    response[0].address,
                    response[0].dateofbirth
                )
            }
            return false
        } else {
            GlobalScope.launch { userManager.clearData() }
            toast(context, message)
            return true
        }
    } else {
        if (message != "") {
            alertDialog(context, message, "Restart app or try again in few minutes") { }
            return false
        } else {
            return false
        }
    }
}