import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemHistoricoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.models.HistoricoServico

class HistoricoAdapter(private val historicoList: MutableList<HistoricoServico>) :
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
            tvNomeServico.text = item.nomeServico
            tvDataHora.text = item.dataHora
            tvStatusESalao.text = "Status: ${item.status} | Salão: ${item.salao}"
            tvValor.text = item.valor
        }
    }

    override fun getItemCount(): Int = historicoList.size


    fun addItem(historico: HistoricoServico) {
        historicoList.add(historico)
        notifyItemInserted(historicoList.size - 1)
    }

    fun updateList(newList: List<HistoricoServico>) {
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
