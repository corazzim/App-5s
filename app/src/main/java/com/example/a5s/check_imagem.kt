package com.example.a5s

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class check_imagem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_imagem)

        // Obtém a referência para a ImageView
        val imageCheck = findViewById<ImageView>(R.id.image_check)

        // Carrega a animação
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        // Aplica a animação à ImageView
        imageCheck.startAnimation(slideUpAnimation)

        // Aguarda 4 segundos antes de redirecionar para a página de login
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Finaliza a atividade atual para não permitir o retorno
        }, 4000)  // 4000 milissegundos = 4 segundos
    }
}
