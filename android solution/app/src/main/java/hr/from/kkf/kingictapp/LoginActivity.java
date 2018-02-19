package hr.from.kkf.kingictapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.teamNameLogin) EditText teamName;
    @BindView(R.id.teamPasswordLogin) EditText teamPassword;
    @BindView(R.id.resultText) TextView resultText;
    @BindView(R.id.resultBody) TextView resultBody;
    @BindView(R.id.checkDetailsButton) Button detailsButton;

    private String token;
    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());
    }

    @OnClick(R.id.login)
    public void sendRequest(){
        Map<String, String> data = new HashMap<>();
        data.put("Teamname", teamName.getText().toString());
        data.put("Password", teamPassword.getText().toString());


        AndroidNetworking.post("http://52.233.158.172/change/api/hr/account/login")
                .addJSONObjectBody(new JSONObject(data))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONObject object = new JSONObject(response.getString("Result"));

                            String nextLink = object.getString("NextApi");
                            token = object.getString("AuthorizationToken");
                            teamId = object.getString("TeamId");

                            resultText.setVisibility(View.VISIBLE);
                            resultBody.setText(nextLink);
                            detailsButton.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    @OnClick(R.id.checkDetailsButton)
    public void checkDetails(){
        Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("token", token);

        startActivity(intent);
    }
}
