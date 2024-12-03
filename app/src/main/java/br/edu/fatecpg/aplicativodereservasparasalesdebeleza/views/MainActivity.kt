package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
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
            try {
                val email = binding.editEmail.text?.toString() ?: ""
                val senha = binding.editSenha.text?.toString() ?: ""

                if (email.isNotBlank() && senha.isNotBlank()) {
                    auth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Login bem-sucedido")
                                val user = auth.currentUser
                                user?.let {
                                    val userId = it.uid
                                    Log.d(TAG, "Verificando coleções para o usuário: $userId")
                                    db.collection("cliente").document(userId).get()
                                        .addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                Log.d(TAG, "Usuário encontrado na coleção cliente")
                                                val intent = Intent(this, SelecaoSaloesActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Log.d(TAG, "Usuário não encontrado na coleção cliente, verificando na coleção saloes")
                                                db.collection("saloes").document(userId).get()
                                                    .addOnSuccessListener { doc ->
                                                        if (doc.exists()) {
                                                            Log.d(TAG, "Usuário encontrado na coleção saloes")
                                                            val intent = Intent(this, GestaoAgendaActivity::class.java)
                                                            startActivity(intent)
                                                            Log.d(TAG, "Redirecionando para GestaoAgendaActivity")
                                                            finish()
                                                        } else {
                                                            Log.e(TAG, "Usuário não encontrado nas coleções cliente e saloes")
                                                            Toast.makeText(this, "Erro no login. Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.e(TAG, "Erro ao acessar coleção saloes: ${e.message}")
                                                        Toast.makeText(this, "Erro ao acessar dados do salão.", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Erro ao acessar coleção cliente: ${e.message}")
                                            Toast.makeText(this, "Erro ao acessar dados do cliente.", Toast.LENGTH_SHORT).show()
                                        }
                                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                                } ?: run {
                                    Log.e(TAG, "Usuário não encontrado após login bem-sucedido")
                                    Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.e(TAG, "Erro no login: ${task.exception?.message}")
                                Toast.makeText(this, "Erro no login. Tente novamente.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Campos Obrigatórios", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro inesperado durante o login: ${e.message}")
                Toast.makeText(this, "Erro inesperado. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.txtLinkCadastro.setOnClickListener {
            try {
                val intent = Intent(this, CadastroActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao iniciar CadastroActivity: ${e.message}")
                Toast.makeText(this, "Erro ao abrir tela de cadastro.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
