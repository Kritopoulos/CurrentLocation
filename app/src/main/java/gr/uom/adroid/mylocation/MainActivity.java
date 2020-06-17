package gr.uom.adroid.mylocation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginButton loginButton;
    Button continuebtn;
    EditText passwordTXT,mailTXT;

    String mybool ="false";
    String user_id;
    TextView statusTXT;
    int PERMISSION_ALL = 97;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    //firebase
    private FirebaseAuth anAuth;
    private  FirebaseAuth.AuthStateListener FirebaseAuthListener;
    private static final String EMAIL = "email";


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passwordTXT = findViewById(R.id.passwordTXT);
        mailTXT = findViewById(R.id.mailTXT);
        statusTXT = findViewById(R.id.statusTXT);
        if(user_id == null){
            statusTXT.setText("Disconected");
        }

        anAuth = FirebaseAuth.getInstance();
        FirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    user_id = anAuth.getCurrentUser().getUid();
                    Log.d("KAPPA", "user id "+user_id);
                    setLogedInOrNot("true");
                    statusTXT.setText("Conected");
                }
            }
        };

        if(!hasPermissions(MainActivity.this, PERMISSIONS ) ){
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        //FACEBOOK LOG IN
            fbLogInBTN();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registerBTN(View v){
        String mail = mailTXT.getText().toString();
        String password = passwordTXT.getText().toString();
        if (mail.equals(null) || password.equals(null)){
            Toast.makeText(MainActivity.this,"Empty field",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("MAIL", "loginBTN: " + mail + password);
            anAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sing up erro", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Succsfuly sign up", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void loginBTN(View v){
        String mail = mailTXT.getText().toString();
        String password = passwordTXT.getText().toString();
        if (mail == null || password == null){
            Toast.makeText(MainActivity.this,"Empty field",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("MAIL", "loginBTN: " + mail + password);
            anAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sing in erro", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Succsfuly loged in", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }
    public void continueBTN(View v){
        if(hasPermissions(MainActivity.this, PERMISSIONS ) ){
            Intent newIntent = new Intent(MainActivity.this, MainPageActivity.class);

            newIntent.putExtra("ACCESS",mybool);
            newIntent.putExtra("ID",user_id);
            startActivity(newIntent);
        }
        else{
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("You need to add all the permissions to move further")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    public void fbLogInBTN(){
        loginButton=findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Successfully loged in!",
                        Toast.LENGTH_SHORT).show();
                statusTXT.setText("Conected");
            }
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Log in failed!",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "An error was occured!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        anAuth.addAuthStateListener(FirebaseAuthListener);
    }
    protected void onStop() {
        super.onStop();
        anAuth.removeAuthStateListener(FirebaseAuthListener);
    }

    public void setLogedInOrNot(String bool){
            mybool = bool;
    }

    public void logOutBTN(View v){
        FirebaseAuth.getInstance().signOut();
        setLogedInOrNot("false");
        statusTXT.setText("Disconected");

    }
}
