package ru.leonid.opengl_paladin;


import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

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


    // точка полоения камеры
    float eyeX = 0;
    float eyeY = 2f;
    float eyeZ = 7;

    // точка направления камеры
    float centerX = 0;
    float centerY = 1;
    float centerZ = 0;

    // up-вектор
    float upX = 0;
    float upY = 1;
    float upZ = 0;

    private long lastTime;
    private int currTexture = 0;
    int animationList[] = {0, 1, 2, 3, 4, 3, 2, 1, 0, 4};

    Paladin paladin;
    int myI = 1;
    float distX = 12, distY = 766; //rand

    public OpenGLRenderer(Context context)
    {
        this.context = context;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(1f, 1f, 1f, 1f);
        glEnable(GL_DEPTH_TEST);

        createAndUseProgram();
        getLocations();
        paladin = new Paladin(aPositionLocation, aTextureLocation);
        //paladin.SetDirection(paladin.GetDirection()+1);
        paladin.SetDirection(paladin.DIRECTION_UP+2);
        prepareData();
       // bindData();
        createViewMatrix();
        paladin.SetTexture(0);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }

    private void prepareData()
    {
       /* final float skyOffset = 0f;
        float[] vertices = {
            //coordinates for sky
            -2, 4f, skyOffset,   0, 0,
            -2, 0, skyOffset,      0, 0.5f,
            2,  4f, skyOffset,   0.5f, 0,
            2, 0, skyOffset,       0.5f, 0.5f,

            //coordinates for sea
            -2, 0, 0,      0.5f, 0,
            -2, -1, 2,       0.5f, 0.5f,
            2, 0, 0,      1, 0,
            2,-1, 2,        1, 0.5f
        };

       vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);*/

        texture = TextureUtils.loadTexture(context, R.drawable.texture);
        texture1 = TextureUtils.loadTexture(context, R.drawable.texture);
        texture2 = TextureUtils.loadTexture(context, R.drawable.texture);
        tPaladinTexture = TextureUtils.loadTexture(context, R.drawable.paladin);
        paladin.SetAtlas(tPaladinTexture);


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

    private void createViewMatrix()
    {
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    private void bindMatrix() {

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

        long currTime = System.currentTimeMillis();
        if((currTime - lastTime > 80) && paladin.isRunning || !paladin.isRunning && paladin.currTexture == 4 && (currTime - lastTime > 800) ||
                                                              !paladin.isRunning && paladin.currTexture != 4 && (currTime - lastTime > 100))
        {
            paladin.currTexture = paladin.currTexture + 1;//5
            paladin.SetTexture(paladin.currTexture);
            lastTime = System.currentTimeMillis();
            paladin.SetTexture(paladin.currTexture);
            myI++;
        }
        if(myI % 50 == 0)
        {
            paladin.SetDirection(paladin.GetDirection() + 1);
            myI++;
        }

        paladin.Draw();
    }


    private void setModelMatrix() {
        Matrix.translateM(mModelMatrix, 0, 0, -0.5f, 0);
        //В переменной angle угол будет меняться  от 0 до 360 каждые 10 секунд.

        float angle = -((float)(SystemClock.uptimeMillis() % TIME) / TIME * 360);
        //void rotateM (float[] m,  int mOffset, float a,float x, float y, float z)
        //Rotates matrix m in place by angle a (in degrees) around the axis (x, y, z).
        Matrix.rotateM(mModelMatrix, 0, angle, 0, 0, 1);
        Matrix.translateM(mModelMatrix, 0, -0.8f, 0, 0);
    }


    public void SetCamAngle(float _scale, float _x, float _y)
    {
        Matrix.rotateM(mViewMatrix,0, _scale, _y, _x, 0f);
    }

    public void RotateCam(float a)
    {
        if(a == 0) a= 0.0001f;
        Matrix.rotateM(mViewMatrix, 0, 1, 0, 0, a);
    }

    public void SetDistanation(float x, float y)
    {
        distX = x;
        distY = y;
    }



}


