package com.example.mobilesocialapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoginTest {

    @Test
    fun `empty username returns false`() {
        val result = Login.validateLoginInput(
            "",
            "Haslo123"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = Login.validateLoginInput(
            "mateuszbizon",
            ""
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `valid inputs returns true`() {
        val result = Login.validateLoginInput(
            "mateuszbizon",
            "Haslo123"
        )

        assertThat(result).isTrue()
    }
}