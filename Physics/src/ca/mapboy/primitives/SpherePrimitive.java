package ca.mapboy.primitives;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import ca.mapboy.util.Colour;
import ca.mapboy.util.Vec3;
import static org.lwjgl.opengl.GL11.*;

public class SpherePrimitive extends Primitive {
	private Sphere sphere;
	public float radius;
	public Colour colour;
	
	private Vec3 rotation;
	
	public SpherePrimitive(Vec3 position, float radius, Colour colour){
		super(position);
		sphere = new Sphere();
		
		this.colour = colour;
		this.position = position;
		this.radius = radius;
	}
	
	public void render(){
		Transform trans = new Transform();
		rigidbody.getMotionState().getWorldTransform(trans);
		
		position = new Vec3(trans.origin.x, trans.origin.y, trans.origin.z);
		
		glTranslatef(position.x, position.y, position.z);
		colour.bind(); 
		sphere.draw(radius, 20, 20);
		glTranslatef(-position.x, -position.y, -position.z);
	}
	
	public void generatePhysics(DiscreteDynamicsWorld dynamicsWorld){
		CollisionShape groundShape = new SphereShape(radius);
		DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(position.x, position.y, position.z), 1.0f)));
		
		int mass = 1;

	    Vector3f fallInertia = new Vector3f(0,0,0); 
	    groundShape.calculateLocalInertia(mass,fallInertia);

	    RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(mass,groundMotionState,groundShape,fallInertia); 
		rigidbody = new RigidBody(fallRigidBodyCI); 
		
		rigidbody.setRestitution(0.6f);
		rigidbody.applyGravity();
		
		dynamicsWorld.addRigidBody(rigidbody);
	}
}
