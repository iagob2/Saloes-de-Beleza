package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val db = Firebase.firestore

        binding.btnEntrar.setOnClickListener {
            if (binding.editEmail.text != null && binding.editSenha.text != null) {
                val email = binding.editEmail.text.toString()
                val senha = binding.editSenha.text.toString()

                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                val userId = it.uid
                                db.collection("cliente").document(userId).get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            val intent = Intent(this, SelecaoSaloesActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            db.collection("saloes").document(userId).get()
                                                .addOnSuccessListener { doc ->
                                                    if (doc.exists()) {
                                                        val intent = Intent(this, GestaoAgendaActivity::class.java)
                                                        startActivity(intent)
                                                    }
                                                }
                                        }
                                    }
                                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            } ?: run {
                                Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Erro no login. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Campos Obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.txtLinkCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}
