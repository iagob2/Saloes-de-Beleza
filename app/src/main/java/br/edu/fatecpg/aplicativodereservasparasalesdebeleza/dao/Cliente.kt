package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

import android.os.Parcel
import android.os.Parcelable

data class Cliente(
    val email: String = "",
    val nomeCompleto: String = "",
    val agendamentos: List<Agenda> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Agenda.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(nomeCompleto)
        parcel.writeTypedList(agendamentos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cliente> {
        override fun createFromParcel(parcel: Parcel): Cliente {
            return Cliente(parcel)
        }

        override fun newArray(size: Int): Array<Cliente?> {
            return arrayOfNulls(size)
        }
    }
}
