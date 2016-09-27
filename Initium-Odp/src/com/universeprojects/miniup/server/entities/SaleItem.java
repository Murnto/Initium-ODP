package com.universeprojects.miniup.server.entities;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.universeprojects.cacheddatastore.CachedEntity;
import com.universeprojects.miniup.server.ODPDBAccess;

public class SaleItem extends CachedEntity {
	private static final String HIDDEN = "Hidden";
	private static final String SOLD = "Sold";

	public SaleItem(Key key) {
		super(key);
	}

	protected SaleItem(Entity entity) {
		super(entity);
	}

	public static SaleItem wrap(CachedEntity cachedEntity) {
		if (cachedEntity == null) {
			return null;
		}

		if (cachedEntity instanceof SaleItem) {
			return (SaleItem) cachedEntity;
		}

		return new SaleItem(cachedEntity.getEntity());
	}

	public boolean isHidden() {
		return this.getStatus().equals(HIDDEN);
	}

	public boolean isSold() {
		return this.getStatus().equals(SOLD);
	}

	public String getStatus() {
		return (String) this.getProperty("status");
	}

	public void setStatus(String status) {
		this.setProperty("status", status);
	}

	public Long getDogecoins() {
		return (Long) this.getProperty("dogecoins");
	}

	public void setDogecoins(Long dogecoins) {
		this.setProperty("dogecoins", dogecoins);
	}

	public Key getItemKey() {
		return (Key) this.getProperty("itemKey");
	}

	public void setItemKey(Key itemKey) {
		this.setProperty("itemKey", itemKey);
	}

	public Item getItem(ODPDBAccess db) {
		return db.getItem(this.getItemKey());
	}

	public void setItem(Item item) {
		this.setItemKey(item.getKey());
	}

	public Key getSellingCharacterKey() {
		return (Key) this.getProperty("characterKey");
	}

	public void setSellingCharacterKey(Key sellingCharacterKey) {
		this.setProperty("characterKey", sellingCharacterKey);
	}

	public Character getSellingCharacter(ODPDBAccess db) {
		return db.getCharacter(this.getSellingCharacterKey());
	}

	public void setSellingCharacter(Character sellingCharacter) {
		this.setSellingCharacterKey(sellingCharacter.getKey());
	}

	public Key getSoldToKey() {
		return (Key) this.getProperty("soldTo");
	}

	public void setSoldToKey(Key soldToKey) {
		this.setProperty("soldTo", soldToKey);
	}
}
