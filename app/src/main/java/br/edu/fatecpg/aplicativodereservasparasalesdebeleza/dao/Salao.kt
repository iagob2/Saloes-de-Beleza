package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

import android.os.Parcel
import android.os.Parcelable

data class Salao(
    val nome: String = "",
    val email: String = "",
    val endereco: String? = null,
    val horario: String = "",
    val diasDeFuncionamento: List<String> = emptyList(),
    val nota: Double? = null,
    val servicos: List<Servico> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(Servico.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(email)
        parcel.writeString(endereco)
        parcel.writeString(horario)
        parcel.writeStringList(diasDeFuncionamento)
        parcel.writeValue(nota)
        parcel.writeTypedList(servicos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Salao> {
        override fun createFromParcel(parcel: Parcel): Salao {
            return Salao(parcel)
        }

        override fun newArray(size: Int): Array<Salao?> {
            return arrayOfNulls(size)
        }
    }
}
