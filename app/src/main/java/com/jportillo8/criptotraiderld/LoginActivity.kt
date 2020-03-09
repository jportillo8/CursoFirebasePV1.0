package com.jportillo8.criptotraiderld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jportillo8.criptotraiderld.model.User
import com.jportillo8.criptotraiderld.network.Callback
import com.jportillo8.criptotraiderld.network.FirestoreService
import com.jportillo8.criptotraiderld.network.USER_COLLECTION_NAME
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author Santiago Carrillo
 * github sancarbar
 * 1/29/19.
 */


const val USERNAME_KEY = "username_key"

class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val TAG = "LoginActivity"

    //5.Creando una referencia del servicio creado en esta App
    lateinit var firestoresService: FirestoreService
    //....
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //5.Instanciando
        firestoresService = FirestoreService(FirebaseFirestore.getInstance())
        //....
    }


    fun onStartClicked(view: View) {
        //5. El usuario no puede oprimir el botom varias veces
        view.isEnabled = false
        //5.
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful){

                val username = username.text.toString()
                //7. Lectura de Datos
                firestoresService.findUserById(username, object : Callback<User> {
                    override fun onSucess(result: User?) {
                        if (result == null) {
                            val user = User()
                            user.username = username
                            saveUserAndStartMainActivity(user, view)
                        }else
                            startMainActivity(username)
                    }

                    override fun onFailed(exception: Exception) {
                        showErrorMessage(view)
                    }

                })//7.0

                //5.Modificacion para la escritura en la base de Datos
                /*val user = User()
                user.username = username
                saveUserAndStartMainActivity(user, view)*/
            }else{
                showErrorMessage(view)
                view.isEnabled = true
                //5.
            }
        }

    }

    //5. Funcion Para guardar el documento en firestore
    private fun saveUserAndStartMainActivity(user: User, view: View) {
        firestoresService.setDocument(user, USER_COLLECTION_NAME, user.username, object : Callback<Void>{
            override fun onSucess(result: Void?) {
                startMainActivity(user.username)
                Log.i(TAG,user.username)
            }

            override fun onFailed(exception: Exception) {
                showErrorMessage(view)
                //Control de Errores
                Log.e(TAG,"soy tu error", exception)
                view.isEnabled = true
                //...C.errores
            }
        })
    }
    //5.

    private fun showErrorMessage(view: View) {
        Snackbar.make(view, getString(R.string.error_while_connecting_to_the_server), Snackbar.LENGTH_LONG)
            .setAction("Info", null).show()
    }

    private fun startMainActivity(username: String) {
        val intent = Intent(this@LoginActivity, TraderActivity::class.java)
        intent.putExtra(USERNAME_KEY, username)
        startActivity(intent)
        finish()
    }

}
