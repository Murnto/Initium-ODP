package com.universeprojects.miniup.server.commands;

import com.universeprojects.cacheddatastore.CachedDatastoreService;
import com.universeprojects.cacheddatastore.CachedEntity;
import com.universeprojects.miniup.server.GameUtils;
import com.universeprojects.miniup.server.HtmlComponents;
import com.universeprojects.miniup.server.ODPDBAccess;
import com.universeprojects.miniup.server.commands.framework.Command;
import com.universeprojects.miniup.server.commands.framework.UserErrorMessage;
import com.universeprojects.miniup.server.entities.Character;
import com.universeprojects.miniup.server.entities.Item;
import com.universeprojects.miniup.server.entities.SaleItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class CommandStoreBuyItem extends Command {

	public CommandStoreBuyItem(ODPDBAccess db, HttpServletRequest request, HttpServletResponse response) {
		super(db, request, response);
	}

	public void run(Map<String, String> parameters) throws UserErrorMessage {

		ODPDBAccess db = getDB();
		CachedDatastoreService ds = getDS();

		Long saleItemId = Long.parseLong(parameters.get("saleItemId"));
		Long characterId = Long.parseLong(parameters.get("characterId"));


		Character character = db.getCurrentCharacter();
		SaleItem saleItem = db.getSaleItem(saleItemId);
		if (saleItem == null)
			throw new UserErrorMessage("This item has been taken down. The owner is no longer selling it.");
		Item item = saleItem.getItem(db);
		CachedEntity storeCharacter = db.getEntity("Character", characterId);
		if (item == null)
			throw new UserErrorMessage("The item being sold has been removed.");


		boolean isPremiumMembership = false;
		if ("Initium Premium Membership".equals(item.getName()))
			isPremiumMembership = true;

		addCallbackData("createStoreItem", HtmlComponents.generateStoreItemHtml(db, character, storeCharacter, item, saleItem, request));

		if (saleItem.isSold())
			throw new UserErrorMessage("The owner of the store has already sold this item.");
		if (saleItem.isHidden())
			throw new UserErrorMessage("The owner of the store is not selling this item at the moment.");

		Long cost = saleItem.getDogecoins();
		if (cost == null)
			throw new UserErrorMessage("The sale item is not setup properly. It has no cost.");


		Character sellingCharacter = saleItem.getSellingCharacter(db);
		if (!sellingCharacter.isMode(ODPDBAccess.CHARACTER_MODE_MERCHANT))
			throw new UserErrorMessage("The owner of the store is not selling at the moment.");
		if (!isPremiumMembership && !GameUtils.equals(sellingCharacter.getLocationKey(), character.getLocationKey()))
			throw new UserErrorMessage("You are not in the same location as the seller. You can only buy from a merchant who is in the same location as you.");

		if (GameUtils.equals(character.getKey(), sellingCharacter.getKey()))
			throw new UserErrorMessage("You cannot buy items from yourself.");

		Double storeSale = sellingCharacter.getStoreSale();
		if (storeSale == null) storeSale = 100d;

		cost = Math.round(cost.doubleValue() * (storeSale / 100));

		if (cost > character.getDogecoins())
			throw new UserErrorMessage("You do not have enough funds to buy this item. You have " + character.getDogecoins() + " and it costs " + saleItem.getDogecoins() + ".");
		if (item.getContainerKey().getId() != sellingCharacter.getKey().getId())
			throw new UserErrorMessage("The item you tried to buy is not actually in the seller's posession. Purchase has been cancelled.");


		if (cost < 0)
			throw new UserErrorMessage("You cannot buy a negatively priced item.");

		ds.beginTransaction();
		try {
			saleItem.setStatus("Sold");
			saleItem.setSoldToKey(character.getKey());
			sellingCharacter.setDogecoins(sellingCharacter.getDogecoins() + cost);
			character.setDogecoins(character.getDogecoins() - cost);
			item.setContainerKey(character.getKey());
			item.setMovedTimestamp(new Date());

			ds.put(saleItem);
			ds.put(sellingCharacter);
			ds.put(character);
			ds.put(item);

			ds.commit();

			addCallbackData("createStoreItem", HtmlComponents.generateStoreItemHtml(db, character, storeCharacter, item, saleItem, request));
		} finally {
			ds.rollbackIfActive();
		}
	}
}