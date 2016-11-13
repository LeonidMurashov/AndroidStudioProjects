package ru.leonid.opengl_runningpaladin;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.sql.Time;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static ru.leonid.opengl_runningpaladin.R.drawable.paladin;

/**
 * Автор: Леонид Мурашов
 * e-mail: l.murashov@yandeex.ru
 *
 */

public class OpenGLRenderer implements Renderer {

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private Context context;

    private FloatBuffer vertexData;

    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    private float x = 0, y = 0;

    private int texture;
    private int texture1;
    private int texture2;
    private int tPaladinTexture;
    private final static long TIME = 7000L;

    long lastTime = 0;
    boolean rotated = true;

    private int currTexture = 0;
    int PALADIN_COUNT = 3;
    Random random;

    List<Paladin> paladins;
    float randomNumbers[];
    int myI = 3;

    public OpenGLRenderer(Context context)
    {
        this.context = context;

        random = new Random(System.currentTimeMillis());
        paladins = new Vector<Paladin>();
        for(int i = 0; i < 1; i++)
        {
            paladins.add(new Paladin(Math.abs(random.nextInt() % 1200), Math.abs(random.nextInt() % 800)));
            paladins.get(i).SetDirection(Paladin.DIRECTION_UP + Math.abs(random.nextInt()%8));
            paladins.get(i).SetTexture(0); //idle
        }
        randomNumbers = new float[100];//crash on 50th paladin
        for(int i = 0; i < 100; i++)
            randomNumbers[i] = random.nextFloat()*2-1;
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1)
    {
        glClearColor(1f, 1f, 1f, 1f);
        glEnable(GL_DEPTH_TEST);

        createAndUseProgram();
        getLocations();
        prepareData();


        for(int i = 0; i < 1; i++)
           paladins.get(i).InitializeParameters(aPositionLocation, aTextureLocation, tPaladinTexture);

        createViewMatrix(false);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }

    private void prepareData()
    {
        final float skyOffset = 0f;
        float[] vertices = {
            //coordinates for plane
            -3.5f, 3.5f, -1,   0, 0,
            -3.5f, -1.5f, -1,      0, 1f,
            3.5f,  3.5f, -1,   1f, 0,
            3.5f, -1.5f, -1,       1f, 1f
        };

       vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        texture = TextureUtils.loadTexture(context, R.drawable.plane);
        tPaladinTexture = TextureUtils.loadTexture(context, paladin);
    }

    private void createAndUseProgram() {
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
    }

    private void getLocations() {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void bindData() {
        // координаты вершин
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);


        // координаты текстур
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);


        // помещаем текстуру в target 2D юнита 0
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texture);


        // помещаем текстуру1 в target 2D юнита 0
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texture1);

        // помещаем текстуру2 в target 2D юнита 0

        //glActiveTexture(GL_TEXTURE0);
        //glBindTexture(GL_TEXTURE_2D, texture2);

        // юнит текстуры
        //glUniform1i(uTextureUnitLocation, 0);
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -0.5f;
        float right = 0.5f;
        float bottom = -0.5f;
        float top = 0.5f;
        float near = 2;
        float far = 12;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix(boolean rotated) {
        // точка полоения камеры
        float eyeX;
        float eyeY;
        float eyeZ;
        if (!rotated)
        {
            eyeX = 0;
            eyeY = 2f;
            eyeZ = 7;
        }
        else
        {
            eyeX = 5;
            eyeY = 2f;
            eyeZ = 1.5f;
        }

        // точка направления камеры
        float centerX = 0;
        float centerY = 1;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    public void bindMatrix()
    {
        Matrix.multiplyMM(mMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//сбрасываем model матрицу
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

        glUseProgram(programId);

        bindData();
        glBindTexture(GL_TEXTURE_2D, texture);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        /*long currTime = System.currentTimeMillis();
        if(currTime - lastTime > 1700)
        {
            rotated ^= true;
            createViewMatrix(rotated);
            lastTime = System.currentTimeMillis();
        }*/
        long currTime = System.currentTimeMillis();

        if(currTime - lastTime > 3000)
        {
            if(paladins.size() < PALADIN_COUNT)
            {
                paladins.add(new Paladin(Math.abs(random.nextInt() % 1200), Math.abs(random.nextInt() % 800)));
                paladins.get(paladins.size() - 1).SetDirection(Paladin.DIRECTION_UP + Math.abs(random.nextInt()%8));
                paladins.get(paladins.size() - 1).SetTexture(0); //idle
                paladins.get(paladins.size() - 1).InitializeParameters(aPositionLocation, aTextureLocation, tPaladinTexture);
            }

            lastTime = System.currentTimeMillis();
        }


        for(int i = 0; i < paladins.size(); i++)
            paladins.get(i).FrameAction();

        Collections.sort(paladins, new Comparator<Paladin>()
                {
                    public int compare(Paladin a, Paladin b)
                    {
                        return ((Float) a.GetBiasY()).compareTo((Float) b.GetBiasY());
                    }
                }
            );

        glDisable(GL_DEPTH_TEST);
        for(int i = 0; i < paladins.size(); i++)
            paladins.get(i).Draw(mModelMatrix, this);
        glEnable(GL_DEPTH_TEST);
    }

    public void SetDistanation(float x, float y)
    {
        if(paladins.size() > 0)paladins.get(0).GoTo(x, y);
        for(int i = 1; i < paladins.size(); i++)
            paladins.get(i).GoTo(x+100*randomNumbers[i], y+100*randomNumbers[i + PALADIN_COUNT]);
    }
}


