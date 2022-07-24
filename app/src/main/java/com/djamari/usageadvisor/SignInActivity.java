package com.djamari.usageadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.djamari.usageadvisor.otherActivity.Setting.SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_akses_pengguna;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_email;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_group;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_id_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_password;
import static com.djamari.usageadvisor.helper.SharePref_keys.SharePref_username;
import static com.djamari.usageadvisor.helper.SharePref_keys.VALUEID1;

public class SignInActivity extends AppCompatActivity {
    TextInputEditText signin_email, signin_password;
    Button button_signin, button_signup, button_lupa_password, button_pelajari_aplikasi;
    FirebaseAuth firebaseAuth;
    LinearLayout progressBar;


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(SignInActivity.this, "Anda Telah Login", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SignInActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IntentFilter lockfilter = new IntentFilter();
//        lockfilter.addAction(Intent.ACTION_SCREEN_ON);
//        lockfilter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(screenTimeService, lockfilter);

        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_signin);


        signin_email = findViewById(R.id.signin_email);
        signin_password = findViewById(R.id.signin_password);

        progressBar = findViewById(R.id.progressBar);

        button_lupa_password = findViewById(R.id.lupa_password);
        button_pelajari_aplikasi = findViewById(R.id.pelajari_aplikasi);

        button_signin = findViewById(R.id.button_signin);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validasi();
            }
        });

        button_signup = findViewById(R.id.pindah_buatakun);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                unregisterReceiver(screenTimeService);
                SharedPreferences sp = getSharedPreferences(VALUEID1, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(SharePref_username, "");
                editor.putString(SharePref_id_username, "");
                editor.putString(SharePref_email, "");
                editor.putString(SharePref_password, "");
                editor.putString(SharePref_akses_pengguna, "");
                editor.putString(SharePref_id_group, "");
                editor.apply();
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }


    public void validasi() {
        String email_signin = Objects.requireNonNull(signin_email.getText()).toString().trim();
        String password_signin = Objects.requireNonNull(signin_password.getText()).toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email_signin.isEmpty()) {
            signin_email.setError("Mohon email di isi");
        } else if (!email_signin.matches(emailPattern)) {
            signin_email.setError("Mohon di isi menggunakan format email");
        } else if (password_signin.isEmpty()) {
            signin_password.setError("Password harus di isi");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            signin_email.setEnabled(false);
            signin_password.setEnabled(false);
            button_signin.setEnabled(false);
            button_signup.setEnabled(false);
            button_pelajari_aplikasi.setEnabled(false);
            button_lupa_password.setEnabled(false);
            firebaseAuth.signInWithEmailAndPassword(email_signin, password_signin).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = user.getUid();
                        SharedPreferences sp = getSharedPreferences(VALUEID1, 0);
                        final SharedPreferences.Editor editor = sp.edit();

                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        DocumentReference docIdRef = firestore.collection("Users").document(userId);
                        docIdRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String sharePref_email = documentSnapshot.getString("email");
                                    String sharePref_username = documentSnapshot.getString("username");
                                    String sharePref_id_username = documentSnapshot.getString("id_username");
                                    String sharePref_akses_pengguna = documentSnapshot.getString("akses_pengguna");
                                    String sharePref_id_group = documentSnapshot.getString("id_group");

                                    editor.putString(SharePref_email, sharePref_email);
                                    editor.putString(SharePref_username, sharePref_username);
                                    editor.putString(SharePref_id_username, sharePref_id_username);
                                    editor.putString(SharePref_akses_pengguna, sharePref_akses_pengguna);
                                    editor.putString(SharePref_id_group, sharePref_id_group);
                                    editor.apply();

                                    Log.i("TAG", "onSuccess: " + sharePref_email);
                                    Log.i("TAG", "onSuccess: " + sharePref_username);
                                    Log.i("TAG", "onSuccess: " + sharePref_id_username);
                                    Log.i("TAG", "onSuccess: " + sharePref_akses_pengguna);
                                    Log.i("TAG", "onSuccess: " + sharePref_id_group);

                                    Toast.makeText(SignInActivity.this, "Berhasil login", Toast.LENGTH_SHORT).show();
                                    Handler handler1 = new Handler();
                                    handler1.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(SignInActivity.this, SettingActivity.class);
                                            startActivity(i);
                                        }
                                    }, 1500);
                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        signin_email.setEnabled(true);
                        signin_email.setFocusable(false);
                        signin_password.setFocusable(false);
                        signin_password.setEnabled(true);
                        button_signin.setEnabled(true);
                        button_signup.setEnabled(true);
                        button_pelajari_aplikasi.setEnabled(true);
                        button_lupa_password.setEnabled(true);
                        Toast.makeText(SignInActivity.this, "Gagal melakukan sign in, email atau password yang anda masukkan salah, atau mungkin anda belum sign up", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
