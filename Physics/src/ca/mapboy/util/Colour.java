package ca.mapboy.util;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

public class Colour {
	public double red, green, blue, alpha;
	
	public Colour(double red, double green, double blue, double alpha){
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void bind(){
		glColor4d(red, green, blue, alpha);
	}
	
	public static Colour randomColour(){
		Random random = new Random();
		
		return new Colour(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
	}
}
