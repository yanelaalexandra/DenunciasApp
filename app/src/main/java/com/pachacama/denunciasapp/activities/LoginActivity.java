package com.pachacama.denunciasapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pachacama.denunciasapp.R;
import com.pachacama.denunciasapp.interfaces.ApiService;
import com.pachacama.denunciasapp.models.Usuario;
import com.pachacama.denunciasapp.singletons.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);

    }

    public void callLogin(View view) {
        final String user = username.getText().toString();
        String pass = password.getText().toString();

        if (user.isEmpty() || pass .isEmpty()) {
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call;
        call = service.login(user, pass);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Usuario usuario = response.body();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("usuario", usuario.getNombre());
                        startActivity(intent);
                        finish();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Usuario o Clave incorrectos");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });





    }
}
