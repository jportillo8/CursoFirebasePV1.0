package com.jportillo8.criptotraiderld.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jportillo8.criptotraiderld.R
import com.jportillo8.criptotraiderld.model.Crypto
import com.squareup.picasso.Picasso

class CryptosAdapter (val cryptosAdapterListener: CryptosAdapterListener): RecyclerView.Adapter<CryptosAdapter.ViewHolder>(){

    var cryptoList: List<Crypto> = ArrayList()




    //13. Implementacon de los metodos del adapter
    // El onCreateViewHolder va a crear el ViewHolder que definimos basado en la vista de row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    //El onBindView..  es el que actualiza los valores
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crypto = cryptoList[position]

        Picasso.get().load(crypto.imageUrl).into(holder.image)
        holder.name.text = crypto.name
        holder.available.text = holder.itemView.context.getString(R.string.available_message, crypto.available.toString())
        holder.buyButton.setOnClickListener {

            cryptosAdapterListener.onBuyCryptoClicked(crypto)
        }

    }

//13...

    //13. Actualizaciones de los componentes
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var image = view.findViewById<ImageView>(R.id.image)
        var name = view.findViewById<TextView>(R.id.nameTextView)
        var available = view.findViewById<TextView>(R.id.availableTextView)
        var buyButton = view.findViewById<TextView>(R.id.buyButton)
    }
    //13.


}