package render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Model {

    private int drawCount;
    private int vId;
    private int tId;
    private int iId;

    public Model(float[] vertices, float[] texCoords, int[] indices){
        drawCount = indices.length;

        vId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vId);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(vertices),GL_STATIC_DRAW);

        tId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,tId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(texCoords), GL_STATIC_DRAW);

        iId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,iId);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    protected void finalize(){
        glDeleteBuffers(vId);
        glDeleteBuffers(tId);
        glDeleteBuffers(iId);
    }

    public void render(){
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER,vId);
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,tId);
        glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,iId);
        glDrawElements(GL_TRIANGLES,drawCount,GL_UNSIGNED_INT,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private FloatBuffer createBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
