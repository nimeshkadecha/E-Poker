package com.example.epokerproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.epokerproject.R;
import com.example.epokerproject.adapter.AdvocateAdapter;
import com.example.epokerproject.adapter.CaseAdapter;
import com.example.epokerproject.databinding.ActivityAdvocateListBinding;
import com.example.epokerproject.interfaces.ItemClickListener;

public class AdvocateListActivity extends AppCompatActivity implements ItemClickListener {
    private static final String CHANNEL_ID = "com.example.epokerproject.activity.chanel02";
    private ActivityAdvocateListBinding binding;
    private AdvocateAdapter advocateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdvocateListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView(){

        createNotificationChannel();

        binding.rvCaseList.setLayoutManager(new LinearLayoutManager(this));
        advocateAdapter = new AdvocateAdapter(this);

        binding.rvCaseList.setAdapter(advocateAdapter);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Demo";
            String description = "Demo Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification(){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.toolbarlogo)
                .setContentTitle("Approve Registration")
                .setContentText("Licence No - ABCD1234XYZ was approved by admin")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(101, builder.build());
    }

    @Override
    public void onClick(int position) {
        createNotification();
    }
}