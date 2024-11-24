package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

import android.os.Parcel
import android.os.Parcelable

data class Servico(
    val nome: String = "",
    val descricao: String = "",
    val preco: String = "",
    val duracao: String = "",
    val horarios: List<String> = emptyList() // Novo campo para horários
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(descricao)
        parcel.writeString(preco)
        parcel.writeString(duracao)
        parcel.writeStringList(horarios)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Servico> {
        override fun createFromParcel(parcel: Parcel): Servico {
            return Servico(parcel)
        }

        override fun newArray(size: Int): Array<Servico?> {
            return arrayOfNulls(size)
        }
    }
}
