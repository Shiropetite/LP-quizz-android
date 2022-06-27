package fr.android.quizz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.android.quizz.R;
import fr.android.quizz.utils.Database;

public class LoginActivity extends AppCompatActivity {
    private EditText inputLogin;
    private EditText inputPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.inputLogin = this.findViewById(R.id.input_username);
        this.inputPassword = this.findViewById(R.id.input_password);
        this.buttonLogin = this.findViewById(R.id.button_login);

        this.buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Database.login(inputLogin.getText().toString(), inputPassword.getText().toString())
                       .observe(LoginActivity.this, new Observer<Integer>() {
                           @Override
                           public void onChanged(Integer success) {
                               if(success == 0) {
                                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                   Bundle bundle = new Bundle();
                                   bundle.putString("username", inputLogin.getText().toString());
                                   intent.putExtras(bundle);
                                   startActivity(intent);
                                   finish();
                               }
                               else if(success == 1) {
                                   Toast.makeText(LoginActivity.this, "Le pseudo ou le mot de passe est incorrecte", Toast.LENGTH_SHORT).show();
                               }

                           }
                       });

            }

        });
    }

}