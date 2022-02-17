package com.example.playgrounds01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playgrounds01.Classes.SecretSharedPrefs;
import com.example.playgrounds01.Classes.SharedPrefs;
import com.playgrounds01.myapp.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    //komponenty
    EditText etEmail, etPassword, etPassword2;
    TextView tvError, tvRegister;
    Button btnLogin, btnNextAct;
    CheckBox cbRememberMe;

    //proměná pro udržení informace Login/Register
    boolean loginScreen;


    //toolbar proměnné
    TextView textViewToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // nastavení Toolbaru jména activity
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        etEmail = (EditText) findViewById(R.id.loginEmail);
        etPassword = (EditText) findViewById(R.id.loginPassword);
        etPassword2 = (EditText) findViewById(R.id.loginPassword2);
        tvError = (TextView) findViewById(R.id.loginTextViewError);
        tvRegister = (TextView) findViewById(R.id.loginTextViewRegister);
        cbRememberMe = (CheckBox) findViewById(R.id.loginCheckBoxRemember);
        btnLogin = (Button) findViewById(R.id.loginBtnLogin);

        //při první načtení se zobrazí v pohledu Login layout
        setLoginLayout();


        //automatizace
        etEmail.setText("roman@aha.cz");
        etPassword.setText("Rominek178678##");



        // při kliknutí na žádost o registraci dojde ke změně layoutu
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginScreen)
                    setRegisterLayout();
                else
                    setLoginLayout();
            }
        });

        // při kliknutí na přihlášení
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginScreen)
                    btnLoginLoginLayout();
                else
                    btnLoginRegisterLayout();
            }
        });

    }

    //nastavení btnLogin při Login layoutu
    private void btnLoginLoginLayout() {
        //načtení hodnot z EditTextů
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean kontrola = true;

        if (kontrola && (email.length() < 1 || password.length() < 1)) {
            tvError.setText("Nejsou vyplněné EditText");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola && (!emailChecker(email))) {
            tvError.setText("Chyba syntax Email");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola && (!passwordChecker(password))) {
            tvError.setText("Chyba syntax Password");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola) {
            tvError.setVisibility(View.GONE);

            // uložení emailu a heslo do šiforvaného souboru (pouze pro přihlašovací údaje)
            SecretSharedPrefs secretShare = new SecretSharedPrefs(this);
            secretShare.putEmail(email);
            secretShare.putPassword(password);

            // uložení základního souboru do nešifrovaného souboru (je rychlejší než šifrovaný)
            SharedPrefs basicShare = new SharedPrefs(this);
            boolean remember = cbRememberMe.isChecked();
            basicShare.putRememberMe(remember);

            //Intent intent = new Intent(MainActivity.this, NextActivity.class);
            //startActivity(intent);
        }
    }

    //nastavení btnLogin při Register layoutu
    private void btnLoginRegisterLayout() {
        //načtení hodnot z EditTextů
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();


        boolean kontrola = true;

        if (kontrola && (email.length() < 1 || password.length() < 1 || password2.length() < 1)) {
            tvError.setText("Nejsou vyplněné EditText");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola && (!emailChecker(email))) {
            tvError.setText("Chyba syntax Email");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola && (!passwordChecker(password))) {
            tvError.setText("Chyba syntax Password");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola && (!password.equals(password2))) {
            tvError.setText("Chyba hesla nejsou stejná");
            tvError.setVisibility(View.VISIBLE);
            kontrola = false;
        }

        if (kontrola) {
            tvError.setVisibility(View.GONE);

            //nastavení pro úspěšnou registraci
            Toast.makeText(this, "Byl vám zaslan potvrzovací email", Toast.LENGTH_SHORT).show();

        }
    }

    // nastavení komponent pro zobrazení Login Layout
    private void setLoginLayout() {
        //změna vlastností komponent, tak ať sedí pro zvolený layout
        loginScreen = true;
        textViewToolbarTitle.setText("PŘIHLÁŠENÍ");
        etEmail.setText("");
        etPassword.setText("");
        etPassword2.setText("");
        etPassword2.setVisibility(View.GONE);
        cbRememberMe.setVisibility(View.VISIBLE);
        btnLogin.setText("Přihlásit");
        tvRegister.setText("Pro registraci klikněte ZDE");

    }

    // nastavení komponent pro zobrazení Register Layout
    private void setRegisterLayout() {
        //změna vlastností komponent, tak ať sedí pro zvolený layout
        loginScreen = false;
        textViewToolbarTitle.setText("REGISTRACE");
        etEmail.setText("");
        etPassword.setText("");
        etPassword2.setText("");
        etPassword2.setVisibility(View.VISIBLE);
        cbRememberMe.setVisibility(View.GONE);
        btnLogin.setText("Registrovat");
        tvRegister.setText("Zpět na přihlašovací obrazovku");
    }

    // kontrola syntaxe Emailu
    private boolean emailChecker(String txt) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern).matcher(txt).matches();
    }

    // kontrola syntaxe Password
    private boolean passwordChecker(String txt) {
        String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        return Pattern.compile(regexPattern).matcher(txt).matches();
    }
}