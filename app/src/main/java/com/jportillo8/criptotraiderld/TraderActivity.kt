package com.jportillo8.criptotraiderld

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.jportillo8.criptotraiderld.adapter.CryptoAdapterListener
import com.jportillo8.criptotraiderld.adapter.CryptosAdapter
import com.jportillo8.criptotraiderld.model.Crypto
import com.jportillo8.criptotraiderld.model.User
import com.jportillo8.criptotraiderld.network.Callback
import com.jportillo8.criptotraiderld.network.FirestoreService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_trader.*


/**
 * @author Santiago Carrillo
 * 2/14/19.
 */
class TraderActivity : AppCompatActivity(), CryptoAdapterListener {

    //14.Deficion de una instancia
    lateinit var firestoreService: FirestoreService

    //La clase hace que debera implementar la interfaz del cryptos adapter listener
    private val cryptosAdapter: CryptosAdapter = CryptosAdapter(this)
    //14....

    //15.variable para recibir lo que nos manda el login
    private var username: String? = null

    private var user: User? = null
    //15.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)

        //15.variable para recibir lo que nos manda el login
        username = intent.extras!![USERNAME_KEY]!!.toString()
        usernameTextView.text = username
        //15. Aqui ya tendriadmos el username

        //14.Obteniedo servicio y despues las demas funciones
        firestoreService = FirestoreService(FirebaseFirestore.getInstance())
        configureRecyclerView()
        loadCryptos()
        //14....

        fab.setOnClickListener { view ->
            Snackbar.make(view, "generating new cryptos", Snackbar.LENGTH_SHORT)
                .setAction("info",null).show()
        }


    }

    //14.
    private fun loadCryptos() {
        firestoreService.getCryptos(object: Callback<List<Crypto>>{
            override fun onSucess(cryptoList: List<Crypto>?) {

                //15. Cargando la lista referente a ese usuario
                firestoreService.findUserById(username!!, object: Callback<User>{
                    override fun onSucess(result: User?) {
                        user = result
                        //verificamos si ese usuario tiene lista de criptomonedas
                        if (user!!.cryptosList == null){
                            val userCryptolist = mutableListOf<Crypto>()

                            for (crypto in cryptoList!!){
                                val cryptoUser = Crypto()
                                cryptoUser.name = crypto.name
                                cryptoUser.available = crypto.available
                                cryptoUser.imageUrl = crypto.imageUrl
                                userCryptolist.add(cryptoUser)
                            }
                            //Actualizemos este user en la BD
                            user!!.cryptosList = userCryptolist
                            firestoreService.updateUser(user!!, null)
                        }
                        //Para que cargue la lista en el panel de informacion
                        loadUserCriptos()
                    }

                    override fun onFailed(exception: java.lang.Exception) {
                        showGeneralServerErrorMessage()
                    }

                })
                //15.........^^^^^^

                this@TraderActivity.runOnUiThread{
                    cryptosAdapter.cryptoList = cryptoList!!
                    cryptosAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailed(exception: Exception) {
                Log.e("Traider Activity", "erro loading criptos", exception)
                showGeneralServerErrorMessage()
            }

        })
    }

    //15 Funcion Para que cargue la lista en el panel de informacion
    private fun loadUserCriptos() {
        runOnUiThread {
            //verificamos si es usuario nos es nulo y la lista tampoco sea nula
            if (user != null && user!!.cryptosList != null){
                //vamos a limpiar la vista
                infoPanel.removeAllViews()
                for (crypto in user!!.cryptosList!!){
                    addUserCryptoInfoRow(crypto)
                }
            }
        }
    }

    private fun addUserCryptoInfoRow(crypto: Crypto) {
        val  view = LayoutInflater.from(this).inflate(R.layout.coin_info, infoPanel,false)
        view.findViewById<TextView>(R.id.coinLabel).text = getString(R.string.coin_info, crypto.name, crypto.available.toString())
        Picasso.get().load(crypto.imageUrl).into(view.findViewById<ImageView>(R.id.coinIcon))
        infoPanel.addView(view)
    }
    //15^^

    //
    private fun configureRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = cryptosAdapter
    }

    fun showGeneralServerErrorMessage() {
        Snackbar.make(fab, "Error while connecting to the server", Snackbar.LENGTH_SHORT)
            .setAction("info", null).show()
    }

    //16. Entra aqui cuando le de click a la compra de cripto monedas
    override fun onBuyCryptoClicked(crypto: Crypto) {
        if (crypto.available > 0){
            for (userCrypto in user!!.cryptosList!!){
                if (userCrypto.name == crypto.name){
                    userCrypto.available += 1
                    break
                }
            }
            crypto.available--
            firestoreService.updateUser(user!!, null)
            firestoreService.updateCrypto(crypto)
        }
    }
//16....

}