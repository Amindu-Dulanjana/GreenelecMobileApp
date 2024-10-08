package lk.ads.app.greenelec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import lk.ads.app.greenelec.model.User;

public class RegisterActivity extends AppCompatActivity {
    private TextView gotoLogin;
    private EditText firstnameText,lastnameText,emailText,mobileText,passwordText;
    private String firstname,lastname,email,mobile,password;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstnameText = findViewById(R.id.registerFirstname);
        lastnameText = findViewById(R.id.registerLastname);
        emailText = findViewById(R.id.registerEmail);
        mobileText = findViewById(R.id.registerMobile);
        passwordText = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        gotoLogin = findViewById(R.id.gotoLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R
                        .anim.animation);
                registerButton.startAnimation(animation);

                isAllFieldsChecked = CheckAllFields();

                if (isAllFieldsChecked) {
                    firstname = firstnameText.getText().toString();
                    lastname = lastnameText.getText().toString();
                    email = emailText.getText().toString();
                    mobile = mobileText.getText().toString();
                    password = passwordText.getText().toString();

                    User user = new User(firstname,lastname,email,mobile,password);

                    fireStore.collection("users").whereEqualTo("email",email)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        QuerySnapshot result = task.getResult();
                                        if (result.isEmpty()){
                                            fireStore.collection("users").add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            firebaseAuth.createUserWithEmailAndPassword(email,password)
                                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                                            firebaseUser.sendEmailVerification();
                                                                            updateUI(firebaseUser);
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R
                        .anim.animation);
                gotoLogin.startAnimation(animation);

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateUI(FirebaseUser user){
        if (user != null){
            if (!user.isEmailVerified()){
                Toast.makeText(RegisterActivity.this, "Please Verify Your Email",Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean CheckAllFields() {
        if (firstnameText.length() == 0) {
            firstnameText.setError("First Name is required");
            return false;
        }

        if (lastnameText.length() == 0) {
            lastnameText.setError("Last Name is required");
            return false;
        }

        if (emailText.length() == 0) {
            emailText.setError("Email is required");
            return false;
        }

        if (mobileText.length() == 0){
            mobileText.setError("Mobile is required");
            return false;
        } else if (mobileText.length() != 10){
            mobileText.setError("Mobile number must be 10 numbers");
            return false;
        }

        if (passwordText.length() == 0) {
            passwordText.setError("Password is required");
            return false;
        } else if (passwordText.length() < 6) {
            passwordText.setError("Password must be minimum 8 characters");
            return false;
        }
        return true;
    }
}