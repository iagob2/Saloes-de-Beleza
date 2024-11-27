import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemHistoricoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Agenda

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
            tvDataHora.text = item.dataHora.toString()
            tvStatusESalao.text = "Salão: ${item.salao.nomeCompleto}"
            tvValor.text = item.servico.preco
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
