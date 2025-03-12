package com.example.calculator111

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Button
import android.widget.EditText
import androidx.credentials.CreatePasswordRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import java.security.KeyStore
import javax.crypto.KeyGenerator

class SetupPassKeyActivity : AppCompatActivity() {

    private lateinit var etPassKey: EditText
    private lateinit var etConfirmPassKey: EditText
    private lateinit var btnSetPassKey: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_pass_key)

        etPassKey = findViewById(R.id.etPassKey)
        etConfirmPassKey = findViewById(R.id.etConfirmPassKey)
        btnSetPassKey = findViewById(R.id.btnSetPassKey)


        btnSetPassKey.setOnClickListener {
            val passKey = etPassKey.text.toString()
            val confirmPassKey = etConfirmPassKey.text.toString()

            if (passKey == confirmPassKey) {
                savePassKey(this, passKey) // Сохраняем Pass Key
                generatePassKey() // Генерируем ключ в KeyStore
                showMessage("Pass Key успешно установлен")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showError("Pass Key не совпадает")
            }
        }
    }

    fun generatePassKey() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            "pass_key_alias", // Уникальный алиас для ключа
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM) // Режим шифрования
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE) // Без дополнения
            .setUserAuthenticationRequired(true) // Требуется аутентификация пользователя
            .setUserAuthenticationValidityDurationSeconds(30) // Время действия аутентификации
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey() // Генерация ключа
    }

    private fun savePassKey(context: Context, key: String) {
        val credentialManager = CredentialManager.create(context)

        val request = CreatePasswordRequest(
            id = key, // Логин пользователя
            password = key // Пароль (Passkey)
        )
        CoroutineScope(Dispatchers.IO).launch {
            credentialManager.createCredential(context, request)
        }
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("pass_key", key)
        editor.apply()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}