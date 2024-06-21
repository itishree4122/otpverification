package com.example.otpverification

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var repasswordEditText: TextInputEditText
    private lateinit var repasswordLayout: TextInputLayout

    private var isPasswordVisible: Boolean = false
    private var isRepasswordVisible: Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.otp_layout)
        // Initialize the password fields
        val passwordLayout: TextInputLayout = findViewById(R.id.password)
        val passwordEditText: TextInputEditText = findViewById(R.id.password_user)
        val repasswordLayout: TextInputLayout = findViewById(R.id.repassword)
        val repasswordEditText: TextInputEditText = findViewById(R.id.repassword_user)

        // Add any additional logic here if needed

        // For example, you can set the input type programmatically if needed:
        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        repasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Alternatively, if you want to manually handle the toggle (though not needed with endIconMode)
        passwordLayout.setEndIconOnClickListener {
            togglePasswordVisibility(passwordEditText)
        }
        repasswordLayout.setEndIconOnClickListener {
            togglePasswordVisibility(repasswordEditText)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.otplayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (isPasswordStrong(password)) {
                    passwordLayout.helperText = "Strong password"
                    passwordLayout.setHelperTextColor(resources.getColorStateList(R.color.colorStrong))
                } else if (password.length >= 8) {
                    passwordLayout.helperText = "Weak password"
                    passwordLayout.setHelperTextColor(resources.getColorStateList(R.color.colorWeak))
                } else {
                    passwordLayout.helperText = "Password must be at least 8 characters"
                    passwordLayout.setHelperTextColor(resources.getColorStateList(R.color.colorError))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


    }

    private fun togglePasswordVisibility(editText: TextInputEditText) {
        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.text?.length ?: 0)  // Move cursor to the end
    }
    private fun isPasswordStrong(password: String): Boolean {
        // Regex for a strong password
        val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&#]).{8,}$")
        return passwordPattern.containsMatchIn(password)
    }

    }
