package com.Igor.CarSystem.service;

import com.Igor.CarSystem.model.ClientReceipt;

/** Storage of rental receipts. */
public interface ClientReceiptService {
	
	/** Saves a receipt. */
	public ClientReceipt takeReceipt(ClientReceipt clientReceipt) throws Exception;

}
