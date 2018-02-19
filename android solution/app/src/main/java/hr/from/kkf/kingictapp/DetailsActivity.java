package hr.from.kkf.kingictapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class DetailsActivity extends AppCompatActivity {

    private String teamId;
    private String token;

    @BindView(R.id.teamNameDetails) EditText teamName;
    @BindView(R.id.resultTextDetails) TextView resultText;
    @BindView(R.id.resultBodyDetails) TextView resultBody;
    @BindView(R.id.confirm) EditText confirm;

    @BindView(R.id.name1Details) EditText name1;
    @BindView(R.id.surname1Details) EditText surname1;
    @BindView(R.id.email1Details) EditText email1;

    @BindView(R.id.name2Details) EditText name2;
    @BindView(R.id.surname2Details) EditText surname2;
    @BindView(R.id.email2Details) EditText email2;

    @BindView(R.id.name3Details) EditText name3;
    @BindView(R.id.surname3Details) EditText surname3;
    @BindView(R.id.email3Details) EditText email3;

    @BindView(R.id.name4Details) EditText name4;
    @BindView(R.id.surname4Details) EditText surname4;
    @BindView(R.id.email4Details) EditText email4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());
        performRequest();
    }

    private void performRequest() {
        Intent intent = getIntent();
        teamId = intent.getStringExtra("teamId");
        token = intent.getStringExtra("token");

        AndroidNetworking.get("http://52.233.158.172/change/api/hr/team/details/{id}")
                .addPathParameter("id", teamId)
                .addHeaders("X-Authorization", token)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.getString("Result"));

                            parseJsonObject(object);
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

    private void parseJsonObject(JSONObject object) throws JSONException {
        resultText.setVisibility(View.VISIBLE);
        resultBody.setText(object.getString("NextApi"));
        teamName.setText(object.getString("TeamName"));

        JSONArray membersObject = object.getJSONArray("Members");

        JSONObject first = membersObject.getJSONObject(0);
        name1.setText(first.getString("Name"));
        surname1.setText(first.getString("Surname"));
        email1.setText(first.getString("Mail"));

        JSONObject second = membersObject.getJSONObject(1);
        name2.setText(second.getString("Name"));
        surname2.setText(second.getString("Surname"));
        email2.setText(second.getString("Mail"));

        JSONObject third = membersObject.getJSONObject(2);
        name3.setText(third.getString("Name"));
        surname3.setText(third.getString("Surname"));
        email3.setText(third.getString("Mail"));

        JSONObject fourth = membersObject.getJSONObject(3);
        name4.setText(fourth.getString("Name"));
        surname4.setText(fourth.getString("Surname"));
        email4.setText(fourth.getString("Mail"));
    }

    @OnClick(R.id.confirmButton)
    public void clickConfirm(){
        if (confirm.getText().toString().isEmpty()) {
            Toast.makeText(DetailsActivity.this, "You did not enter a repository link", Toast.LENGTH_SHORT).show();
            return;
        }

        AndroidNetworking.get("http://52.233.158.172/change/api/hr/team/confirm?id={id}&repository={repository}")
                .addPathParameter("id", teamId)
                .addPathParameter("repository", confirm.getText().toString())
                .addHeaders("X-Authorization", token)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            Toast.makeText(DetailsActivity.this, "Failed: " + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetailsActivity.this, "Confirmed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //treba nekako ovo gore poslati i provjeriti jel vraceno 200 OK
    }
}
