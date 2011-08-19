package com.zoltu.MovieReleases.server;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.googlecode.objectify.ObjectifyService;

public abstract class MyLocator<EntityType, IdType> extends Locator<EntityType, IdType>
{
	@Override
	public EntityType create(Class<? extends EntityType> pClass)
	{
		try
		{
			return pClass.newInstance();
		}
		catch (InstantiationException pException)
		{
			throw new RuntimeException(pException);
		}
		catch (IllegalAccessException pException)
		{
			throw new RuntimeException(pException);
		}
	}
	
	@Override
	public Class<EntityType> getDomainType()
	{
		assert false : "This code is supposedly never called.  If it is called then this method needs to be overriden by derived classes.";
		throw new RuntimeException(
				"MyLocator.getDomainType was called even though *supposedly* it shouldn't be.  To resolve override this method in derived classes.");
	}
	
	public abstract static class WithStringId<EntityType> extends MyLocator<EntityType, String>
	{
		@Override
		public EntityType find(Class<? extends EntityType> pClass, String pId)
		{
			return ObjectifyService.begin().find(pClass, pId);
		}
		
		@Override
		public Class<String> getIdType()
		{
			return String.class;
		}
	}
	
	public abstract static class WithLongId<EntityType> extends MyLocator<EntityType, Long>
	{
		@Override
		public EntityType find(Class<? extends EntityType> pClass, Long pId)
		{
			return ObjectifyService.begin().find(pClass, pId);
		}
		
		@Override
		public Class<Long> getIdType()
		{
			return Long.class;
		}
	}
}
