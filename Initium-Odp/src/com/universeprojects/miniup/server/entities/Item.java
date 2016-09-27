package com.universeprojects.miniup.server.entities;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.universeprojects.cacheddatastore.CachedEntity;

import java.util.Date;

public class Item extends CachedEntity {

	public Item(Key key) {
		super(key);
	}

	protected Item(Entity entity) {
		super(entity);
	}

	public static Item wrap(CachedEntity cachedEntity) {
		if (cachedEntity == null) {
			return null;
		}

		if (cachedEntity instanceof Item) {
			return (Item) cachedEntity;
		}

		return new Item(cachedEntity.getEntity());
	}

	public String getName() {
		return (String) this.getProperty("name");
	}

	public void setName(String name) {
		this.setProperty("name", name);
	}

	public Key getContainerKey() {
		return (Key) this.getProperty("containerKey");
	}

	public void setContainerKey(Key containerKey) {
		this.setProperty("containerKey", containerKey);
	}

	public Date getMovedTimestamp() {
		return (Date) this.getProperty("movedTimestamp");
	}

	public void setMovedTimestamp(Date movedTimestamp) {
		this.setProperty("movedTimestamp", movedTimestamp);
	}
}
