package com.leonid.murashov.Geometries;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Plane extends Figure {

    com.badlogic.gdx.math.Plane plane;
    float A, B, C, D, width;

    public Plane(Vector3 vec1, Vector3 vec2, Vector3 vec3, Color color) {
        if(vec1.isCollinear(vec2) && vec2.isCollinear(vec3) && vec3.isCollinear(vec1) || vec1 == vec2 || vec2 == vec3 || vec3 == vec1)
            throw new IllegalArgumentException("Collinear vectors");

        plane = new com.badlogic.gdx.math.Plane();
        plane.set(vec1, vec2, vec3);

        //vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec1).len();
                // This point has the same distance to other 3 points
        this.width = Math.max(Math.max(vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec1).len(),
                vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec2).len()),
                vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec3).len());

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(width*2+20,width*2+20,0.3f,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        //instance.transform.setToRotationRad();
        Vector3 normal = plane.getNormal();
        instance.transform.rotate(Vector3.Z, normal);
        A = normal.x;
        B = normal.y;
        C = normal.z;
        D = -1 * (A*vec1.x + B*vec1.y + C*vec1.z);

               // instance.transform.setToRotation(plane.getNormal(), )
        this.center = vec1.cpy().add(vec2).add(vec3).scl(0.33333f);
        instance.transform.setTranslation(center);
    }

    public Plane(float A, float B, float C, float D, float width, Vector3 center, Color color)
    {
        plane = new com.badlogic.gdx.math.Plane(new Vector3(A,B,C), D);

        //vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec1).len();

        this.width = width;
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(width*2+20,width*2+20,0.3f,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        //instance.transform.setToRotationRad();
        Vector3 normal = plane.getNormal();
        instance.transform.rotate(Vector3.Z, normal);
        this.A = normal.x;
        this.B = normal.y;
        this.C = normal.z;
        this.D = D;

        // instance.transform.setToRotation(plane.getNormal(), )
        this.center = center;
        instance.transform.setTranslation(center);
    }

    public Plane(Vector3 normal, float width, Vector3 center, Color color)
    {
        plane = new com.badlogic.gdx.math.Plane(normal, center);

        //vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec1).len();

        this.width = width;
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(width*2+20,width*2+20,0.3f,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        //instance.transform.setToRotationRad();
        instance.transform.rotate(Vector3.Z, normal);
        this.A = normal.x;
        this.B = normal.y;
        this.C = normal.z;
        D = -1 * (A*center.x + B*center.y + C*center.z);

        // instance.transform.setToRotation(plane.getNormal(), )
        this.center = center;
        instance.transform.setTranslation(center);
    }

    @Override
    public void Draw(ModelBatch modelBatch, Environment environment) {

        modelBatch.render(instance, environment);
    }

    @Override
    public void TranslateCenter(Vector3 translation) {
        //plane.d += translation.z;
        instance.transform.set(instance.transform.getTranslation(new Vector3(0,0,0)).cpy().add(translation), instance.transform.getRotation(new Quaternion()));
       // instance.transform.translate(translation);
        //this.center.add(translation);
    }

    @Override
    public void dispose() {
        model.dispose();
        center.set(0,0,0);
    }

    public Plane GetParallelPlane(Color color)
    {
        Random random = new Random();
        Vector3 normal = plane.getNormal();
        float _D = D + (random.nextFloat()*60+30)*(random.nextBoolean()?1:-1);
        float t = -(A*center.x+B*center.y+C*center.z+_D)/(A*normal.x+B*normal.y+C*normal.z);
        return new Plane(A, B, C, _D, width, center.cpy().add(normal.cpy().scl(t)), color);
    }

    public Plane GetPerpendicularPlane(Color color)
    {
        Random random = new Random();
        Vector3 normal = plane.getNormal();
        // by = 1, bz = 1
        float bx = -(normal.y+normal.z)/(normal.x == 0 ? 0.00001f : normal.x);
        return new Plane(new Vector3(bx,1,1).setLength(1.f), width, center.cpy().add(new Vector3(bx,1,1).setLength(1.f).crs(normal).scl(random.nextFloat()*width-width/2)), color);
    }


}
