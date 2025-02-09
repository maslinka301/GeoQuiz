package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.geoquiz.databinding.ActivityGameOverBinding

private lateinit var binding: ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        val rightAnswers = intent.getIntExtra("EXTRA_RIGHT_ANSWERS", 0)
        val cheatAnswers = intent.getIntExtra("EXTRA_CHEAT_ANSWERS", 0)

        binding.resultTextView.setText(getString(R.string.results, rightAnswers))
        binding.cheatCountTextView.setText(cheatAnswers.toString())

        binding.playAgainButton.setOnClickListener {
            //quizViewModel.resetGame()
            finish()
        }

    }
}