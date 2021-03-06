package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myfirstapp.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity implements SettingsFragment.SettingsStateListener, KeyEvent.Callback {
    SharedPreferences sharedPreferences;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =
                getSharedPreferences("com.example.myfirstapp.userstore", Context.MODE_PRIVATE);
        checkInitialTheme();
        com.example.myfirstapp.databinding.ActivityMainBinding binding = com.example.myfirstapp.databinding.ActivityMainBinding
                .inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        checkAndSetSound();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void checkInitialTheme() {
        boolean currentNightMode = sharedPreferences.getBoolean("darkTheme", false);
        if (currentNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    private void checkAndSetTheme(boolean darkTheme) {
        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            sharedPreferences.edit().putBoolean("soundOn", true).apply();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndSetSound();
    }

    private void checkAndSetSound() {
        if (sharedPreferences.getBoolean("soundOn", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                muteOrUnmute(AudioManager.ADJUST_UNMUTE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                muteOrUnmute(AudioManager.ADJUST_MUTE);
            }
        }
    }

    private void muteOrUnmute(int adjust) {
        //mute or unmute audio
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, adjust, 0);
    }

    @Override
    public void onSoundChange() {
        checkAndSetSound();
        SettingsFragment.binding.switchEnableSound.setChecked(true);
    }

    @Override
    public void onThemeChange(boolean darkTheme) {
        checkAndSetTheme(darkTheme);
    }
}
