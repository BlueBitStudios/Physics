package ca.mapboy.primitives;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;

import ca.mapboy.util.Vec3;

public class Primitive {
	public Vec3 position;
	public RigidBody rigidbody;
	
	public Primitive(Vec3 position){
		this.position = position;
	}
	
	public void render(){
		
	}
	
	public void generatePhysics(DiscreteDynamicsWorld dynamicsWorld){
		
	}
}
