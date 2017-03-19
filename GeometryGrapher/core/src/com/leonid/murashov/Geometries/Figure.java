package com.leonid.murashov.Geometries;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public abstract class Figure {

    Model model;
    ModelInstance instance;
    protected Vector3 center;

    abstract void Draw(ModelBatch modelBatch, Environment environment);
    abstract void dispose();
    public Vector3 GetCenter() { return center; }
    public abstract void TranslateCenter(Vector3 center);
}
