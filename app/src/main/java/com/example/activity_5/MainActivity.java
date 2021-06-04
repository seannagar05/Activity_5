package com.example.activity_5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.activity_5.Retrofit.RetrofitBuilder;
import com.example.activity_5.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button button_convert;
    EditText text_base_currency,text_result_currency;
    Spinner spinner_from_currency,spinner_to_currency;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_convert = (Button) findViewById(R.id.btn_convert);
        text_base_currency = (EditText) findViewById(R.id.txt_currency_base);
        text_result_currency = (EditText) findViewById(R.id.txt_result_currency);
        spinner_from_currency = (Spinner) findViewById(R.id.spinner_from);
        spinner_to_currency = (Spinner) findViewById(R.id.spinner_to);

        String[] dropDownList = {"USD", "EUR", "KRW", "JPY", "PHP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dropDownList);
        spinner_from_currency.setAdapter(adapter);
        spinner_to_currency.setAdapter(adapter);

        button_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);

                Call<JsonObject> call = retrofitInterface.getExchangeCurrency(spinner_from_currency.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("response", String.valueOf(response.body()));
                        Toast.makeText(MainActivity.this, "Computed", Toast.LENGTH_SHORT).show();
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("conversion_rates");
                        Double currency = Double.valueOf(text_base_currency.getText().toString());
                        Double multiplier = Double.valueOf(rates.get(spinner_to_currency.getSelectedItem().toString()).toString());
                        Double result = currency * multiplier;
                        text_result_currency.setText(String.valueOf(result));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });


    }
}