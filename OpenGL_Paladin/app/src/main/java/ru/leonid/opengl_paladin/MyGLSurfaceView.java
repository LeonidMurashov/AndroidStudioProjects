package ru.leonid.opengl_paladin;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.io.IOException;

import static java.lang.Math.abs;

public class MyGLSurfaceView extends GLSurfaceView
{

    private final float TOUCH_SCALE_FACTOR = 0.3f;
    private float mPreviousX;
    private float mPreviousY;
    private OpenGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    public void setRenderer(OpenGLRenderer _renderer)
    {
        super.setRenderer(_renderer);
        renderer = _renderer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if(abs(dx+dy) != 0)
                    renderer.SetCamAngle((float) (Math.sqrt(dx * dx + dy * dy) * TOUCH_SCALE_FACTOR), dx/abs(dx+dy), dy/abs(dx+dy));
                requestRender();
                break;
            default:
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
