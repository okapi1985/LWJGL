package render;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20C.*;

public class Shader {

    private int programObject;
    private int vs;
    private int fs;

    public Shader(String filename) {
        programObject = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename + ".vs"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(filename + ".fs"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }


        glAttachShader(programObject, vs);
        glAttachShader(programObject, fs);

        glBindAttribLocation(programObject, 0, "vertices");
        glBindAttribLocation(programObject, 1, "textures");

        glLinkProgram(programObject);
        if (glGetProgrami(programObject, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(programObject));
            System.exit(1);
        }
        glValidateProgram(programObject);
        if (glGetProgrami(programObject, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(programObject));
            System.exit(1);
        }
    }

    protected void finalize() {
        glDetachShader(programObject, vs);
        glDetachShader(programObject, fs);
        glDeleteShader(vs);
        glDeleteShader(fs);
        glDeleteProgram(programObject);
    }

    public void setUniform(String uniformName, int value) {
        int location = glGetUniformLocation(programObject, uniformName);
        if (location != -1) {
            glUniform1i(location, value);
        }
    }

    public void setUniform(String uniformName, Vector4f value) {
        int location = glGetUniformLocation(programObject, uniformName);
        if (location != -1) {
            glUniform4f(location, value.x, value.y, value.z, value.w);
        }
    }

    public void setUniform(String uniformName, Matrix4f value) {
        int location = glGetUniformLocation(programObject, uniformName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void bind() {
        glUseProgram(programObject);
    }

    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
            String line;
            while ((line = br.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string.toString();
    }
}
