package com.android.quack.surface;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLHelper {
    private String mVertex = "precision highp float;\n" +
            "precision highp int;\n" +
            "attribute vec4 aVertexCo;\n" +
            "attribute vec2 aTextureCo;\n" +
            "\n" +
            "uniform mat4 uVertexMatrix;\n" +
            "uniform mat4 uTextureMatrix;\n" +
            "\n" +
            "varying vec2 vTextureCo;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = uVertexMatrix*aVertexCo;\n" +
            "    vTextureCo = (uTextureMatrix*vec4(aTextureCo,0,1)).xy;\n" +
            "}";

    private String mFragment = "precision mediump float;\n" +
            "varying vec2 vTextureCo;\n" +
            "uniform sampler2D uTexture;\n" +
            "void main() {\n" +
            "    vec2 coord = vec2(vTextureCo.x, 1.0 - vTextureCo.y);\n" +
            "    gl_FragColor = texture2D(uTexture, coord);\n" +
            "}";

    protected int mGLVertexCo;
    protected int mGLTextureCo;
    protected int mGLVertexMatrix;
    protected int mGLTextureMatrix;
    protected int mGLTexture;

    private float[] mVertexMatrix = getOriginalMatrix();
    private float[] mTextureMatrix = getOriginalMatrix();

    protected FloatBuffer mVertexBuffer;
    protected FloatBuffer mTextureBuffer;

    public static float[] getOriginalTextureCo() {
        return new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
    }

    /**
     * 初始化顶点数据
     */
    protected void initBuffer(int width, int height) {
        float w = width / 2;//起着定位作用
        float h = height / 2;
        float vertices[] = new float[] {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        ByteBuffer vertex = ByteBuffer.allocateDirect(32);
        vertex.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertex.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        ByteBuffer texture = ByteBuffer.allocateDirect(32);
        texture.order(ByteOrder.nativeOrder());
        mTextureBuffer = texture.asFloatBuffer();
        mTextureBuffer.put(getOriginalTextureCo());
        mTextureBuffer.position(0);
    }


    /**
     *创建OpenGL环境 绘制bitmap纹理
     * @param bitmap
     */
    public void drawToBitmap(Bitmap bitmap, int width, int height) {
        //清空屏幕
        GLES20.glClearColor(1, 1, 1, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        initBuffer(width, height);

        int mGLProgram = GlUtil.createProgram(mVertex, mFragment);
        mGLVertexCo = GLES20.glGetAttribLocation(mGLProgram, "aVertexCo");
        mGLTextureCo = GLES20.glGetAttribLocation(mGLProgram, "aTextureCo");
        mGLVertexMatrix = GLES20.glGetUniformLocation(mGLProgram, "uVertexMatrix");
        mGLTextureMatrix = GLES20.glGetUniformLocation(mGLProgram, "uTextureMatrix");
        mGLTexture = GLES20.glGetUniformLocation(mGLProgram, "uTexture");

        //绘制bitmap纹理texture
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glUseProgram(mGLProgram);
        GLES20.glUniformMatrix4fv(mGLVertexMatrix, 1, false, mVertexMatrix, 0);
        GLES20.glUniformMatrix4fv(mGLTextureMatrix, 1, false, mTextureMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glUniform1i(mGLTexture, 0);

        GLES20.glEnableVertexAttribArray(mGLVertexCo);
        GLES20.glVertexAttribPointer(mGLVertexCo, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mGLTextureCo);
        GLES20.glVertexAttribPointer(mGLTextureCo, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLVertexCo);
        GLES20.glDisableVertexAttribArray(mGLTextureCo);
    }

    public static float[] getOriginalMatrix(){
        return new float[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        };
    }
}
