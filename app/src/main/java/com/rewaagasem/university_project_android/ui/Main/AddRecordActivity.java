package com.rewaagasem.university_project_android.ui.Main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rewaagasem.university_project_android.databinding.ActivityAddRecordBinding;
import com.rewaagasem.university_project_android.firebase.MessageListener;
import com.rewaagasem.university_project_android.firebase.MyFirebaseProvider;
import com.rewaagasem.university_project_android.model.BMIRecord;
import com.rewaagasem.university_project_android.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddRecordActivity extends AppCompatActivity {


    User mUser ;
    ActivityAddRecordBinding binding;
    private Calendar calendar;
    private int year, month, day;
    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference recordsDBRef, userDBref;
    final List<BMIRecord> my_records = new ArrayList<>();
     double last_record = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        initDBRef();
        setData();


    }


    private void initDBRef() {
        recordsDBRef = MyFirebaseProvider.getChildDatabaseReference("BMI_Rec");

        recordsDBRef.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userDBref = MyFirebaseProvider.getChildDatabaseReference("Users").child(user.getUid());
        userDBref.keepSynced(true);
        userDBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void setData() {
        my_records.clear();
        MyFirebaseProvider.getChildDatabaseReference("BMI_Rec").child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        my_records.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            BMIRecord c = snapshot1.getValue(BMIRecord.class);
                            my_records.add(c);
                        }

                        if(my_records.size()==0){
                            last_record=0;

                        }else{
                            last_record = Double.valueOf(my_records.get(my_records.size()-1).getBMI());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void initUI() {

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        binding.date.setOnClickListener(v->{
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

        binding.saveRecord.setOnClickListener(v->{
                double bmi = 0 ;
                double weight = Double.valueOf(binding.weight.getText().toString());
                double length = Double.valueOf(binding.length.getText().toString());

                double age = getAge(mUser.getDob());
                double age_percent = 0.7;

                if ( age >= 2 && age <= 10){
                    age_percent = 0.7;
                }else if( (age > 10 && age <= 20) && mUser.getGender().equals("Male")){
                    age_percent = 0.9;
                }else if( (age > 10 && age <= 20) && mUser.getGender().equals("Female")){
                    age_percent = 0.9;
                }else if (age > 20 ){
                    age_percent = 1;
                }else{
                    Toast.makeText(AddRecordActivity.this, "UnSupported Age", Toast.LENGTH_SHORT).show();
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

            String record_id = new Date().getTime() + "";
            BMIRecord record = new BMIRecord();
            record.setStatus(status);
            record.setUID(mUser.getID());
            record.setBMI(bmi+"");
            record.setWeight(binding.weight.getText().toString());
            record.setLength(binding.length.getText().toString());
            record.setID(record_id);
            record.setIs_last(true);
            record.setDate(binding.date.getText().toString());


            MyFirebaseProvider.DoAddRecord(record, user.getUid(), new MessageListener() {
                @Override
                public void onResult(boolean result, String key) {
                    if (result){
                        Toast.makeText(AddRecordActivity.this, "Record Add Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }




    private String checkStatus(String status , double bmi){




        if(last_record != 0){
            System.out.println(last_record);
            double delta = bmi - last_record;
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

        }
        return status;
    }

    private double getAge(String date){

        Date date1 = new Date(binding.date.getText().toString());

        Date date2 = new Date(date);

        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        double age = days / 365.0 ;
        return age;
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
        binding.date.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}