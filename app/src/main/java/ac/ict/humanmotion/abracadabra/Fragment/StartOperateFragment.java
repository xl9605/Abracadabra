package ac.ict.humanmotion.abracadabra.Fragment;

import ac.ict.humanmotion.abracadabra.LoginActivity;
import ac.ict.humanmotion.abracadabra.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;


public class StartOperateFragment extends Activity {
    private Chronometer timer;
    private Button end_operate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startoperate);
        timer = (Chronometer)this.findViewById(R.id.chronometer);
        timer.setBase(SystemClock.elapsedRealtime());
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0"+String.valueOf(hour)+":%s");
        timer.start();
        end_operate = findViewById(R.id.end_operate);
        end_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();
                Intent intent=new Intent();
                intent.setClass(StartOperateFragment.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
