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
            val precoText = binding.edtPrecoServico.text.toString()
            val duracao = binding.edtDuracaoServico.text.toString()
            val horariosText = binding.edtHorariosDisponiveis.text.toString()

            if (nome.isNotEmpty() && descricao.isNotEmpty() && precoText.isNotEmpty() && duracao.isNotEmpty() && horariosText.isNotEmpty()) {
                val preco = precoText.toDoubleOrNull()
                if (preco != null) {
                    val horarios = horariosText.split(",").map { it.trim() }
                    val servico = Servico(nome, descricao, preco, duracao, horarios)
                    servicosList.add(servico)
                    Toast.makeText(this, "Serviço adicionado!", Toast.LENGTH_SHORT).show()

                    binding.edtNomeServico.text.clear()
                    binding.edtDescricaoServico.text.clear()
                    binding.edtPrecoServico.text.clear()
                    binding.edtDuracaoServico.text.clear()
                    binding.edtHorariosDisponiveis.text.clear()
                } else {
                    Toast.makeText(this, "Preço inválido. Deve ser um número.", Toast.LENGTH_SHORT).show()
                }
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
