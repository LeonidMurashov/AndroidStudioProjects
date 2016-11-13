package ru.leonid.opengl_paladin;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

    float[] verticesCoordinates;
    float[] vertices;
    float[] paladinTextures;
    public int currTexture = 0;
    public boolean isRunning = true;

    Paladin(int _aPositionLocation, int _aTextureLocation)
    {
        aPositionLocation = _aPositionLocation;
        aTextureLocation = _aTextureLocation;
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

        verticesCoordinates = new float [] {
                //coordinates for paladin
                // X, Y, Z
                0.85f, 1, 0,
                0.85f, 0, 0,
                0, 1, 0,
                0, 0, 0
        };
    }

    public void SetTexture(int index) // Current 0-4
    {
        // Generate vertex(position + texels) array
        if(index >= 5)
            index %= 5;
        vertices = new float[20];
        int j = 0;
        for(int i = 0; i<4; i++)
        {
            vertices[j++] = verticesCoordinates[0 + i*3];
            vertices[j++] = verticesCoordinates[1 + i*3];
            vertices[j++] = verticesCoordinates[2 + i*3];
            vertices[j++] = paladinTextures[0 + i*2 + index*8 + direction * 40];
            vertices[j++] = paladinTextures[1 + i*2 + index*8 + direction * 40];
        }

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    void Draw()
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

        // Drawing
        glBindTexture(GL_TEXTURE_2D, tPaladinTexture);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    void SetAtlas(int _tPaladinTexture)
    {
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