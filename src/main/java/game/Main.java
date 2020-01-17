package game;

import assets.Assets;
import gui.Gui;
import io.Timer;
import io.Window;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Shader;
import world.TileRenderer;
import world.World;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;

public class Main {

    public Main() {
        Window.setCallbacks();

        if (!glfwInit()) {
            System.err.println("GLFW Failed to initialize!");
            System.exit(1);
        }

        Window win = new Window();
        win.setSize(640, 480);
        win.setFullscreen(false);
        win.createWindow("Game");

        GL.createCapabilities();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_BLEND);

        Camera camera = new Camera(win.getWidth(), win.getHeight());
        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();
        Assets.initAsset();

//        float[] vertices = new float[]{
//                -0.5f, 0.5f, 0, //top left      0
//                0.5f, 0.5f, 0, //top right      1
//                0.5f, -0.5f, 0, //bottom right  2
//                -0.5f, -0.5f, 0, //bottom left  3
//
//        };
//
//        float[] texture = new float[]{
//                0, 0,
//                1, 0,
//                1, 1,
//                0, 1,
//        };
//
//        int[] indices = new int[]{
//                0, 1, 2,
//                2, 3, 0
//        };
//
//        Model model = new Model(vertices, texture, indices);
        Shader shader = new Shader("shader");

        World world = new World("test_level", camera);
        world.calculateView(win);

        Gui gui = new Gui(win);

        double frameCap = 1.0 / 60.0;

        double frameTime = 0;
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        while (!win.shouldClose()) {
            boolean canRender = false;

            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed += passed;
            frameTime += passed;

            time = time2;

            while (unprocessed >= frameCap) {
                if (win.hasResized()) {
                    camera.setProjection(win.getWidth(), win.getHeight());
                    gui.resizeCamera(win);
                    world.calculateView(win);
                    glViewport(0, 0, win.getWidth(), win.getHeight());
                }

                unprocessed -= frameCap;
                canRender = true;

                if (win.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(win.getWindow(), true);
                }


                world.update((float) frameCap, win, camera);

                world.correctCamera(camera, win);

                win.update();

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);

//                shader.bind();
//                shader.setUniform("sampler",0);
//                shader.setUniform("projection", camera.getProjection().mul(target));
//                model.render();
//                tex.bind(0);

                world.render(tiles, shader, camera);

                gui.render();

                win.swapBuffers();
                frames++;
            }
        }
        Assets.deleteAsset();

        glfwTerminate();
    }

    public static void main(String[] args) {

        new Main();

//        if (!glfwInit()){
//            throw new IllegalStateException("Failed to initialize GLFW!");
//        }
//
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//        long window = glfwCreateWindow(640,480,"My game",0,0);
//
//        if (window == 0){
//            throw new IllegalStateException("Failed to create window!");
//        }
//
//        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//        glfwSetWindowPos(window,(videoMode.width() - 640)/2,
//                (videoMode.height() - 480)/2);
//
//        glfwShowWindow(window);
//
//        while (!glfwWindowShouldClose(window)){
//            glfwPollEvents();
//        }
//
//        glfwTerminate();
    }
}
