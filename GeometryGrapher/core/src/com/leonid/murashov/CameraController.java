package com.leonid.murashov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraController implements GestureDetector.GestureListener {

    private PerspectiveCamera cam;
    SceneHolder sceneHolder;
    Vector3 position = new Vector3(150, 150, 150), lookPoint = new Vector3(0,0,0);
    float angle = 0;


    public CameraController(SceneHolder sceneHolder) {

        this.sceneHolder = sceneHolder;

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(position);
        cam.lookAt(lookPoint);
        //angle = (float)Math.acos((Vector3.Z.z*lookPoint.z)/(Vector3.Z.cpy().len()*lookPoint.cpy().len()));
        cam.near = 1;
        cam.far = 3000;
        cam.update();
    }

    public Camera getCamera() {
        return cam;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
       // Gdx.app.log(Float.toString(x), Float.toString(y));
        //Gdx.app.log("CAN SEE IT", "!!!!!!!!!!!!!!!");
        if(count == 2) {
            lookPoint.set(0,0,0);
            cam.position.set(100,0,0);
            cam.up.set(Vector3.Y);
            pan(-1, -1, 0, 0);
            angle = 0;
        }

        sceneHolder.changePlane();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //Gdx.app.log("YOUR BUTTON", Float.toString(button));
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

       // float len = position.len(),
       //         theta = (float)Math.acos(position.z/len),
       //         phi = (float)Math.atan(position.y/ position.x);

       // theta -= deltaX*0.005f;
       // if(phi + deltaY*0.005f > -3.1415f/2.f && phi + deltaY*0.005f < 3.1415f/2.f)
       //     phi += deltaY*0.005f;
       // position.set((float)(len*Math.sin(theta)*Math.cos(phi)), (float)(len*Math.sin(theta)*Math.sin(phi)), (float)(len*Math.cos(theta)));
       // cam.position.set(position);
        //Gdx.app.log("CAM ANG!!!!!", Float.toString(theta)+ " " +Float.toString(phi) + " " + Float.toString(len));
       // len = cam.position.len();
       // Gdx.app.log("CAM ANG!!!!!", Float.toString(cam.position.x)+ " " +Float.toString(cam.position.y) + " " + Float.toString(cam.position.z));
        cam.rotateAround(lookPoint, Vector3.Y.cpy(), -deltaX*0.1f);
        if(angle - deltaY*0.1f > -89 && angle - deltaY*0.1f < 89) {
            cam.rotateAround(lookPoint, Vector3.Y.cpy().crs(cam.position), -deltaY * 0.1f);
            angle -= deltaY*0.1f;
        }
        //cam.position.scl(len/cam.position.len());
        cam.lookAt(lookPoint);
        cam.update();
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(cam.position.cpy().scl(1 + (initialDistance-distance)*0.0001f).len() != 0)
            cam.position.scl(1 + (initialDistance-distance)*0.0001f);
        //cam.position.set(position);
        cam.lookAt(lookPoint);
        cam.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

        /*Vector2 bias = initialPointer1.cpy().add(initialPointer2).scl(0.5f).sub(pointer1.cpy().add(pointer2).scl(0.5f));
        Vector3 add = Vector3.Y.cpy().crs(cam.position).scl(bias.x*0.0001f).add(Vector3.Y.cpy().scl(bias.y*0.01f));
        lookPoint.add(add);
        cam.position.add(add);
        pan(-1,-1,0,0);

        cam.up.set(Vector3.Y);*/
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
