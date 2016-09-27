package com.universeprojects.miniup.server.entities;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.universeprojects.cacheddatastore.CachedEntity;

public class Character extends CachedEntity {

	public Character(Key key) {
		super(key);
	}

	protected Character(Entity entity) {
		super(entity);
	}

	public static Character wrap(CachedEntity characterEntity) {
		if (characterEntity == null) {
			return null;
		}

		if (characterEntity instanceof Character) {
			return (Character) characterEntity;
		}

		return new Character(characterEntity.getEntity());
	}

	public boolean isMode(String mode) {
		return this.getMode().equals(mode);
	}

	public Long getDogecoins() {
		return (Long) this.getProperty("dogecoins");
	}

	public void setDogecoins(Long dogecoins) {
		this.setProperty("dogecoins", dogecoins);
	}

	public String getMode() {
		return (String) this.getProperty("mode");
	}

	public void setMode(String mode) {
		this.setProperty("mode", mode);
	}

	public Key getLocationKey() {
		return (Key) this.getProperty("locationKey");
	}

	public void setLocationKey(Key locationKey) {
		this.setProperty("locationKey", locationKey);
	}

	public Double getStoreSale() {
		return (Double) this.getProperty("storeSale");
	}

	public void setStoreSale(Double storeSale) {
		this.setProperty("storeSale", storeSale);
	}
}
