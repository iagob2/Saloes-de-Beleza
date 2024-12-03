package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemAgendamentoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.models.HistoricoServico
import java.text.SimpleDateFormat
import java.util.Locale

class GestaoAgendaAdapter(
    private val historicoList: MutableList<HistoricoServico>,
    private val onItemClick: (HistoricoServico) -> Unit
) : RecyclerView.Adapter<GestaoAgendaAdapter.GestaoAgendaViewHolder>() {

    inner class GestaoAgendaViewHolder(val binding: ItemAgendamentoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(historicoList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestaoAgendaViewHolder {
        val binding = ItemAgendamentoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GestaoAgendaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GestaoAgendaViewHolder, position: Int) {
        val historico = historicoList[position]
        with(holder.binding) {
            tvNomeCliente.text = historico.cliente.email // Acessa o nome do cliente (email, por exemplo)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvDataHora.text = historico.dataHora?.toDate()?.let { dateFormat.format(it) } ?: "Data inválida"

            tvServico.text = "Serviço: ${historico.servico.nome}" // Acessa o nome do serviço
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
