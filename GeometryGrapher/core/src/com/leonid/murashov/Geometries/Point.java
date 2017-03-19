package com.leonid.murashov.Geometries;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Point extends Figure {

    public Point(Vector3 pos)
    {
        this.center = pos;
        ModelBuilder modelBuilder = new ModelBuilder();

        Color color = Color.BLACK;
        if(pos.len() == 0)//(new Vector3(0,0,0)))
            color = Color.RED;

        model = modelBuilder.createSphere(3,3,3,16,16, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        instance.transform.setTranslation(pos);
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
