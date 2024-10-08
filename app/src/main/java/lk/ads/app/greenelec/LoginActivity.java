package lk.ads.app.greenelec;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private TextView gotoRegister,gotoHome;
    private EditText emailText,passwordText;
    private Button loginBtn,googleLoginBtn,facebookLoginBtn;
    private FirebaseAuth firebaseAuth;
    boolean isAllFieldsChecked = false;
    private NotificationManager notificationManager;
    private String channelId = "info";
    private SignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.loginEmail);
        passwordText = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.LoginButton);
        gotoRegister = findViewById(R.id.gotoRegister);
        gotoHome = findViewById(R.id.gotoHome);
        googleLoginBtn = findViewById(R.id.login_with_google);

        firebaseAuth = FirebaseAuth.getInstance();
        signInClient = Identity.getSignInClient(getApplicationContext());

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"INFO",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.setDescription("This is Information Channel");
            channel.setVibrationPattern(new long[]{0,1000,1000,1000});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R
                        .anim.animation);
                loginBtn.startAnimation(animation);

                isAllFieldsChecked = CheckAllFields();

                if (isAllFieldsChecked){
                    String email = emailText.getText().toString();
                    String password = passwordText.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.ic_stat_name)
                                                .setContentTitle("Greenelec Notification")
                                                .setContentText("You Login Successfully!")
                                                .setColor(Color.rgb(23,165,137))
                                                .build();

                                        notificationManager.notify(1,notification);

                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("email",firebaseUser.getEmail());
                                        editor.commit();

                                        updateUI(firebaseUser);
                                    }
                                }
                            });
                }
            }
        });

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.web_client_id)).build();

                Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
                signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
                        signInLauncher.launch(intentSenderRequest);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R
                        .anim.animation);
                gotoRegister.startAnimation(animation);

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R
                        .anim.animation);
                gotoHome.startAnimation(animation);

                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateUI(FirebaseUser user){
        if (user != null){
            if (!user.isEmailVerified()){
                Toast.makeText(LoginActivity.this, "Please Verify Your Email",Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private boolean CheckAllFields() {

        if (emailText.length() == 0) {
            emailText.setError("Email is required");
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
    private void changeUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Greenelec Notification")
                        .setContentText("You Login Successfully!")
                        .setColor(Color.rgb(23,165,137))
                        .build();

                notificationManager.notify(1,notification);

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email",firebaseUser.getEmail());
                editor.commit();
                changeUI(firebaseUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void handleSignInResult(Intent intent){
        try {
            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);
        }catch (ApiException e){

        }
    }
    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            handleSignInResult(o.getData());
                        }
                    });
}