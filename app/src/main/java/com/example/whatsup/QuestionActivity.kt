package com.example.whatsup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.whatsup.databinding.ActivityQuestionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.random.Random

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    private lateinit var database:DatabaseReference
    private lateinit var questionList:ArrayList<Questions>
    private lateinit var  question:Questions
    private var index:Int=0
    private lateinit var fauth:FirebaseAuth
    private var correctAnswer:Long=0
    private lateinit var timer: CountDownTimer
    private lateinit var dialog:ProgressDialog
    private lateinit var catId: String
    private lateinit var bookList:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityQuestionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bookList= arrayListOf()
        fauth= FirebaseAuth.getInstance()
        questionList= arrayListOf()
        dialog= ProgressDialog(this)
        dialog.setMessage("Loading Questions")
        dialog.setCancelable(false)
        dialog.show()
        database=FirebaseDatabase.getInstance().getReference()
        catId= intent.getStringExtra("catId").toString()
        database.child("Questions").child(catId).get().addOnSuccessListener {
            Toast.makeText(this,"Questions",Toast.LENGTH_SHORT).show()
            questionList.clear()
            for(snapshot in it.children){
                var question=snapshot.getValue(Questions::class.java)
                if (question != null) {
                    questionList.add(question)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this,"No Questions",Toast.LENGTH_SHORT).show()
        }
        Handler().postDelayed({
            dialog.cancel()
            reset()
            setNextquestion()
            resetTimer()

        }, 1000)
    }
     fun setNextquestion() {
       if(index<questionList.size){
           question=questionList[index]
           binding.question.setText(question.ques)
           binding.option1.setText(question.opt1)
           binding.option2.setText(question.opt2)
           binding.option3.setText(question.opt3)
           binding.option4.setText(question.opt4)
           binding.bookMark.setOnClickListener {
               var uid=question.id.toString()
               database.child("BookMark").child(fauth.currentUser!!.uid).child(fauth.currentUser!!.uid).child(uid).setValue(uid).addOnSuccessListener {
                   Toast.makeText(this,"Question added in BookMark",Toast.LENGTH_SHORT).show()
               }
           }
       }
    }
    fun reset(){
        var currentQues=(index+1).toString()+"/"+questionList.size
        binding.questionCounter.setText(currentQues)
        binding.option1.setBackgroundResource(R.drawable.option_unselected)
        binding.option2.setBackgroundResource(R.drawable.option_unselected)
        binding.option3.setBackgroundResource(R.drawable.option_unselected)
        binding.option4.setBackgroundResource(R.drawable.option_unselected)
    }
    fun resetTimer(){
        timer = object: CountDownTimer(200000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.setText((millisUntilFinished/1000).toString())
            }
            override fun onFinish() {
                Toast.makeText(this@QuestionActivity,"Time Finished",Toast.LENGTH_SHORT).show()
                var intent=Intent(this@QuestionActivity,resultScreen::class.java)
                intent.putExtra("correct",correctAnswer)
                startActivity(intent)
                finish()
            }
        }
        timer.start()
    }
   public fun onclick(view: View){
        when(view.id){
            R.id.nextBtn-> {
                reset()
                index = index + 1
                if(index<questionList.size) {
                    setNextquestion()
                }else{
                    Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show()
                    timer.cancel()
                    var intent= Intent(this,resultScreen::class.java)
                    intent.putExtra("correct",correctAnswer)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.option1->{
                 checkAnswer(1)
            }
            R.id.option2->{
                checkAnswer(2)
            }
            R.id.option3->{
                checkAnswer(3)
            }
            R.id.option4->{
                checkAnswer(4)
            }
        }
    }
    fun checkAnswer(answer:Int){
        if(answer.equals(question.ans)){
            correctAnswer++
        }
        else{
            if(answer==1){
                binding.option1.setBackgroundResource(R.drawable.option_wrong)
            }
            else if(answer==2){
                binding.option2.setBackgroundResource(R.drawable.option_wrong)
            }
            else if(answer==3){
                binding.option3.setBackgroundResource(R.drawable.option_wrong)
            }
            else if(answer==4){
                binding.option4.setBackgroundResource(R.drawable.option_wrong)
            }
        }
        if(question.ans==1){
            binding.option1.setBackgroundResource(R.drawable.option_right)
        }
        else if(question.ans==2){
            binding.option2.setBackgroundResource(R.drawable.option_right)
        }
        else if(question.ans==3){
            binding.option3.setBackgroundResource(R.drawable.option_right)
        }
        else if(question.ans==4){
            binding.option4.setBackgroundResource(R.drawable.option_right)
        }
    }
    fun first(){
        setNextquestion()
    }
}