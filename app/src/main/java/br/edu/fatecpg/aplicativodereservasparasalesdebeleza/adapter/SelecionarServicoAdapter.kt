package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemServicoBinding

class SelecionarServicoAdapter(
    private val servicoList: List<Servico>
) : RecyclerView.Adapter<SelecionarServicoAdapter.ServicoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicoViewHolder {
        val binding = ItemServicoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServicoViewHolder, position: Int) {
        val servico = servicoList[position]
        holder.bind(servico)
    }

    override fun getItemCount(): Int = servicoList.size

    inner class ServicoViewHolder(private val binding: ItemServicoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(servico: Servico) {
            binding.tvNomeServico.text = servico.nome
            binding.tvPrecoDuracaoServico.text = "${servico.preco} - ${servico.duracao}"
            binding.tvDescricaoServico.text = servico.descricao
        }
    }
}
