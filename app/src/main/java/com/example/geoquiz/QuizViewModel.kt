package com.example.geoquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "index"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val QUESTION_STATUS_LIST = "QUESTION_STATUS_LIST"

class QuizViewModel(val state: SavedStateHandle) : ViewModel() {


    private val questionBank = listOf(
        Question(R.string.question_first_text, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    private var questionStatusList //= mutableListOf(0, 0, 0, 0, 0, 0)
        get() = state.get(QUESTION_STATUS_LIST) ?: mutableListOf(0, 0, 0, 0, 0, 0)
        set(value) = state.set(QUESTION_STATUS_LIST, value)
//статусы:
//0 - не отвечен
//1 - отвечен правильно
//-1 - отвечен не правильно
//-2 - считерен

    var currentIndex
        get() = state.get(CURRENT_INDEX_KEY)
            ?: 0 //если что-то там есть, берет это. Если там ниче нет, берет 0
        set(value) = state.set(CURRENT_INDEX_KEY, value)

    var isCheater: Boolean
        get() = state.get(IS_CHEATER_KEY) ?: false
        set(value) = state.set(IS_CHEATER_KEY, value)

    private var isAnswered = false


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }

    fun updateQuestionStatus(index: Int, newStatus: Int){
        if (index in questionStatusList.indices){
            questionStatusList = questionStatusList.apply { this[index] = newStatus }
        }
    }

    fun getQuestionStatus(index: Int): Int{
        return questionStatusList[index]
    }


    fun getAnswers(type: Int): Int{
        var count = 0
        questionStatusList.forEach{number->
            if (number == type)
                count++
        }
        return count
    }

    fun resetGame() {
        currentIndex = 0
        isCheater = false
        questionStatusList = mutableListOf(0, 0, 0, 0, 0, 0)
    }


}