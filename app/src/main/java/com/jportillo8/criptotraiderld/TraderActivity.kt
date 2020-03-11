package com.jportillo8.criptotraiderld

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.jportillo8.criptotraiderld.adapter.CryptoAdapterListener
import com.jportillo8.criptotraiderld.adapter.CryptosAdapter
import com.jportillo8.criptotraiderld.model.Crypto
import com.jportillo8.criptotraiderld.network.Callback
import com.jportillo8.criptotraiderld.network.FirestoreService
import kotlinx.android.synthetic.main.activity_trader.*
import java.lang.Exception


/**
 * @author Santiago Carrillo
 * 2/14/19.
 */
class TraderActivity : AppCompatActivity(), CryptoAdapterListener {

    //14.
    lateinit var firestoreService: FirestoreService
    private val cryptosAdapter: CryptosAdapter = CryptosAdapter(this)
    //14....

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)

        //14.
        firestoreService = FirestoreService(FirebaseFirestore.getInstance())
        configureRecyclerView()
        loadCryptos()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "generating new cryptos", Snackbar.LENGTH_SHORT)
                .setAction("info",null).show()
        }

        fun showGeneralServerErrorMessage(){
            Snackbar.make(fab, "Error while connecting to the server", Snackbar.LENGTH_SHORT)
                .setAction("info",null).show()
        }
    }

    //14.
    private fun loadCryptos() {
        firestoreService.getCryptos(object: Callback<List<Crypto>>{
            override fun onSucess(result: List<Crypto>?) {
                this@TraderActivity.runOnUiThread{
                    cryptosAdapter.cryptoList = result!!
                    cryptosAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailed(exception: Exception) {
                TODO("Not yet implemented")
            }

        })
    }
//
    private fun configureRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = cryptosAdapter
    }

    //14. Entra aqui cuando le de click a la compra de cripto monedas
    override fun onBuyCryptoClicked(crypto: Crypto) {
        TODO("Not yet implemented")
    }
//14....

}