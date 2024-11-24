package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityNovoServicoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico

class NovoServicoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNovoServicoBinding
    private val servicosList = mutableListOf<Servico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityNovoServicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrarServico.setOnClickListener {
            val nome = binding.edtNomeServico.text.toString()
            val descricao = binding.edtDescricaoServico.text.toString()
            val preco = binding.edtPrecoServico.text.toString()
            val duracao = binding.edtDuracaoServico.text.toString()

            if (nome.isNotEmpty() && descricao.isNotEmpty() && preco.isNotEmpty() && duracao.isNotEmpty()) {
                val servico = Servico(nome, descricao, preco, duracao)
                servicosList.add(servico)
                Toast.makeText(this, "Serviço adicionado!", Toast.LENGTH_SHORT).show()
                // Limpa os campos para adicionar um novo serviço
                binding.edtNomeServico.text.clear()
                binding.edtDescricaoServico.text.clear()
                binding.edtPrecoServico.text.clear()
                binding.edtDuracaoServico.text.clear()
            } else {
                Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.txtVoltar.setOnClickListener {
            val resultIntent = Intent().apply {
                putParcelableArrayListExtra("lista_servicos", ArrayList(servicosList))
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
