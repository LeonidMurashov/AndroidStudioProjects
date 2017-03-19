package com.learning.texnar13.zvezdacoha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {
    MyDraw canvas;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new MyDraw(this);
        setContentView(canvas);
        canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 7) {
                    i = 0;
                } else {
                    canvas.level = i;
                    i++;
                }
                canvas.redraw();
            }
        });
    }
}
