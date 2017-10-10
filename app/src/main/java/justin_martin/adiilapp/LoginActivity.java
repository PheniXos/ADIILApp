package justin_martin.adiilapp;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText emailInputLogin;
    private EditText passwordInputLogin;

    private Button connexionButtonLogin;
    private TextView forgetPasswordLogin ;

    private String snackbarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInputLogin = (EditText) findViewById(R.id.emailInputLogin);
        passwordInputLogin = (EditText) findViewById(R.id.passwordInputLogin);

        connexionButtonLogin = (Button) findViewById(R.id.connexionButtonLogin);
        forgetPasswordLogin = (TextView) findViewById(R.id.forgetPasswordLogin);

        connexionButtonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                signin(emailInputLogin.getText().toString(), passwordInputLogin.getText().toString());

            }
        });

        forgetPasswordLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                    resetPassword(emailInputLogin.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("user auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("user deco", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        passwordInputLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    signin(emailInputLogin.getText().toString(), passwordInputLogin.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    private void signin(String email, String password) {
        if(email.isEmpty() ||password.isEmpty()) {
            snackbarText = getString(R.string.EmailOrPasswordEmpty);
            showSnackBar();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    snackbarText = getString(R.string.AuthMailError);
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    snackbarText = getString(R.string.AuthPasswordError);
                                } catch (Exception e) {
                                    snackbarText = e.getMessage();
                                }
                                showSnackBar();
                            }
                        }
                    });
        }
    }

    private void showSnackBar() {
        Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content),
                snackbarText, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    private void resetPassword(String email) {
        if(email.isEmpty()) {
            snackbarText = getString(R.string.EmailEmpty);
            showSnackBar();
        }
        else {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                snackbarText = getString(R.string.ResetPasswordSent);
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    snackbarText = getString(R.string.ResetPasswordMailError);
                                } catch (Exception e) {
                                    snackbarText = e.getMessage();
                                }
                            }
                            showSnackBar();
                        }
                    });
        }
    }

}
