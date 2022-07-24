package com.djamari.usageadvisor.ui.fragment_signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.djamari.usageadvisor.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_email;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_password;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class Fragment1 extends Fragment {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-zA-Z])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$");
    private TextInputEditText daftar_username;
    private TextInputEditText daftar_email;
    private TextInputEditText daftar_password;
    private TextInputEditText daftar_password_ulangi;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences sp = requireActivity().getSharedPreferences(VALUEID1, 0);
        final View root = inflater.inflate(R.layout.fragment_signup_1, container, false);
        daftar_username = root.findViewById(R.id.daftar_username);
        daftar_username.setText(sp.getString(SharePref_username, ""));

        daftar_email = root.findViewById(R.id.daftar_email);
        daftar_email.setText(sp.getString(SharePref_email, ""));

        daftar_password = root.findViewById(R.id.daftar_password);
        daftar_password.setText(sp.getString(SharePref_password, ""));

        daftar_password_ulangi = root.findViewById(R.id.daftar_password_ulangi);
        daftar_password_ulangi.setText(sp.getString(SharePref_password, ""));

        Button lanjut = root.findViewById(R.id.button_daftar);
        lanjut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validasi(requireContext().getApplicationContext());
            }
        });

        Button pindahSignIn = root.findViewById(R.id.pindah_signin);
        pindahSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();
            }
        });
        return root;
    }

    private void validasi(Context context) {
        SharedPreferences sp = context.getSharedPreferences(VALUEID1, 0);
        SharedPreferences.Editor editor = sp.edit();

        final String username_daftar = Objects.requireNonNull(daftar_username.getText()).toString().trim();
        final String email_daftar = Objects.requireNonNull(daftar_email.getText()).toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String password_daftar = Objects.requireNonNull(daftar_password.getText()).toString().trim();
        final String ulangi_password_daftar = Objects.requireNonNull(daftar_password_ulangi.getText()).toString().trim();

        if (username_daftar.isEmpty() && email_daftar.isEmpty() && password_daftar.isEmpty() && ulangi_password_daftar.isEmpty()) {
            daftar_username.setError("Username harus di isi");
            daftar_email.setError("Email harus di isi");
            daftar_password.setError("Password harus di isi");
            daftar_password_ulangi.setError("Ulangi password harus di isi");
        } else if (username_daftar.isEmpty()) {
            daftar_username.setError("Username harus di isi");
        } else if (email_daftar.isEmpty()) {
            daftar_email.setError("Email harus di isi");
        } else if (!email_daftar.matches(emailPattern)) {
            daftar_email.setError("Email harus di isi dengan format yang benar");
        } else if (password_daftar.isEmpty()) {
            daftar_password.setError("Password harus di isi");
        } else if (!PASSWORD_PATTERN.matcher(password_daftar).matches()) {
            daftar_password.setError("Password harus memiliki setidaknya 6 karakter kombinasi text dan angka dan harus tidak memiliki karakter whitespace");
            Toast.makeText(requireActivity(), "Password harus memiliki setidaknya 6 karakter kombinasi text dan angka dan harus tidak memiliki karakter whitespace", Toast.LENGTH_LONG).show();
        } else if (ulangi_password_daftar.isEmpty()) {
            daftar_password_ulangi.setError("Ulangi password harus di isi");
        } else if (!ulangi_password_daftar.equals(password_daftar)) {
            daftar_password_ulangi.setError("Ulangi password harus sama dengan password");
        } else {
            editor.putString(SharePref_id_username, random_id_username());
            editor.putString(SharePref_username, username_daftar);
            editor.putString(SharePref_email, email_daftar);
            editor.putString(SharePref_password, password_daftar);
            editor.apply();

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit).replace(R.id.fragment_signup, new Fragment2()).commit();
        }
    }
    private static String random_id_username() {

        String saltChar = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 4) {
            int index = (int) (random.nextFloat() * saltChar.length());
            salt.append(saltChar.charAt(index));
        }
        return salt.toString();
    }
}
