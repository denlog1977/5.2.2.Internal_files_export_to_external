package com.example.a522internal_files_export_to_external;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        final CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(preferences.getBoolean("external", false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putBoolean("external", checkBox.isChecked());
                myEditor.apply();
            }
        });



        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView editTextLogin = findViewById(R.id.editTextLogin);
                TextView editTextPassword = findViewById(R.id.editTextPassword);
                String editTextLoginToString = editTextLogin.getText().toString();
                String editTextPasswordToString = editTextPassword.getText().toString();

                if(editTextLoginToString.isEmpty() || editTextPasswordToString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Не заполнены поля логин-пароль", Toast.LENGTH_LONG).show();
                    return;
                }

                String login = "";
                String password = "";

                if (checkBox.isChecked()) {
                    try {
                        File loginpasswordFile = new File(getApplicationContext().getExternalFilesDir(null),"loginpassword.txt");
                        FileReader fileReader = new FileReader(loginpasswordFile);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        login = bufferedReader.readLine();
                        password = bufferedReader.readLine();
                        Log.i("BufferedReader", "Прочитано из внешнего файла: login =  " + login + " password = " + password);
                        Toast.makeText(MainActivity.this, "Прочитано из внешнего файла: login =  " + login + " password = " + password, Toast.LENGTH_LONG).show();
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("BufferedReader", e.toString());
                        Toast.makeText(MainActivity.this, "e.toString() = " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        InputStreamReader inputStreamReader = new InputStreamReader(openFileInput("loginpassword.txt"));
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        login = reader.readLine();
                        password = reader.readLine();
                        Log.i("BufferedReader", "Прочитано из внутреннего файла: login =  " + login + " password = " + password);
                        Toast.makeText(MainActivity.this, "Прочитано из внутреннего файла: login =  " + login + " password = " + password, Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (editTextLoginToString.equals(login) && editTextPasswordToString.equals(password)) {
                    Toast.makeText(MainActivity.this, "Login-password is correct\n" + login + " " + password, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Некорректная пара Login-password\n" + "Вы ввели логин " + editTextLoginToString + " а надо " + login + "\n" + "Вы ввели пароль " + editTextPasswordToString + " а надо " + password, Toast.LENGTH_LONG).show();
                }

            }

        });


        Button buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView editTextLogin = findViewById(R.id.editTextLogin);
                TextView editTextPassword = findViewById(R.id.editTextPassword);
                CheckBox checkBox = findViewById(R.id.checkBox);

                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();

                if(login.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Не заполнены поля логин-пароль", Toast.LENGTH_LONG).show();
                    return;
                }

                String fileContents = login + "\n" + password;
                if (checkBox.isChecked()) {
                    File loginpasswordFile = new File(getApplicationContext().getExternalFilesDir(null),"loginpassword.txt");
                    try {
                        FileWriter logWriter = new FileWriter(loginpasswordFile);
                        logWriter.append(fileContents);
                        logWriter.close();
                        Toast.makeText(MainActivity.this, "Логин пароль записаны во ВНЕШНИЙ файл: " + loginpasswordFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        Log.i("BufferedReader", "Логин пароль записаны во ВНЕШНИЙ файл: " + login +" " + password + "    путь к файлу: " + loginpasswordFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("BufferedReader", e.toString());
                    }
                } else {
                    try {
                        FileOutputStream outputStream = openFileOutput("loginpassword.txt", Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                        Toast.makeText(MainActivity.this, "Вы зарегистрированы с логин паролем: " + login +" " + password, Toast.LENGTH_SHORT).show();
                        Log.i("BufferedReader", "ЗАПИСАНЫ ВО внутренний ФАЙЛ логин пароль: " + login +" " + password);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private boolean getSharePref() {
        try {
            return preferences.getBoolean("external", false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

    }

}
