package com.leonid.murashov;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.leonid.murashov.Geometries.Line;
import com.leonid.murashov.Geometries.Plane;
import com.leonid.murashov.Geometries.Point;

import java.util.Random;

public class MainClass extends InputAdapter implements ApplicationListener{

	public ModelBatch modelBatch;
    public Environment environment;

    protected Label label;
    protected BitmapFont font;
    protected Stage stage;
    protected SceneHolder sceneHolder;
    CameraController cameraController;

	@Override
	public void create () {

        sceneHolder = new SceneHolder();
        cameraController = new CameraController(sceneHolder);
        Gdx.input.setInputProcessor(new GestureDetector(cameraController));

		modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
        //environment.add(new DirectionalLight().set(0.3f,0.3f,0.3f, 10f, 10f ,-20f));

        environment.add(new PointLight().set(0.3f,0.3f,0.3f, 100f, 100f, 100f, 50));

        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

        stage = new Stage();
        stage.addActor(label);
	}

    @Override
    public void resize(int width, int height) {

    }

    @Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        StringBuilder builder = new StringBuilder();
        builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        label.setText(builder);
        stage.draw();

		modelBatch.begin(cameraController.getCamera());
        sceneHolder.render(modelBatch, environment);
		modelBatch.end();
	}

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
	public void dispose () {
	}
}
