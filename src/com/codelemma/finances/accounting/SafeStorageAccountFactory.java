package com.codelemma.finances.accounting;

import com.codelemma.finances.accounting.Storage.StorageException;

/**
 * Account factory(dp) which creates Account objects based on the data read from provided Storage.
 * It inherits the default error handling policy from SafeAccountFactory and extends it by
 * attempting to clear storage content if parsing error occurs. This way the application
 * is not doomed and storage functionality will be restored albeit at the cost of losing data
 * currently stored.
 */
public class SafeStorageAccountFactory extends SafeAccountFactory {
	private Storage storage;

	public SafeStorageAccountFactory(Storage storage) {
		super(new StorageAccountFactory(storage));
		this.storage = storage;
	}
	
	@Override
	protected void attemptToFix(AccountCreationException exception) {
		try {
			storage.clear();
		} catch (StorageException se) {
			// If we can't clear storage, we'll just have to live with broken one.
		}
	}
}