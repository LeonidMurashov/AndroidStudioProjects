package com.leonid.murashov.Geometries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Line extends Figure {

    public Line(Vector3 vec1, Vector3 vec2) {
        center = vec1.cpy().add(vec2).scl(0.5f);
        Vector3 resultVector = vec1.cpy().sub(vec2);

        ModelBuilder modelBuilder = new ModelBuilder();
        //model = modelBuilder.createCylinder(1, resultVector.len(),1,8, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
         //       VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        model = modelBuilder.createArrow(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z,0.001f,10f*(100.f/resultVector.len()),16,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                         VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
       // instance.transform.set(vec1,new Quaternion());//, new Quaternion(Math.asin(vec1.sub(vec2).x));

      //  float yaw = 3.14f/2f, pitch = 3.14f/2f, roll = 3.14f/2f;
/*
        if(resultVector.y != 0)
            instance.transform.rotateRad(Vector3.X, (float)Math.atan(resultVector.z / resultVector.y));
        if(resultVector.x != 0)
            instance.transform.rotateRad(Vector3.Y, (float)Math.atan(resultVector.z / resultVector.x));
        if(resultVector.y != 0)
            instance.transform.rotateRad(Vector3.Z, (float)Math.atan(resultVector.x / resultVector.y));
*/
        /*if(resultVector.y != 0)
            yaw = (float)Math.atan(resultVector.z / resultVector.y);
        if(resultVector.z != 0)
            pitch = (float)Math.atan(resultVector.x / resultVector.z);
        if(resultVector.y != 0)
            roll = (float)Math.atan(resultVector.x / resultVector.y);
*/
        //instance.transform.setToRotationRad(Vector3.Y, 1.5f);
        //instance.transform.setToRotationRad(Vector3.Y,(float)Math.atan(resultVector.z / resultVector.y));
       // instance.transform.setToRotationRad(Vector3.X,(float)Math.atan(resultVector.z / resultVector.y));
       // instance.transform.setToRotationRad(Vector3.Z, (float)Math.atan(resultVector.x / resultVector.y));

        //instance.transform.setToRotationRad(Vector3.Z, (float)Math.atan(resultVector.y/ resultVector.x));
        //instance.transform.setToRotationRad(Vector3.X, (float)Math.atan(resultVector.z/ resultVector.y));
       // instance.transform.rotateRad(Vector3.Y, (float)Math.atan2(resultVector.z, resultVector.y));

      //  instance.transform.rotate()

        //instance.transform.setTranslation(vec1.cpy().add(vec2).scl(0.5f));

        //instance.transform.

        //Gdx.app.log("Координата!!!!!!!!!!", Float.toString((float)Math.atan(resultVector.y/ resultVector.x)));
    }

    @Override
    public void Draw(ModelBatch modelBatch, Environment environment) {

        modelBatch.render(instance, environment);
    }

    @Override
    public void TranslateCenter(Vector3 translation) {
        instance.transform.translate(translation);
        this.center.add(translation);
    }

    @Override
    public void dispose() {
        model.dispose();
        center.set(0,0,0);
    }
}
