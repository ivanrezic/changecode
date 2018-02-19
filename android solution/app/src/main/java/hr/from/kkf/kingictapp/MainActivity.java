package hr.from.kkf.kingictapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.registrationButton)
    public void goToRegisterActivity(){
        startActivity(new Intent(getBaseContext(), RegisterActivity.class));
    }

    @OnClick(R.id.loginButton)
    public void goToLoginActivity(){
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }
}
