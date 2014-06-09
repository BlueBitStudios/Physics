package ca.mapboy.primitives;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import ca.mapboy.util.Colour;
import ca.mapboy.util.Vec3;

public class RectanglePrimitive extends Primitive {
	Vec3 size;
	Colour colour;
	
	public RectanglePrimitive(Vec3 position, Vec3 size, Colour colour){
		super(position);
		
		this.colour = colour;
		this.size = size;
	}
	
	public void render(){
		glDisable(GL_LIGHTING);
		glUseProgram(0);
		
		colour.bind();
		rect(position, size);
		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
		glColor3d(0, 0, 0);
		rect(position, size);
		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );

		glEnable(GL_LIGHTING);
	}
	
	private void rect(Vec3 position, Vec3 size){
		glCullFace(GL_FRONT);
		quad1(new Vec3(position.x - (size.x / 2), position.y - size.y, position.z - (size.z / 2)), new Vec3(size.x, size.y, 0));
		glCullFace(GL_BACK);
		quad1(new Vec3(position.x - (size.x / 2), position.y - size.y, position.z + (size.z / 2)), new Vec3(size.x, size.y, 0));

		quad1(new Vec3(position.x - (size.x / 2), position.y - size.y, position.z - (size.z / 2)), new Vec3(0, size.y, size.z));
		glCullFace(GL_FRONT);
		quad1(new Vec3(position.x + (size.x / 2), position.y - size.y, position.z - (size.z / 2)), new Vec3(0, size.y, size.z));

		quad2(new Vec3(position.x - (size.x / 2), position.y - size.y, position.z - (size.z / 2)), new Vec3(size.x, 0, size.z));
		glCullFace(GL_BACK);
		quad2(new Vec3(position.x - (size.x / 2), position.y, position.z - (size.z / 2)), new Vec3(size.x, 0, size.z));
	}
	
	private void quad1(Vec3 position, Vec3 size){
		glBegin(GL_QUADS);
			glVertex3f(position.x, position.y, position.z);
			glVertex3f(position.x + size.x, position.y, position.z + size.z);
			glVertex3f(position.x + size.x, position.y + size.y, position.z + size.z);
			glVertex3f(position.x, position.y + size.y, position.z);
		glEnd();
	}
	
	private void quad2(Vec3 position, Vec3 size){
		glBegin(GL_QUADS);
			glVertex3f(position.x, position.y, position.z);
			glVertex3f(position.x, position.y, position.z + size.z);
			glVertex3f(position.x + size.x, position.y + size.y, position.z + size.z);
			glVertex3f(position.x + size.x, position.y, position.z);
		glEnd();
	}
	
	public void generatePhysics(DiscreteDynamicsWorld dynamicsWorld){
		CollisionShape groundShape = new BoxShape(new Vector3f(size.x/2, size.y, size.z/2));
		DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(position.x, position.y-size.y, position.z), 1.0f)));
		RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
		rigidbody = new RigidBody(groundRigidBodyCI); 

		rigidbody.setRestitution(1.0f);
		dynamicsWorld.addRigidBody(rigidbody);
	}
}
