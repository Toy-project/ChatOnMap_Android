package com.example.toyproject.nearendipity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signup_btn;
    private EditText email_edit, password_edit, name_edit, passwordconfirm_edit; //회원가입용
/****************************
   private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
   private DatabaseReference databaseReference = firebaseDatabase.getReference();
******************************/
    private boolean register_db=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        signup_btn = findViewById(R.id.buttonSignUp);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_edit = (EditText)findViewById(R.id.editTextEmail);
                password_edit = (EditText)findViewById(R.id.editTextPassword);
                passwordconfirm_edit = (EditText)findViewById(R.id.editTextPasswordConfirm);
                name_edit = (EditText)findViewById(R.id.editTextName);

                //edit를 string으로 변환
                String Semail = email_edit.getText().toString();
                String Spassword = password_edit.getText().toString();
                String Spasswordconfirm = passwordconfirm_edit.getText().toString();
                String Sname = name_edit.getText().toString();

               createAccount(Sname,Semail,Spassword,Spasswordconfirm);

               if(register_db==true)
               {
                   //writeNewUser(Semail,Sname);
                   register_db=false;
               }
            }
        });
    }

//비밀번호 6자리 이상 한글미포함
    private boolean isValidPasswd(String target) {
        Pattern p = Pattern.compile("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)");

        Matcher m = p.matcher(target);

        if (m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
            return true;
        }else{
            return false;
        }
    }
//이메일 유효성 검사
    private boolean isValidEmail(String target) {

        if (target == null || TextUtils.isEmpty(target)){
            return false;
        }else{
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

//데이터베이스에 저장
/****************************
private void writeNewUser(String email, String name) {


    databaseReference.child("users").child(email).setValue(name);
}
 ******************************/




    //회원가입
    private void createAccount(String name, String email, String password, String passwordconfirm) {

        // 1,2차 비밀번호 동일검사 실행
        if(password.equals(passwordconfirm))
        {

        } else {
            Toast.makeText(RegisterActivity.this, "비밀번호가 같지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
        //이메일 유효성 검사 실행
        if(!isValidEmail(email)){
           // Log.e(TAG, "createAccount: email is not valid ");
            Toast.makeText(RegisterActivity.this, "Email이 유효하지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 비밀번호 6자리 이상 한글미포함 검사
        if (isValidPasswd(password)){
            //Log.e(TAG, "createAccount: password is not valid ");
            Toast.makeText(RegisterActivity.this, "Password의 형식이 맞지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

//계정 생성 시작

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
                            register_db = true; // 성공시 true 저장 (db에 등록하기위함)
                            return;
                        } else {
                            Toast.makeText(RegisterActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                            return;
                        }



                    }
                });


    }





}
