package com.example.playgrounds01.Classes;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecretSharedPrefs {

    SharedPreferences spCrypto;

    // jméno souboru s sharedPreferences
    private final String nameSharePrefs = "secretOptions";

    // jména pro hodnoty uložené v souboru
    private final String nameEmail = "email";
    private final String namePassword = "password";


    public SecretSharedPrefs(Context context){
        // vytvoření klíče pro šifrování
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            // inicializace šifrované SharedPreferences
            spCrypto = EncryptedSharedPreferences.create(nameSharePrefs, masterKeyAlias,
                    context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEmail(){
        return spCrypto.getString(nameEmail, null);
    }

    public String getPassword(){
        return spCrypto.getString(namePassword, null);
    }

    public void putEmail(String value){
        SharedPreferences.Editor editorCrypto = spCrypto.edit();
        editorCrypto.putString(nameEmail, value);
        editorCrypto.apply();
    }

    public void putPassword(String value){
        SharedPreferences.Editor editorCrypto = spCrypto.edit();
        editorCrypto.putString(namePassword, value);
        editorCrypto.apply();
    }
}
