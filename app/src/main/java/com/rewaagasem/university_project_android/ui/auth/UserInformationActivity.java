package com.rewaagasem.university_project_android.ui.auth;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rewaagasem.university_project_android.databinding.ActivityUserInformationBinding;
import com.rewaagasem.university_project_android.model.BMIRecord;
import com.rewaagasem.university_project_android.model.User;
import com.rewaagasem.university_project_android.ui.Main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserInformationActivity extends AppCompatActivity {


    ActivityUserInformationBinding binding;
    private Calendar calendar;
    private int year, month, day;
    String gender = "";
    User mUser;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users").child(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);


        initUI();



        binding.saveData.setOnClickListener(v->{

            if(binding.female.isChecked()){
                gender = "Female";
            }else {
                gender = "Male";
            }
            mUser.setGender(gender);
            mUser.setDob(binding.dateOfBirth.getText().toString());
            mUser.setWeight(binding.weight.getText().toString());
            mUser.setLength(binding.length.getText().toString());

            calculateBMI();

            myRef.setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(UserInformationActivity.this, "Information saved successful", Toast.LENGTH_SHORT).show();

                }
            });

            });


    }


    private void calculateBMI(){
        Date c = Calendar.getInstance().getTime();
//        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date = df.format(c);

        double bmi = 0 ;
        double weight = Double.valueOf(binding.weight.getText().toString());
        double length = Double.valueOf(binding.length.getText().toString());

        double age = getAge(binding.dateOfBirth.getText().toString());
        double age_percent = 0.7;

        if ( age >= 2 && age <= 10){
            age_percent = 0.7;
        }else if( (age > 10 && age <= 20) && gender.equals("Male")){
            age_percent = 0.9;
        }else if( (age > 10 && age <= 20) && gender.equals("Female")){
            age_percent = 0.9;
        }else if (age > 20 ){
            age_percent = 1;
        }else{
            Toast.makeText(UserInformationActivity.this, "UnSupported Age", Toast.LENGTH_SHORT).show();
            return;
        }

        bmi = (weight / (length * length)) * age_percent;

        String status = "";

        if(bmi < 18.5){
            status = "Underweight";
            status =  checkStatus(status,bmi);
        }else if( bmi >= 18.5 && bmi < 25){
            status = "Healthy Weight";
            status =  checkStatus(status,bmi);
        }else if( bmi >= 25 && bmi < 30){
            status = "Overweight";
            status =  checkStatus(status,bmi);
        }else if (bmi >= 30){
            status = "Obesity";
            status =  checkStatus(status,bmi);
        }

        String bmi_id=new Date().getTime()+"";
        DatabaseReference bmiRef = database.getReference("BMI_Rec").child(user.getUid()).child(bmi_id);
        BMIRecord bmiRecord = new BMIRecord();
        bmiRecord.setBMI(bmi+"");
        bmiRecord.setUID(user.getUid());
        bmiRecord.setDate(date);
        bmiRecord.setLength(binding.length.getText().toString());
        bmiRecord.setWeight(binding.weight.getText().toString());
        bmiRecord.setStatus(status);
        bmiRecord.setIs_last(true);
        bmiRef.setValue(bmiRecord);
    }

    private void initUI() {

        binding.dateOfBirth.setOnClickListener(v->{
            setDate();
        });

        binding.addWeight.setOnClickListener(v->{
            if(!binding.weight.getText().toString().isEmpty()){
                double current_weight = Double.valueOf(binding.weight.getText().toString());
                binding.weight.setText((current_weight + 1) + "");
            }
        });

        binding.minusWeight.setOnClickListener(v->{
            if(!binding.weight.getText().toString().isEmpty()){
                double current_weight = Double.valueOf(binding.weight.getText().toString());
                if(current_weight != 0){
                    binding.weight.setText((current_weight - 1) + "");
                }
            }
        });

        binding.addLength.setOnClickListener(v->{
            if(!binding.length.getText().toString().isEmpty()){
                double current_length = Double.valueOf(binding.length.getText().toString());
                binding.length.setText((current_length + 1) + "");
            }
        });

        binding.minusLength.setOnClickListener(v->{
            if(!binding.length.getText().toString().isEmpty()){
                double current_length = Double.valueOf(binding.length.getText().toString());
                if(current_length != 0){
                    binding.length.setText((current_length - 1) + "");
                }
            }
        });


    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) ->
            showDate(arg1, arg2+1, arg3);

    private void showDate(int year, int month, int day) {
        binding.dateOfBirth.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private String checkStatus(String status , double bmi){
        double delta = bmi - 0;
        switch (status){
            case "Underweight":
                if (delta < -1){
                    status += " (So Bad)";
                }else if( delta >= -1 && delta < -0.6){
                    status += " (So Bad)";
                }
                else if( delta >= -0.6 && delta < -0.3){
                    status += " (So Bad)";
                }else if( delta >= -0.3 && delta < 0){
                    status += " (Little Change)";
                }else if( delta >= 0 && delta < 0.3){
                    status += " (Little Change)";
                }else if( delta >= 0.3 && delta < 0.6){
                    status += " (Still Good)";
                }else if( delta >= 0.6 && delta < 1){
                    status += " (Go Ahead)";
                }else if( delta >= 1 ){
                    status += " (Go Ahead)";
                }
                break;
            case "Healthy Weight":
                if (delta < -1){
                    status += " (So Bad)";
                }else if( delta >= -1 && delta < -0.6){
                    status += " (Be Careful)";
                }
                else if( delta >= -0.6 && delta < -0.3){
                    status += " (Be Careful)";
                }else if( delta >= -0.3 && delta < 0){
                    status += " (Little Change)";
                }else if( delta >= 0 && delta < 0.3){
                    status += " (Little Change)";
                }else if( delta >= 0.3 && delta < 0.6){
                    status += " (Be Careful)";
                }else if( delta >= 0.6 && delta < 1){
                    status += " (Be Careful)";
                }else if( delta >= 1 ){
                    status += " (Be Careful)";
                }
                break;
            case "Overweight":
                if (delta < -1){
                    status += " (Be Careful)";
                }else if( delta >= -1 && delta < -0.6){
                    status += " (Go Ahead)";
                }
                else if( delta >= -0.6 && delta < -0.3){
                    status += " (Still Good)";
                }else if( delta >= -0.3 && delta < 0){
                    status += " (Little Change)";
                }else if( delta >= 0 && delta < 0.3){
                    status += " (Little Change)";
                }else if( delta >= 0.3 && delta < 0.6){
                    status += " (Be Careful)";
                }else if( delta >= 0.6 && delta < 1){
                    status += " (So Bad)";
                }else if( delta >= 1 ){
                    status += " (So Bad)";
                }
                break;
            case "Obesity":
                if (delta < -1){
                    status += " (Go Ahead)";
                }else if( delta >= -1 && delta < -0.6){
                    status += " (Go Ahead)";
                }
                else if( delta >= -0.6 && delta < -0.3){
                    status += " (Little Change)";
                }else if( delta >= -0.3 && delta < 0){
                    status += " (Little Change)";
                }else if( delta >= 0 && delta < 0.3){
                    status += " (Be Careful)";
                }else if( delta >= 0.3 && delta < 0.6){
                    status += " (So Bad)";
                }else if( delta >= 0.6 && delta < 1){
                    status += " (So Bad)";
                }else if( delta >= 1 ){
                    status += " (So Bad)";
                }
                break;
        }


        return status;
    }

    private double getAge(String date){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String dateToday = df.format(c);
        Date date1 = new Date(binding.dateOfBirth.getText().toString());
        Date date2 = new Date(dateToday);

        long diff = date2.getTime() - date1.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        double age = days / 365.0 ;
        return age;
    }

}