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
import com.pachacama.denunciasapp.models.ResponseMessage;
import com.pachacama.denunciasapp.singletons.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText nombresInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        nombresInput  = findViewById(R.id.fullname_input);

    }

    public void callRegister(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String nombres = nombresInput.getText().toString();
        String tipo = "2";

        if (username.isEmpty() || password.isEmpty() || nombres.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<ResponseMessage> call;

        call = service.createUser(username, password, nombres, tipo);

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage: " + responseMessage);
                        Toast.makeText(RegisterActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    public void callCancel(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
