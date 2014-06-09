package ca.mapboy.physics;

import ca.mapboy.game.Game;
import ca.mapboy.primitives.Primitive;
import ca.mapboy.util.Vec3;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsSpace {
	public Vec3 gravity;
	public DiscreteDynamicsWorld dynamicsWorld;
	
	RigidBody fallRigidBody;
	
	public PhysicsSpace(Vec3 gravity){
		this.gravity = gravity;
		
		BroadphaseInterface broadphase = new DbvtBroadphase();
		DefaultCollisionConfiguration config = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CollisionDispatcher(config);
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, config);
		dynamicsWorld.setGravity(gravity.vecMathVector());
	}
	
	public void updatePhysics(){
		dynamicsWorld.stepSimulation(1/60.f, 10);
	}
}
