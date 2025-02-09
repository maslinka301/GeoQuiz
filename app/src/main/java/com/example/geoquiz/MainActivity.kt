    package com.example.geoquiz

    import android.app.Activity
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.activity.viewModels
    import com.example.geoquiz.databinding.ActivityMainBinding

    private const val TAG = "MainActivity"
    private const val KEY = "index"
    private const val REQUEST_CODE_CHEAT = 0

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding


//        private var isAnswered = false

        //private var listOfAnswers = listOf(false, false, false, false, false, false)

        private val quizViewModel: QuizViewModel by viewModels()

        private val cheatLancher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                quizViewModel.updateQuestionStatus(quizViewModel.currentIndex, -2)
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            //quizViewModel.currentIndex = savedInstanceState?.getInt(KEY, 0) ?: 0


            Log.d(TAG,"onCreate(Bundle?) called")
            binding =
                ActivityMainBinding.inflate(layoutInflater) //ActivityMainBinding это указывается имя макета, который раздуваем
            setContentView(binding.root)


            binding.questionTextView.setOnClickListener {
                quizViewModel.moveToNext()
                updateQuestion()
                enableOfButtons()
            }

            binding.trueButton.setOnClickListener {
                checkAnswer(true)
                enableOfButtons()
            }

            binding.falseButton.setOnClickListener {
                checkAnswer(false)
                enableOfButtons()
            }

            binding.nextButton.setOnClickListener {
                quizViewModel.moveToNext()
                updateQuestion()
                enableOfButtons()
            }

            binding.prevButton.setOnClickListener {
                quizViewModel.moveToPrev()
                updateQuestion()
                enableOfButtons()
            }

            binding.cheatButton.setOnClickListener {
                //val intent = Intent(this, CheatActivity::class.java ) //контекст указывает, в каком пакете искать активность, а класс - какая активность
                val intent = CheatActivity.newIntent(this, quizViewModel.currentQuestionAnswer)
                //startActivity(intent)
                cheatLancher.launch(intent)
            }

            binding.finishButton?.setOnClickListener {
                val rightAnswers = quizViewModel.getAnswers(1)
                val cheatAnswers = quizViewModel.getAnswers(-2)

                quizViewModel.resetGame()
                updateQuestion()
                enableOfButtons()

                val intent = Intent(this, GameOverActivity::class.java).apply {
                    putExtra("EXTRA_RIGHT_ANSWERS", rightAnswers)
                    putExtra("EXTRA_CHEAT_ANSWERS", cheatAnswers)
                }
                startActivity(intent)
            }

            updateQuestion()
            enableOfButtons()
        }

        override fun onStart() {
            super.onStart()
            Log.d(TAG, "onStart() called")
        }

        override fun onResume() {
            super.onResume()
            Log.d(TAG, "onResume() called")
        }

        override fun onPause() {
            super.onPause()
            Log.d(TAG, "onPause() called")
        }

        override fun onStop() {
            super.onStop()
            Log.d(TAG, "onStop() called")
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.d(TAG, "onDestroy() called")
        }

        private fun updateQuestion() {
            binding.questionTextView.setText(quizViewModel.currentQuestionText)
            binding.prevButton.isEnabled = quizViewModel.currentIndex != 0
            binding.nextButton.isEnabled = quizViewModel.currentIndex != 5
        }

        private fun checkAnswer(userAnswer: Boolean) {
            val correctAnswer = quizViewModel.currentQuestionAnswer
            val status: Int

            if (quizViewModel.getQuestionStatus(quizViewModel.currentIndex) != -2 ){
                if( userAnswer == correctAnswer){
                    status = 1
                }
                else
                    status = -1
                quizViewModel.updateQuestionStatus(quizViewModel.currentIndex, status)
            }


            val message = when(quizViewModel.getQuestionStatus(quizViewModel.currentIndex)) {
                1 -> R.string.correct_toast
                -1 -> R.string.incorrect_toast
                -2 -> R.string.judgment_toast
                else -> -100
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        private fun enableOfButtons(){ //отвечает за доступность кнопок. Когда стоишь на вопросе нельзя нажать на кнопку ответа дважды
            val isAnswered = quizViewModel.getQuestionStatus(quizViewModel.currentIndex) == 0

            binding.trueButton.isEnabled = isAnswered
            binding.falseButton.isEnabled = isAnswered
        }

    }