package com.jportillo8.criptotraiderld

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_trader.*


/**
 * @author Santiago Carrillo
 * 2/14/19.
 */
class TraderActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "generating new cryptos", Snackbar.LENGTH_SHORT )
                .setAction("Info", null).show()
        }
    }
    fun showGeneralServerErrorMessage(){
        Snackbar.make(fab, "Error while connecting to the server", Snackbar.LENGTH_SHORT)
            .setAction("Info",null).show()
    }
}