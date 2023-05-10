package com.example.mobilesocialapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationTest {
    @Test
    fun `invalid email address returns false`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz",
            "Haslo123",
            "Haslo123",
            "mateuszbizon"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `invalid password length returns false`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz@wp.pl",
            "Haslo12",
            "Haslo12",
            "mateuszbizon"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty username returns false`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz@wp.pl",
            "Haslo123",
            "Haslo123",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password has less than 1 digit returns false`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz@wp.pl",
            "Haslo",
            "Haslo",
            "mateuszbizon"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password has less than 1 big letter returns false`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz@wp.pl",
            "haslo",
            "haslo",
            "mateuszbizon"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password are not equal returns false`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz@wp.pl",
            "Haslo123",
            "Haslo1234",
            "mateuszbizon"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid inputs returns true`() {
        val result = Registration.validateRegistrationInput(
            "mat-biz@wp.pl",
            "Haslo123",
            "Haslo123",
            "mateuszbizon"
        )
        assertThat(result).isTrue()
    }
}