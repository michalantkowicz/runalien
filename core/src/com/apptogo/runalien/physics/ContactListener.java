package com.apptogo.runalien.physics;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener
{
	public Map<Integer, Integer> contacts = new HashMap<Integer, Integer>();
	
	@Override
	public void beginContact(Contact contact)
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		int ida = ((UserData)fa.getUserData()).getId();
		int idb = ((UserData)fb.getUserData()).getId();
		
		if( contacts.get(ida) == null )
		{
			contacts.put(ida, idb);
			contacts.put(idb, ida);
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
	}
	
	private boolean checkFixturesTypes(Fixture fixtureA, Fixture fixtureB, String typeA, String typeB)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if //wiem polecialem ale tego gowna nie da sie inaczej rozczytac
			(
				( 
						((UserData)fixtureA.getUserData()).key.equals( typeA ) 
						&& 
						((UserData)fixtureB.getUserData()).key.equals( typeB ) 
				) 
				|| 
				( 
						((UserData)fixtureA.getUserData()).key.equals( typeB ) 
						&& 
						((UserData)fixtureB.getUserData()).key.equals( typeA ) 
				)
			)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkIsOneOfType(Fixture fixtureA, Fixture fixtureB, String type)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if
			(
					((UserData)fixtureA.getUserData()).key.equals( type ) 
					||
					((UserData)fixtureB.getUserData()).key.equals( type ) 
			)
			{
				return true;
			}
		}
		
		return false;
	}
		
	private Fixture getFixtureByType(Fixture fixtureA, Fixture fixtureB, String type)
	{
		return ((UserData)fixtureA.getUserData()).key.equals(type) ? fixtureA : fixtureB;
	}
}
