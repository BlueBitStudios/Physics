package ca.mapboy.util;

public class Quat4 {
	public float x, y, z, w;
	
	public Quat4(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public static Quat4 identity(){
		return new Quat4(0, 0, 0, 1);
	}
	
	public javax.vecmath.Quat4f vecMathQuaternion(){
		return new javax.vecmath.Quat4f(x, y, z, w);
	}
}
