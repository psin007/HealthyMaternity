package com.example.rural_healthy_mom_to_be.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.R;

public class WelcomeActvity extends AppCompatActivity {
    private CheckBox checkBoxTNC;
    private TextView textViewErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_actvity);
        checkBoxTNC = (CheckBox)findViewById(R.id.checkboxTNC);
        textViewErrorMessage = (TextView) findViewById(R.id.textViewError);
    }

    // when welcome button is clicked
    public void welcomeClicked(View view){
        if(((checkBoxTNC)).isChecked()){
            Intent intent = new Intent(WelcomeActvity.this, FormActivity.class);
            startActivity(intent);
            //ToDo: RR added this line to remove the WelcomeActivity from back stack.
            finish();
        }
        else{
            checkBoxTNC.setError("Read and accept our terms first!");

        }
    }
}
