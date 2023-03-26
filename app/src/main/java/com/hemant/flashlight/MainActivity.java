package com.hemant.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hemant.flashlight.databinding.ActivityMainBinding;


public class MainActivity<Intent> extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean hasCameraFlash = false;
    boolean isTorch = true;
    boolean isMobile = false;
    boolean flashOnTorch = true;
    boolean flashOnMobile = true;
    boolean isSomeTorch = false;
    boolean isSomeMobile = false;
    int a,b,c;
    int start=1;

    private ImageButton powerButton,touchButton,mobileButton;
    //below declaration is for change the brightness
    private ContentResolver contentResolver;
    private Window window;
    private int pBrightness;
    private float aBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        powerButton=findViewById(R.id.power_button);
        touchButton=findViewById(R.id.touch_button);
        mobileButton=findViewById(R.id.mobile_button);
        contentResolver = getContentResolver();
        window = getWindow();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.button_click_sound);    //this can get mp3 file from raw directory
        //below code for give permission from user brightness adjustment
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!android.provider.Settings.System.canWrite(this)){
                android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:"+ getApplication().getPackageName()));
                startActivity(intent);
            }
        }
        try {
            pBrightness = android.provider.Settings.System.getInt(contentResolver,android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        android.content.SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        a = sharedPreferences.getInt("a",0);
        b = sharedPreferences.getInt("b",0);

        isTorch = sharedPreferences.getBoolean("isTorch",false);
        isMobile = sharedPreferences.getBoolean("isMobile",false);
        start=sharedPreferences.getInt("start",1);
        if(start==1){
            isTorch=true;
            Toast.makeText(this, "Turn on this setting to start the app", Toast.LENGTH_LONG).show();
        }

        boolean c1 = sharedPreferences.getBoolean("checkbox1",false);
        boolean c2 = sharedPreferences.getBoolean("checkbox2",false);

        if(b==1){
            if(c2 && !c1){
                onStop();
            }
            b=0;
        }

        binding.powerButton.setOnClickListener(view -> {     //new View.setOnclickListener()
            if(isTorch){
                boolean c3 = sharedPreferences.getBoolean("checkbox3",false);
                boolean c4 = sharedPreferences.getBoolean("checkbox4",false);
                if(c3){
                    mediaPlayer.start();
                }
                binding.powerButton.setHapticFeedbackEnabled(c4);

                if(hasCameraFlash){
                    checkValidation();
                    if(flashOnTorch){
                        changeState(flashOnTorch);
                        flashOnTorch = false;
                    }
                    else {
                        changeState(flashOnTorch);
                        flashOnTorch = true;
                    }
                    isSomeTorch=true;
                    isSomeMobile=false;
                    flashOnMobile=true;
                }

            }
            else if(isMobile){
                boolean c3 = sharedPreferences.getBoolean("checkbox3",false);
                boolean c4 = sharedPreferences.getBoolean("checkbox4",false);
                if(c3){
                    mediaPlayer.start();
                }
                binding.powerButton.setHapticFeedbackEnabled(c4);
                checkValidation();
                if(flashOnMobile){
                    mobileTouchOn();
                }
                else{
                    mobileTouchOff();
                }
                isSomeMobile=true;
                isSomeTorch=false;
                flashOnTorch=true;
            }
        });
        binding.touchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean c3 = sharedPreferences.getBoolean("checkbox3",false);
                boolean c4 = sharedPreferences.getBoolean("checkbox4",false);
                if(c3){
                    mediaPlayer.start();
                }
                binding.touchButton.setHapticFeedbackEnabled(c4);
                isTorch=true;
                ed.putBoolean("isTorch",true);
                ed.apply();
                ed.putBoolean("isMobile",false);
                ed.apply();
                changeMode(true);
            }
        });
        binding.mobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean c3 = sharedPreferences.getBoolean("checkbox3",false);
                boolean c4 = sharedPreferences.getBoolean("checkbox4",false);
                if(c3){
                    mediaPlayer.start();
                }
                binding.mobileButton.setHapticFeedbackEnabled(c4);
                isMobile=true;
                ed.putBoolean("isMobile",true);
                ed.apply();
                ed.putBoolean("isTorch",false);
                ed.apply();
                changeMode(false);
            }
        });
    }
    public void changeState(boolean state){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            cameraOnOffMethod(state);
        }
        else{
            Toast.makeText(MainActivity.this, "Your device is not compatible", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void changeMode(boolean mode){
        if(mode){
            if(isTorch){
                binding.touchButton.setImageResource(R.drawable.back_torch_on);
                isMobile=false;
                binding.mobileButton.setImageResource(R.drawable.front_torch_off);
                binding.mainBackground.setBackgroundColor(getResources().getColor(R.color.main_background));
                binding.touchButton.setBackgroundColor(getResources().getColor(R.color.main_background));
                binding.mobileButton.setBackgroundColor(getResources().getColor(R.color.main_background));
                android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS,pBrightness);
                WindowManager.LayoutParams layoutParamas = window.getAttributes();
                layoutParamas.screenBrightness = pBrightness;
                window.setAttributes(layoutParamas);
            }
        }
        else{
            binding.mobileButton.setImageResource(R.drawable.front_torch_on);
            if(isMobile){
                isTorch=false;
                binding.touchButton.setImageResource(R.drawable.back_torch_off);
                cameraOnOffMethod(false);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startOrStop();
        a=1;
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("a",a);
        ed.apply();
        start = sharedPreferences.getInt("start",1);
        isTorch = sharedPreferences.getBoolean("isTorch",false);
        isMobile = sharedPreferences.getBoolean("isMobile",false);
        if(isTorch){
            binding.touchButton.setImageResource(R.drawable.back_torch_on);
        }
        else if(isMobile){
            binding.mobileButton.setImageResource(R.drawable.front_torch_on);
        }
        if(start==1){
            binding.touchButton.setImageResource(R.drawable.back_torch_on);
            start=0;
            ed.putInt("start",start);
            ed.putBoolean("isTorch",true);
            ed.apply();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startOrStop();
        a=1;
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("a",a);
        ed.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        boolean c2 = sharedPreferences.getBoolean("checkbox2",false);

        if(c2){
            b=1;
            ed.putInt("b",b);
            ed.apply();
            if(b==1){
                if(isTorch){
                    changeState(false);
                    flashOnTorch=true;
                    isSomeTorch=true;
                    isSomeMobile=false;
                    flashOnMobile=true;
                    ed.putBoolean("isTorch",true);
                    ed.apply();
                    ed.putBoolean("isMobile",false);
                    ed.apply();
                }
                else if(isMobile){
                    flashOnMobile=true;
                    binding.mainBackground.setBackgroundColor(getResources().getColor(R.color.main_background));
                    binding.touchButton.setBackgroundColor(getResources().getColor(R.color.main_background));
                    binding.mobileButton.setBackgroundColor(getResources().getColor(R.color.main_background));
                    android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS,pBrightness);
                    WindowManager.LayoutParams layoutParamas = window.getAttributes();
                    layoutParamas.screenBrightness = pBrightness;
                    window.setAttributes(layoutParamas);
                    isSomeMobile=true;
                    isSomeTorch=false;
                    flashOnTorch=true;
                    ed.putBoolean("isMobile",true);
                    ed.apply();
                    ed.putBoolean("isTorch",false);
                    ed.apply();
                }
            }

        }
    }

    public void cameraOnOffMethod(boolean state){
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        String camId;
        try {
            camId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(camId,state);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void checkValidation(){
        if(!flashOnTorch && isTorch && !flashOnMobile && isSomeMobile && !isSomeTorch){
            flashOnTorch=true;
        }
        else if(!flashOnMobile && isMobile && !flashOnTorch && isSomeTorch && !isSomeMobile){
            flashOnMobile=true;
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                android.content.Intent intent = new android.content.Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
                break;

            case R.id.share:
                android.content.Intent intent3 = new android.content.Intent(android.content.Intent.ACTION_SEND);
                intent3.setType("text/plain");
                intent3.putExtra(android.content.Intent.EXTRA_SUBJECT,"Check out this app ");
                intent3.putExtra(android.content.Intent.EXTRA_TEXT,"Click on the link to download:\n https://drive.google.com/file/d/12rRqCInaHcA_phDpuL1bAe2lkjx2opB4/view?usp=drivesdk");
                startActivity(android.content.Intent.createChooser(intent3,"Share this app"));
                break;

            case R.id.send_feedback:
                android.content.Intent intent4 = new android.content.Intent(android.content.Intent.ACTION_SEND);
                intent4.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{"vasulehemant03@gmail.com"});
                intent4.putExtra(android.content.Intent.EXTRA_SUBJECT,"About flashlight app feedback");
                intent4.setType("message/rfc822");
                intent4.setPackage("com.google.android.gm");
                startActivity(intent4);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    public void mobileTouchOn(){
        flashOnMobile=false;
        binding.mainBackground.setBackgroundColor(getResources().getColor(R.color.white));
        binding.touchButton.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mobileButton.setBackgroundColor(getResources().getColor(R.color.white));
        android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS,pBrightness);
        WindowManager.LayoutParams layoutParamas = window.getAttributes();
        aBrightness = pBrightness/ (float) 300;
        layoutParamas.screenBrightness = aBrightness;
        window.setAttributes(layoutParamas);
    }

    public void mobileTouchOff(){
        flashOnMobile=true;
        binding.mainBackground.setBackgroundColor(getResources().getColor(R.color.main_background));
        binding.touchButton.setBackgroundColor(getResources().getColor(R.color.main_background));
        binding.mobileButton.setBackgroundColor(getResources().getColor(R.color.main_background));
        android.provider.Settings.System.putInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS,pBrightness);
        WindowManager.LayoutParams layoutParamas = window.getAttributes();
        layoutParamas.screenBrightness = pBrightness;
        window.setAttributes(layoutParamas);
    }
    public void startOrStop(){
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        boolean c1 = sharedPreferences.getBoolean("checkbox1",false);
        if(a==1){
            if(c1){
                if(isTorch){
                    changeState(true);
                    flashOnTorch=false;
                    isSomeTorch=true;
                    isSomeMobile=false;
                    flashOnMobile=true;
                }
                else{
                    mobileTouchOn();
                    isSomeMobile=true;
                    isSomeTorch=false;
                    flashOnTorch=true;
                }
            }
            a=0;
        }
    }
}