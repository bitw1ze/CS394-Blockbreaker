package edu.csub.cs.blockbreaker;

import edu.csub.cs.audio.Assets;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Scores extends Activity {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        Button next = (Button) findViewById(R.id.Button02);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                Assets.click.play(1);
                finish();
            }
        });
    }
}