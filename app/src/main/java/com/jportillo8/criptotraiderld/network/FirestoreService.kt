package com.jportillo8.criptotraiderld.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jportillo8.criptotraiderld.model.Crypto
import com.jportillo8.criptotraiderld.model.User
import java.lang.Exception

//Acceso a los nombres de la collecciones
const val CRYPTO_COLLECTION_NAME = "cryptos"
const val USER_COLLECTION_NAME = "users"

//Modulo para usar Firestore
class FirestoreService(val firebaseFirestore: FirebaseFirestore) {

    fun setDocument(data: Any, collectionName: String, id: String, callback: Callback<Void>) {

        //Implementacion Usamos el modulo nos permite guardar cualquier tipo de documento
        firebaseFirestore.collection(collectionName).document(id).set(data)
            .addOnSuccessListener { callback.onSucess(null) }
            .addOnFailureListener { exception -> callback.onFailed(exception) }
        Log.i("FirestoreService",id)
    }

    //Esta funcion nos permite actulizar el user
    fun updateUser(user: User, callback: Callback<User>?) {
        firebaseFirestore.collection(CRYPTO_COLLECTION_NAME).document(user.username)
            .update("cryptoList", user.cryptosList)
            .addOnSuccessListener { result ->
                if (callback != null)
                    callback.onSucess(user)
            }
            .addOnFailureListener { exception -> callback?.onFailed(exception) }
    }
    //Esta funcion nos permite actulizar el crypto
    fun updateCrypto(crypto: Crypto){
        firebaseFirestore.collection(CRYPTO_COLLECTION_NAME).document(crypto.getDocumentId())
            .update("available", crypto.available)
    }

    //7.0 Colsutando lista de criptomonedas, hace la lectura de las criptomonedas
    fun getCryptos(callback: Callback<List<Crypto>>?) {
        firebaseFirestore.collection(CRYPTO_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val cryptoList = result.toObjects(Crypto::class.java)
                    callback!!.onSucess(cryptoList)
                    break
                }
            }
            .addOnFailureListener { exception -> callback!!.onFailed(exception)  }
    }
    //Encontrando los usuarios dependiendo del id
    fun findUserById(id: String, callback: Callback<User>){
        firebaseFirestore.collection(USER_COLLECTION_NAME).document(id)
            .get()
            .addOnSuccessListener { result ->
                if (result.data != null){
                    callback.onSucess(result.toObject(User::class.java))
                }else{
                    callback.onSucess(null)
                }
            }
            .addOnFailureListener { exception -> callback.onFailed(exception)  }
    }
    ///7.0


}