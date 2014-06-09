package ca.mapboy.util;

public class Vec3 {
	public float x, y, z;
	
	public Vec3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3(Vec3 vec){
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public void scale(float scaleFactor){
		this.x *= scaleFactor;
		this.y *= scaleFactor;
		this.z *= scaleFactor;
	}
	
	public void sub(Vec3 otherVector){
		this.x -= otherVector.x;
		this.y -= otherVector.y;
		this.z -= otherVector.z;
	}
	
	public org.lwjgl.util.vector.Vector3f lwjglVector(){
		return new org.lwjgl.util.vector.Vector3f(x, y, z);
	}
	
	public javax.vecmath.Vector3f vecMathVector(){
		return new javax.vecmath.Vector3f(x, y, z);
	}
	
	public static Vec3 convert(javax.vecmath.Vector3f otherVec){
		return new Vec3(otherVec.x, otherVec.y, otherVec.z);
	}
	
	@Override
	public String toString(){
		return x + ", " + y + ", " + z;
	}
}
