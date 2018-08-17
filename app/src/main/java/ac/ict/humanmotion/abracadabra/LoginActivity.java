package ac.ict.humanmotion.abracadabra;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import ac.ict.humanmotion.abracadabra.tools.CircularImage;


public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
       // getSupportActionBar().hide();
        ImageView plogin = (ImageView) findViewById(R.id.cover_user_photo);
        plogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                // TODO Auto-generated method stub
                    login();
            }
        });
    }

    private void login() {
            Intent intent = new Intent();
            intent.setClass(this, SplashActivity.class);
            startActivityForResult(intent, 15);
      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);

        startActivityForResult(intent, PICK_FROM_CAMERA);*/

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        startActivityForResult(intent, 1);
*/
    }
}