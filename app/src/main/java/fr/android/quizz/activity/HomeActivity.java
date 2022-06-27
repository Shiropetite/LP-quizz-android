package fr.android.quizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.android.quizz.R;
import fr.android.quizz.adapter.UserAdapter;
import fr.android.quizz.model.User;
import fr.android.quizz.utils.Database;

public class HomeActivity extends AppCompatActivity {
    private ArrayAdapter<User> friendsAdapter;
    private ArrayList<User> friends;
    private ListView listFriends;
    private TextView username;
    private TextView score;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();

        listFriends = (ListView) findViewById(R.id.list_friends);
        username = findViewById(R.id.home_usersname);
        avatar = findViewById(R.id.quizz_image);
        score = findViewById(R.id.home_score);
        Button startQuizz = findViewById(R.id.button_start);

        Database.getUserByNameWithFriends(bundle.getString("username"))
                .observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        friends = user.getFriends();
                        friendsAdapter = new UserAdapter(HomeActivity.this, friends);
                        listFriends.setAdapter(friendsAdapter);
                        username.setText(user.getUsername());
                        score.setText(user.getScore() + " points");
                        Glide.with(HomeActivity.this).load(user.getAvatar()).into(avatar);
                    }
                });

        startQuizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QuizzActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }
}