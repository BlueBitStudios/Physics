package ca.mapboy.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import ca.mapboy.entity.Player;
import ca.mapboy.physics.PhysicsSpace;
import ca.mapboy.primitives.Primitive;
import ca.mapboy.primitives.RectanglePrimitive;
import ca.mapboy.primitives.SpherePrimitive;
import ca.mapboy.util.Colour;
import ca.mapboy.util.Quat4;
import ca.mapboy.util.ShaderLoader;
import ca.mapboy.util.Vec3;

public class Game {
	public int width, height;
	public String name;
	
	public PhysicsSpace physics;
	public Player player;
	
	public static ArrayList<Primitive> primitives = new ArrayList<Primitive>();
	
	long lastFrame;
    int fps;
	long lastFPS;
	
	private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    private void updateFPS() {
		if (getTime() - lastFPS > 1000) {
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

	public Game(String name, int width, int height) {
		this.width = width;
		this.height = height;
		this.name = name;
		
		lastFrame = getTime();
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(name);
			Display.setResizable(true);
			
			PixelFormat format = new PixelFormat().withDepthBits(24).withSamples(4).withSRGB(true);
			Display.create(format);
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		init();

		while (!Display.isCloseRequested()) {
			update(getDelta());
			render();
			
			Display.sync(60);
			Display.update();
		}

		exit(false);
	}

	public void exit(boolean asCrash) {
		AL.destroy();
		Display.destroy();

		System.exit(asCrash ? 0 : 1);
	}
	
	
	public void init(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(60f, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[] {0.05f, 0.05f, 0.05f, 1f}));
		glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[] {0, 0, 0, 1}));
		
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		
		player = new Player(new Vec3(0, 10, 0), new Quat4(0, 0, 0, 1));
		
		primitives.add(rect);
		primitives.add(rect2);
		primitives.add(rect3);
		primitives.add(rect4);
		primitives.add(rect5);
		
		physics = new PhysicsSpace(new Vec3(0, -2, 0));
		
		rect.generatePhysics(physics.dynamicsWorld);
		rect2.generatePhysics(physics.dynamicsWorld);
		rect3.generatePhysics(physics.dynamicsWorld);
		rect4.generatePhysics(physics.dynamicsWorld);
		rect5.generatePhysics(physics.dynamicsWorld);
		
		player.generateRigidbody(physics.dynamicsWorld);
		
	}

	/* --------------- */
	
	private float zNear = 0.001f;
	private float zFar = 55f;
	
	public void update(int delta) {
        glBindTexture(GL_TEXTURE_2D, 0);

        glLoadIdentity();
        glRotatef(player.rotation.x, 1, 0, 0);
        glRotatef(player.rotation.y, 0, 1, 0);
        glRotatef(player.rotation.z, 0, 0, 1);
        glTranslatef(player.position.x, -player.position.y, player.position.z);
        
        physics.updatePhysics();
        player.update(delta);
        
        while (Mouse.next()) {
            if (Mouse.isButtonDown(0)) {
                Mouse.setGrabbed(true);
            }
            if (Mouse.isButtonDown(1)) {
                Mouse.setGrabbed(false);
            }
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            exit(false);
        }
        
        if (Display.wasResized()) {
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            GLU.gluPerspective(60, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        }
        
        
        updateFPS();
	}

	RectanglePrimitive rect = new RectanglePrimitive(new Vec3(0, -1, 0), new Vec3(20, 0.5f, 20), new Colour(0.2, 0.4, 0.2, 1));
	RectanglePrimitive rect2 = new RectanglePrimitive(new Vec3(10, 4, 0), new Vec3(0.5f, 5f, 19.5f), new Colour(0.4, 0.4, 0.4, 1));
	RectanglePrimitive rect3 = new RectanglePrimitive(new Vec3(0, 4, 10), new Vec3(19.5f, 5f, 0.5f), new Colour(0.4, 0.4, 0.4, 1));
	RectanglePrimitive rect4 = new RectanglePrimitive(new Vec3(-10, 4, 0), new Vec3(0.5f, 5f, 19.5f), new Colour(0.4, 0.4, 0.4, 1));
	RectanglePrimitive rect5 = new RectanglePrimitive(new Vec3(0, 4, -10), new Vec3(19.5f, 5f, 0.5f), new Colour(0.4, 0.4, 0.4, 1));
	
	public static FloatBuffer asFloatBuffer(float[] values){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	
	Random random = new Random();
	
	int counter = 0;
	public void render() {
		
		counter++;
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
		glClearColor(0.2f, 0.4f, 0.8f, 1);
		
		glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[] { -player.position.x, player.position.y, -player.position.z, 1}));
		
		if(counter % 20 == 0){
			Primitive sphere = new SpherePrimitive(new Vec3(random.nextInt(18) - 9, 20, random.nextInt(18) - 9), 0.5f, Colour.randomColour());
			sphere.generatePhysics(physics.dynamicsWorld);
			primitives.add(sphere);
		}
		
		for(Primitive primitive : primitives){
			primitive.render();
		}
	}
}
