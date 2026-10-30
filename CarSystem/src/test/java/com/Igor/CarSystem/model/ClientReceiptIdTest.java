package com.Igor.CarSystem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** The receipt ID counter continues after the IDs already stored, so new receipts never reuse one. */
class ClientReceiptIdTest {

	@Test
	void continuesAfterStoredIds() {
		ClientReceipt.continueAfter(1000);
		assertEquals(1001, ClientReceipt.incrementId());
		assertEquals(1002, ClientReceipt.incrementId());
	}

	@Test
	void neverMovesBack() {
		long next = ClientReceipt.incrementId() + 1;
		ClientReceipt.continueAfter(0);
		assertTrue(ClientReceipt.incrementId() >= next);
	}
}
