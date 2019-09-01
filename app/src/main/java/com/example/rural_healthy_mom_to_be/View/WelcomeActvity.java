package com.example.rural_healthy_mom_to_be.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.MainActivity;
import com.example.rural_healthy_mom_to_be.R;

public class WelcomeActvity extends AppCompatActivity {
    private CheckBox checkBoxTNC;
    private TextView textViewErrorMessage;
    private TextView strTNC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_actvity);
        checkBoxTNC = (CheckBox)findViewById(R.id.checkboxTNC);
        textViewErrorMessage = (TextView) findViewById(R.id.textViewError);
        strTNC = findViewById(R.id.tvTNC);

        strTNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the object of
                // AlertDialog Builder class
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(WelcomeActvity.this);
                String getTNC = getApplicationContext().getResources().getString(R.string.tncContent);
                // Set TNC message
                builder.setMessage(getTNC);

                // Set the Title
                builder.setTitle("Term and Conditions");
                builder.setCancelable(true);

                builder
                        .setNegativeButton(
                                "Confirm",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        dialog.cancel();
                                    }
                                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();
            }
        });


        strTNC.setMovementMethod(LinkMovementMethod.getInstance());
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
