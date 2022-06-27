package fr.android.quizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fr.android.quizz.R;
import fr.android.quizz.adapter.UserAdapter;
import fr.android.quizz.model.Question;
import fr.android.quizz.model.User;
import fr.android.quizz.utils.Database;

public class QuizzActivity extends AppCompatActivity {
    private User user;
    private Button isTrue;
    private Button isFalse;
    private TextView statement;
    private TextView explanation;
    private TextView timer;
    private ImageView image;
    private TextView answer;
    private Button nextQuestion;
    private CountDownTimer realTimer;

    private Question rdmQuestion;
    private ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        Bundle bundle = getIntent().getExtras();
        Database.getUserByNameWithFriends(bundle.getString("username"))
                .observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User logUser) {
                       user = logUser;
                    }
                });

        isTrue = findViewById(R.id.quizz_answer1);
        isFalse = findViewById(R.id.quizz_answer2);
        statement = findViewById(R.id.quizz_statement);
        explanation = findViewById(R.id.quizz_explanation);
        timer = findViewById(R.id.quizz_chrono);
        image = findViewById(R.id.quizz_image);
        answer = findViewById(R.id.quizz_statement_answer);
        nextQuestion = findViewById(R.id.quizz_next);

        Database.getRandomQuestions().observe(this, new Observer<ArrayList<Question>>() {
            @Override
            public void onChanged(ArrayList<Question> questions) {
               if (questions.size() > 0) {
                   questionList = questions;

                   statement.setText(questionList.get(0).getStatement());
                   Glide.with(QuizzActivity.this).load(questionList.get(0).getImage()).into(image);
                   rdmQuestion = questionList.get(0);
                   timer.setText("00:30");
                   explanation.setText("");
                   answer.setText("");
                   nextQuestion.setVisibility(View.GONE);

                   questionList.remove(0);

                   createTimer();
               }
            }
        });

        isTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realTimer.cancel();
                if(rdmQuestion.isAnswer() == true) {
                    user.setScore(user.getScore() + 10);
                    answer.setTextColor(getColor(R.color.green));
                }
                else {
                    answer.setTextColor(getColor(R.color.red));
                }

                isTrue.setVisibility(View.GONE);
                isFalse.setVisibility(View.GONE);
                answer.setText("La réponse est " + (rdmQuestion.isAnswer() == true ? "VRAI" : "FAUX"));
                explanation.setText(rdmQuestion.getExplanation());
                nextQuestion.setVisibility(View.VISIBLE);

                if(questionList.size() == 0) {
                    nextQuestion.setText("Voir mon score");
                }
            }
        });

        isFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realTimer.cancel();
                if(rdmQuestion.isAnswer() == false) {
                    user.setScore(user.getScore() + 10);
                    answer.setTextColor(getColor(R.color.green));
                }
                else {
                    answer.setTextColor(getColor(R.color.red));
                }

                isTrue.setVisibility(View.GONE);
                isFalse.setVisibility(View.GONE);
                answer.setText("La réponse est " + (rdmQuestion.isAnswer() == true ? "VRAI" : "FAUX"));
                explanation.setText(rdmQuestion.getExplanation());
                nextQuestion.setVisibility(View.VISIBLE);

                if(questionList.size() == 0) {
                    nextQuestion.setText("Voir mon score");
                }
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(questionList.size() > 0) {
                    statement.setText(questionList.get(0).getStatement());
                    Glide.with(QuizzActivity.this).load(questionList.get(0).getImage()).into(image);
                    rdmQuestion = questionList.get(0);
                    timer.setText("00:30");
                    explanation.setText("");
                    answer.setText("");
                    nextQuestion.setVisibility(View.GONE);
                    isTrue.setVisibility(View.VISIBLE);
                    isFalse.setVisibility(View.VISIBLE);

                    questionList.remove(0);

                    createTimer();
                }
                else {
                    finishQuizz();
                }
            }
        });

    }

    protected void createTimer() {
        realTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                int remaining = Math.round(millisUntilFinished / 1000);
                timer.setText("00:" + remaining);
            }

            public void onFinish() {
                if(questionList.size() > 0) {

                    statement.setText(questionList.get(0).getStatement());
                    Glide.with(QuizzActivity.this).load(questionList.get(0).getImage()).into(image);
                    rdmQuestion = questionList.get(0);
                    timer.setText("00:30");
                    explanation.setText("");
                    answer.setText("");
                    nextQuestion.setVisibility(View.GONE);

                    questionList.remove(0);

                    createTimer();
                }
                else {
                    finishQuizz();
                }
            }
        };

        realTimer.start();
    }

    protected void finishQuizz() {
        Database.updateScore(user).observe(QuizzActivity.this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean success) {
                if(success) {
                    Intent intent = new Intent(QuizzActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", user.getUsername());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }

        });
    }
}