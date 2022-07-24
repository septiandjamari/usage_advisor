package com.djamari.usageadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.djamari.usageadvisor.ui.fragment_signup.Fragment1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
//    public static final Pattern PASSWORD_PATTERN =
//            Pattern.compile("^" +
//                    "(?=.*[0-9])" +
//                    "(?=.*[a-zA-Z])" +
//                    "(?=\\S+$)" +
//                    ".{4,}" +
//                    "$");
//    String akses_pengguna, randomIdGroup;
//    TextInputEditText daftar_username, daftar_email, daftar_password, daftar_password_ulangi;
//
//    Button lanjut, pindahSignIn;
    FirebaseAuth firebaseAuth;
//
//    LinearLayout progressBar;


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(SignUpActivity.this, "Anda Telah Login", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                }
            }, 1000);


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

        setContentView(R.layout.activity_signup);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_signup, new Fragment1()).commit();

//        daftar_username = findViewById(R.id.daftar_username);
//        daftar_email = findViewById(R.id.daftar_email);
//        daftar_password = findViewById(R.id.daftar_password);
//        daftar_password_ulangi = findViewById(R.id.daftar_password_ulangi);
//        akses_pengguna = "";
//        Personal = findViewById(R.id.radioPersonal);
//        Admin = findViewById(R.id.radioAdmin);
//        Member = findViewById(R.id.radioMember);

//        progressBar = findViewById(R.id.progressBar);
//        radioGroup = findViewById(R.id.radioGroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (i) {
//                    case R.id.radioPersonal:
//
//                        akses_pengguna = "Personal";
//                        break;
//
//                    case R.id.radioAdmin:
//
//                        akses_pengguna = "Admin";
//                        break;
//
//                    case R.id.radioMember:
//
//                        akses_pengguna = "Member";
//                        break;
//                }
//            }
//        });

//        lanjut = findViewById(R.id.button_daftar);
//        lanjut.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                validasi();
//
//            }
//        });
//
//        pindahSignIn = findViewById(R.id.pindah_signin);
//        pindahSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//
//            }
//        });
    }

//    public void validasi() {
//
//
//        final String username_daftar = Objects.requireNonNull(daftar_username.getText()).toString().trim();
//        final String email_daftar = Objects.requireNonNull(daftar_email.getText()).toString().trim();
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        final String password_daftar = Objects.requireNonNull(daftar_password.getText()).toString().trim();
//        String ulangi_password_daftar = Objects.requireNonNull(daftar_password_ulangi.getText()).toString().trim();
//
//        if (username_daftar.isEmpty() && email_daftar.isEmpty() && password_daftar.isEmpty() && ulangi_password_daftar.isEmpty()) {
//
//            daftar_username.setError("Username harus di isi");
//            daftar_email.setError("Email harus di isi");
//            daftar_password.setError("Password harus di isi");
//            daftar_password_ulangi.setError("Ulangi password harus di isi");
//
//        } else if (username_daftar.isEmpty()) {
//
//            daftar_username.setError("Username harus di isi");
//
//        } else if (email_daftar.isEmpty()) {
//
//            daftar_email.setError("Email harus di isi");
//
//        } else if (!email_daftar.matches(emailPattern)) {
//
//            daftar_email.setError("Email harus di isi dengan format yang benar");
//
//        } else if (password_daftar.isEmpty()) {
//
//            daftar_password.setError("Password harus di isi");
//
//        } else if (!PASSWORD_PATTERN.matcher(password_daftar).matches()) {
//
//            daftar_password.setError("Password harus memiliki setidaknya 6 karakter kombinasi text dan angka dan harus tidak memiliki karakter whitespace");
//            Toast.makeText(SignUpActivity.this, "Password harus memiliki setidaknya 6 karakter kombinasi text dan angka dan harus tidak memiliki karakter whitespace", Toast.LENGTH_LONG).show();
//
//        } else if (ulangi_password_daftar.isEmpty()) {
//
//            daftar_password_ulangi.setError("Ulangi password harus di isi");
//
//        } else if (!ulangi_password_daftar.equals(password_daftar)) {
//
//            daftar_password_ulangi.setError("Ulangi password harus sama dengan password");
//
//        }
//        randomIdGroup = "";
////
//        disabledAction();
//        firebaseAuth.createUserWithEmailAndPassword(email_daftar, password_daftar).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if (task.isSuccessful()) {
//
//                    Setter_user_more userMore = new Setter_user_more(username_daftar, email_daftar, akses_pengguna, randomIdGroup);
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    String userId = Objects.requireNonNull(user).getUid();
//
//                    firestore.collection("Users")
//                            .document(userId)
//                            .set(userMore).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if (task.isSuccessful()) {
//
//                                Toast.makeText(SignUpActivity.this, "Proses pendaftaran berhasil", Toast.LENGTH_LONG).show();
//                                Toast.makeText(SignUpActivity.this, "Anda Telah Login!", Toast.LENGTH_SHORT).show();
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        progressBar.setVisibility(View.GONE);
//                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                        startActivity(intent);
////                                                    unregisterReceiver(screenTimeService);
//                                        finish();
//
//                                    }
//                                }, 1000);
//
//                            } else {
//
//                                Toast.makeText(SignUpActivity.this, "Proses pendaftaran gagal, mohon coba lagi", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
//
//                } else {
//
//                    enabledAction();
//                    Toast.makeText(SignUpActivity.this, "Gagal melakukan registrasi, email yang anda masukkan telah terdaftar", Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });


//        else {
//
//            switch (akses_pengguna) {
//
//                case "Personal":
//
//                    randomIdGroup = "";
//
//                    Toast.makeText(SignUpActivity.this, "Anda memilih akses pengguna Personal, silahkan tunggu proses pendaftaran", Toast.LENGTH_LONG).show();
//                    disabledAction();
//                    firebaseAuth.createUserWithEmailAndPassword(email_daftar, password_daftar).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//                            if (task.isSuccessful()) {
//
//                                Setter_user_more userMore = new Setter_user_more(username_daftar, email_daftar, akses_pengguna, randomIdGroup);
//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                String userId = Objects.requireNonNull(user).getUid();
//
//                                firestore.collection("Users")
//                                        .document(userId)
//                                        .set(userMore).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//
//                                            Toast.makeText(SignUpActivity.this, "Proses pendaftaran berhasil", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(SignUpActivity.this, "Anda Telah Login!", Toast.LENGTH_SHORT).show();
//
//                                            new Handler().postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    progressBar.setVisibility(View.GONE);
//                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                                    startActivity(intent);
////                                                    unregisterReceiver(screenTimeService);
//                                                    finish();
//
//                                                }
//                                            }, 1000);
//
//                                        } else {
//
//                                            Toast.makeText(SignUpActivity.this, "Proses pendaftaran gagal, mohon coba lagi", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    }
//                                });
//
//                            } else {
//
//                                enabledAction();
//                                Toast.makeText(SignUpActivity.this, "Gagal melakukan registrasi, email yang anda masukkan telah terdaftar", Toast.LENGTH_LONG).show();
//
//                            }
//                        }
//                    });
//                    break;
//
//                case "Admin":
//
//                    randomIdGroup = random_string();
//
//                    Toast.makeText(SignUpActivity.this, "Anda memilih akses pengguna Admin", Toast.LENGTH_LONG).show();
//                    disabledAction();
//
//                    firebaseAuth.createUserWithEmailAndPassword(email_daftar,password_daftar).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//                            if (task.isSuccessful()) {
//
//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                String userId = Objects.requireNonNull(user).getUid();
//                                Setter_user_more userMore = new Setter_user_more(username_daftar, email_daftar, akses_pengguna, randomIdGroup);
//                                Setter_group_user groupMore = new Setter_group_user(userId, username_daftar, akses_pengguna);
//
//                                firestore.collection("Groups")
//                                        .document(randomIdGroup)
//                                        .collection("Member")
//                                        .document(userId)
//                                        .set(groupMore);
//                                firestore.collection("Users")
//                                        .document(userId)
//                                        .set(userMore).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//
//                                            Toast.makeText(SignUpActivity.this, "Proses pendaftaran berhasil", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(SignUpActivity.this, "Anda Telah Login!", Toast.LENGTH_SHORT).show();
//
//                                            new Handler().postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    enabledAction();
//                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                                    startActivity(intent);
////                                                    unregisterReceiver(screenTimeService);
//                                                    finish();
//
//                                                }
//                                            }, 1000);
//
//                                        } else {
//                                            enabledAction();
//                                            Toast.makeText(SignUpActivity.this, "Proses pendaftaran gagal, mohon coba lagi", Toast.LENGTH_LONG).show();
//
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                    break;
//
//                case "Member":
//
//                    Toast.makeText(SignUpActivity.this, "Anda memilih akses pengguna Member", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(SignUpActivity.this, Member_verification_activity.class);
//                    intent.putExtra("username_daftar", username_daftar);
//                    intent.putExtra("email_daftar", email_daftar);
//                    intent.putExtra("password_daftar", password_daftar);
//                    intent.putExtra("akses_pengguna_daftar", akses_pengguna);
//                    startActivity(intent);
//                    break;
//
//                case "":
//
//                    Toast.makeText(this, "Anda harus memilih akses penggunaan", Toast.LENGTH_SHORT).show();
//
//            }
//
//        }

//    }
//PENTING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    public static String random_string() {
//
//        String saltChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random random = new Random();
//        while (salt.length() < 10) {
//
//            int index = (int) (random.nextFloat() * saltChar.length());
//            salt.append(saltChar.charAt(index));
//        }
//
//        return salt.toString();
//    }

//    public void disabledAction() {
//        progressBar.setVisibility(View.VISIBLE);
//        daftar_username.setEnabled(false);
//        daftar_email.setEnabled(false);
//        daftar_password.setEnabled(false);
//        daftar_password_ulangi.setEnabled(false);
//        lanjut.setEnabled(false);
//        pindahSignIn.setEnabled(false);
//    }
//
//    public void enabledAction() {
//        progressBar.setVisibility(View.GONE);
//        daftar_username.setEnabled(true);
//        daftar_email.setEnabled(true);
//        daftar_password.setEnabled(true);
//        daftar_password_ulangi.setEnabled(true);
//        lanjut.setEnabled(true);
//        pindahSignIn.setEnabled(true);
//    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

