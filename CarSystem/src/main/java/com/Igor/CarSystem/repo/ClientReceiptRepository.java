package com.Igor.CarSystem.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Igor.CarSystem.model.ClientReceipt;

/** MongoDB access for rental receipts. */
@Repository
public interface ClientReceiptRepository extends MongoRepository<ClientReceipt, Long> {
	
	/** @return true if any receipt exists for a client with this name */
	public boolean existsByClientName(String clientName);
	
	/** A single receipt for the client; fails if the client has more than one. */
	public ClientReceipt findByClientId(int clientId);

	/** All receipts of the client. */
	public List<ClientReceipt> findAllByClientId(int clientId);

}
