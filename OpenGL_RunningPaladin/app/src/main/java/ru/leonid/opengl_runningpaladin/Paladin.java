package ru.leonid.opengl_runningpaladin;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static java.lang.Math.pow;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static android.opengl.GLES20.*;

public class Paladin
{
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_UPRIGHT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_DOWNRIGHT = 3;
    public static final int DIRECTION_DOWN = 4;
    public static final int DIRECTION_DOWNLEFT = 5;
    public static final int DIRECTION_LEFT = 6;
    public static final int DIRECTION_UPLEFT = 7;

    private final static int POSITION_COUNT = 3;
    private final static int TEXTURE_COUNT = 2;
    private final static int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private int aPositionLocation;
    private int aTextureLocation;
    private int tPaladinTexture;
    private FloatBuffer vertexData;
    private int direction = DIRECTION_UP;
    private long lastTime;
    private float distX = 500, distY = 300; //rand
    private float currX, currY;
    private boolean animation = false;
    float biasY;
    private List<Float> points;

    float[] verticesCoordinates;
    float[] vertices;
    float[] paladinTextures;
    public int currTexture = 0;
    public boolean isRunning = true;

    Paladin(float startPosX, float startPosY)
    {
        currX = distX = startPosX;
        currY = distY = startPosY;
        paladinTextures = new float[640];
        float width = 882, height = 808;
        int elementsHeight = 8, elementsWidth = 10;
        int elHeight = 98;
        int j = 0;
        for (int row = 0; row < 8; row++)
            for(int i = 5; i < 10; i++)
            {
                int elWidth = (i >= 0 && i < 4) ? 92 : 83;
                int offset = (i >= 0 && i < 4) ? 0 : 45;
                paladinTextures[j++] = (elWidth*(i+1) + offset)/(float)width; paladinTextures[j++] = (elHeight*row)/(float)height;
                paladinTextures[j++] = (elWidth*(i+1) + offset)/(float)width; paladinTextures[j++] = (elHeight*(row+1))/(float)height;
                paladinTextures[j++] = (elWidth*i + offset)/(float)width; paladinTextures[j++] = (elHeight*row)/(float)height;
                paladinTextures[j++] = (elWidth*i + offset)/(float)width; paladinTextures[j++] = (elHeight*(row+1))/(float)height;    // Texture unwrapping on Triangles_Strips(square)(2, 3, 1, 4 quaters)
            }

        float k = 1.7f;
        verticesCoordinates = new float [] {
                //coordinates for paladin
                // X, Y, Z
                0.425f*k, 0.5f*k, 0,
                0.425f*k, 0, 0,
                0, 0.5f*k, 0,
                0, 0, 0
        };
        lastTime = System.currentTimeMillis();
        points = new Vector<Float>();
    }

    public void SetTexture(int index) // Current 0-4
    {
        // Generate vertex(position + texels) array
        if(index >= 5)
            index %= 5;
        vertices = new float[20];
        int j = 0;
        biasY = currY/600.0f;
        for(int i = 0; i<4; i++)
        {
            vertices[j++] = verticesCoordinates[0 + i*3];
            vertices[j++] = verticesCoordinates[1 + i*3];
            vertices[j++] = biasY;//verticesCoordinates[2 + i*3];
            vertices[j++] = paladinTextures[0 + i*2 + index*8 + direction * 40];
            vertices[j++] = paladinTextures[1 + i*2 + index*8 + direction * 40];
        }

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

    }

    void GoTo(float x, float y)
    {
        points.add(x);
        points.add(y);
        if(points.size() == 2)
        {
            distX = x;
            distY = y;

            direction = GetDirection(distX - currX, distY - currY);
        }
    }

    public float GetBiasY()
    {
        return currY;
    }

    void FrameAction()
    {
        long currTime = System.currentTimeMillis();
        if(currTime - lastTime > 70 && animation)
        {
            lastTime = System.currentTimeMillis();
            SetTexture(currTexture);
            currTexture = currTexture + 1;//5
        }

        // Every frame
        float shiftX = 0, shiftY = 0;
        double part = 0;
        if(distX - currX != 0)
            part = Math.min(1, 5/(double)Math.sqrt(Math.pow((distX - currX), 2) + Math.pow((distY - currY), 2)));
        shiftX = (float)((Math.abs(distX - currX)*part));
        shiftY = (float)((Math.abs(distY - currY)*part));

        if(distX - currX < 0)
            shiftX *= -1;
        if(distY - currY < 0)
            shiftY *= -1;


        currY += shiftY;
        currX += shiftX;
        if(shiftX != 0 || shiftY != 0)
        {
            animation = true;
        }
        else
        {
            SetTexture(5);
            // Stop paladin or go to next point
            if(points.size() != 0)
            {
                points.remove(0);
                points.remove(0);
            }
            if(points.size() > 0)
            {
                while(points.size() > 2 && Math.sqrt(Math.pow(distX - points.get(0), 2) + Math.pow(distY - points.get(1), 2)) < 50
                        || points.size() > 0 && Math.sqrt(Math.pow(distX - points.get(0), 2) + Math.pow(distY - points.get(1), 2)) < 10)
                {
                    points.remove(0);
                    points.remove(0);
                }
                if(points.size() > 0)
                {
                    distX = points.get(0);
                    distY = points.get(1);

                    direction = GetDirection(distX - currX, distY - currY);
                }
            }

            animation = false;
        }
    }

    void Draw(float[] mModelMatrix, OpenGLRenderer openGLRenderer)
    {
        // Bind data
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);


        // координаты текстур
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);

        // Shift matrix
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, currX/215.0f - 3.3f, 2.6f - (currY/215.0f), 0f);
        openGLRenderer.bindMatrix();

        // Drawing
        glBindTexture(GL_TEXTURE_2D, tPaladinTexture);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    void InitializeParameters(int _aPositionLocation, int _aTextureLocation, int _tPaladinTexture)
    {
        aPositionLocation = _aPositionLocation;
        aTextureLocation = _aTextureLocation;
        tPaladinTexture = _tPaladinTexture;
    }

    void SetDirection(int _direction)
    {
        if(_direction >= 8)
            _direction %= 8;
        direction = _direction;
    }
    int GetDirection()
    {
        return direction;
    }

    int GetDirection(float shiftX, float shiftY)
    {
        double angle = 0;
        if(shiftX != 0)
            angle = Math.atan(Math.abs(shiftY)/(double)Math.abs(shiftX)) * 57.3;
        if(shiftX == 0 && shiftY == 0) return -1;
        if(shiftX > 0)
        {
            if(shiftY > 0)
            {
                if(angle < 22.5) return DIRECTION_RIGHT;
                else if(angle > 67.5) return DIRECTION_DOWN;
                else return DIRECTION_DOWNRIGHT;
            }
            else
            {
                if(angle < 22.5) return DIRECTION_RIGHT;
                else if(angle > 67.5) return DIRECTION_UP;
                else return DIRECTION_UPRIGHT;
            }
        }
        else
        {
            if(shiftY > 0)
            {
                if(angle < 22.5) return DIRECTION_LEFT;
                else if(angle > 67.5) return DIRECTION_DOWN;
                else return DIRECTION_DOWNLEFT;
            }
            else
            {
                if(angle < 22.5) return DIRECTION_LEFT;
                else if(angle > 67.5) return DIRECTION_UP;
                else return DIRECTION_UPLEFT;
            }
        }

        /*
        if(shiftX > 0)
        {
            if(shiftY < 0)
                return DIRECTION_UPRIGHT;
            else if(shiftY > 0)
                return DIRECTION_DOWNRIGHT;
            else
                return DIRECTION_RIGHT;
        }
        else if(shiftX < 0)
        {
            if(shiftY < 0)
                return DIRECTION_UPLEFT;
            else if(shiftY > 0)
                return DIRECTION_DOWNLEFT;
            else
                return DIRECTION_LEFT;
        }
        else
        {
            if(shiftY < 0)
                return DIRECTION_UP;
            else if(shiftY > 0)
                return DIRECTION_DOWN;
            else
                return -1;
        }*/
    }

}
/*    Paladin(int _aPositionLocation, int _aTextureLocation)
    {
        aPositionLocation = _aPositionLocation;
        aTextureLocation = _aTextureLocation;
        paladinTextures = new float[256];
        float width = 882, height = 808;
        int elementsHeight = 8, elementsWidth = 10;
        int elWidth = 82, elHeight = 98, offset = 53;
        int j = 0;
        for (int row = 0; row < 8; row++)
            for(int i = 10 - FRAMES_COUNT; i < 10; i++)
            {
                paladinTextures[j++] = (elWidth*i + offset)/(float)width; paladinTextures[j++] = (elHeight*row)/(float)height;
                paladinTextures[j++] = (elWidth*i + offset)/(float)width; paladinTextures[j++] = (elHeight*(row+1))/(float)height;    // Texture unwrapping on Triangles_Strips(square)(2, 3, 1, 4 quaters)
                paladinTextures[j++] = (elWidth*(i+1) + offset)/(float)width; paladinTextures[j++] = (elHeight*row)/(float)height;
                paladinTextures[j++] = (elWidth*(i+1) + offset)/(float)width; paladinTextures[j++] = (elHeight*(row+1))/(float)height;
            }

        verticesCoordinates = new float [] {
                //coordinates for paladin
                // X, Y, Z
                0.85f, 1, 0,
                0.85f, 0, 0,
                0, 1, 0,
                0, 0, 0
        };
    }

    public void SetTexture(int index, int direction) // Current 0-4
    {
        // Generate vertex(position + texels) array
        vertices = new float[(POSITION_COUNT + TEXTURE_COUNT) * 4];
        int j = 0;
        for(int i = 0; i<4; i++)
        {
            vertices[j++] = verticesCoordinates[0 + i*POSITION_COUNT];
            vertices[j++] = verticesCoordinates[1 + i*POSITION_COUNT];
            vertices[j++] = verticesCoordinates[2 + i*POSITION_COUNT];
            vertices[j++] = paladinTextures[0 + i*TEXTURE_COUNT + index*(4*2) + direction*(4*2)*FRAMES_COUNT]; // 4 * 2 D texels
            vertices[j++] = paladinTextures[1 + i*TEXTURE_COUNT + index*(4*2) + direction*(4*2)*FRAMES_COUNT];
        }*/