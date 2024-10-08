package lk.ads.app.greenelec;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.ads.app.greenelec.model.User;

public class RegisterFragment extends Fragment {
    public static final String TAG = MainActivity.class.getName();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View registerFragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(registerFragment, savedInstanceState);
        TextView loginNow = registerFragment.findViewById(R.id.gotoLogin);
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, loginFragment);
                fragmentTransaction.commit();
            }
        });

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        EditText firstnameText = registerFragment.findViewById(R.id.firstNameText);
        EditText lastnameText = registerFragment.findViewById(R.id.lastNameText);
        EditText emailAddressText = registerFragment.findViewById(R.id.emailAddressText);
        EditText phoneText = registerFragment.findViewById(R.id.phoneText);
        EditText passwordText = registerFragment.findViewById(R.id.registerPassword);

        Button registerBtn = registerFragment.findViewById(R.id.registerbtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = firstnameText.getText().toString();
                String lastname = lastnameText.getText().toString();
                String email = emailAddressText.getText().toString();
                String mobile = phoneText.getText().toString();
                String password = passwordText.getText().toString();

                User user = new User(firstname,lastname,email,password,mobile);

                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.i(TAG,"CreateUserWithEmailAndPassword:Success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    updateUI(user);
                                    Toast.makeText(getActivity(), "Please Verify Your Email", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }else {
                            Log.w(TAG, "CreateUserWithEmailAndPassword:Failed");
                            Toast.makeText(getActivity(),"Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            if (!user.isEmailVerified()) {
                Toast.makeText(getActivity(), "Please Verify Your Email", Toast.LENGTH_LONG).show();
                //return;
            }

            LoginFragment loginFragment = new LoginFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, loginFragment);
            fragmentTransaction.commit();
        }
    }
}
