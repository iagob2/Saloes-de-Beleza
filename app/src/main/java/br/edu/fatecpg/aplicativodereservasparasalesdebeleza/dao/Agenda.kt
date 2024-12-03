package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Agenda(
    val dataHora: Date = Date(),
    val cliente: Cliente = Cliente(),
    val salao: Salao = Salao(),
    val servico: Servico = Servico()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        Date(parcel.readLong()),
        parcel.readParcelable(Cliente::class.java.classLoader) ?: Cliente(),
        parcel.readParcelable(Salao::class.java.classLoader) ?: Salao(),
        parcel.readParcelable(Servico::class.java.classLoader) ?: Servico()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dataHora.time)
        parcel.writeParcelable(cliente, flags)
        parcel.writeParcelable(salao, flags)
        parcel.writeParcelable(servico, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Agenda> {
        override fun createFromParcel(parcel: Parcel): Agenda {
            return Agenda(parcel)
        }

        override fun newArray(size: Int): Array<Agenda?> {
            return arrayOfNulls(size)
        }
    }
}
