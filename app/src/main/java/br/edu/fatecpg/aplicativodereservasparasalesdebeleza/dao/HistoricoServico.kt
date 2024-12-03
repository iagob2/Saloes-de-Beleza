package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.models

import android.os.Parcel
import android.os.Parcelable
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Cliente
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao
import com.google.firebase.Timestamp

data class HistoricoServico(
    val salao: Salao = Salao(),
    val cliente: Cliente = Cliente(),
    val servico: Servico = Servico(),
    val nomeServico: String = "",
    val status: String = "",
    val dataHora: Timestamp? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Salao::class.java.classLoader) ?: Salao(),
        parcel.readParcelable(Cliente::class.java.classLoader) ?: Cliente(),
        parcel.readParcelable(Servico::class.java.classLoader) ?: Servico(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Timestamp::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(salao, flags)
        parcel.writeParcelable(cliente, flags)
        parcel.writeParcelable(servico, flags)
        parcel.writeString(nomeServico)
        parcel.writeString(status)
        parcel.writeParcelable(dataHora, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HistoricoServico> {
        override fun createFromParcel(parcel: Parcel): HistoricoServico {
            return HistoricoServico(parcel)
        }

        override fun newArray(size: Int): Array<HistoricoServico?> {
            return arrayOfNulls(size)
        }
    }
}
