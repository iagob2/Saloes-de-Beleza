import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemHistoricoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Agenda
import java.text.SimpleDateFormat
import java.util.Locale

class HistoricoAdapter(private val historicoList: MutableList<Agenda>) :
    RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    inner class HistoricoViewHolder(val binding: ItemHistoricoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val binding = ItemHistoricoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoricoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val item = historicoList[position]
        with(holder.binding) {
            tvNomeServico.text = item.servico.nome
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvDataHora.text = item.dataHora?.let { dateFormat.format(it) } ?: "Data inválida"
            tvStatusESalao.text = "Salão: ${item.salao.nome}"
            tvValor.text = "R$ ${item.servico.preco}" // Converte o Double para String
        }
    }

    override fun getItemCount(): Int = historicoList.size

    fun addItem(agenda: Agenda) {
        historicoList.add(agenda)
        notifyItemInserted(historicoList.size - 1)
    }

    fun updateList(newList: List<Agenda>) {
        historicoList.clear()
        historicoList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < historicoList.size) {
            historicoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
