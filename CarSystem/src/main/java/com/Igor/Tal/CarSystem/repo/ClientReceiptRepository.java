package com.Igor.Tal.CarSystem.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Igor.Tal.CarSystem.model.ClientReceipt;

@Repository
public interface ClientReceiptRepository extends MongoRepository<ClientReceipt, Long> {
	
	public boolean existsByClientName(String clientName);
	
	public ClientReceipt findByClientId(int clientId);

	public List<ClientReceipt> findAllByClientId(int clientId);
	
	public List<ClientReceipt> findAllByClientIdAndReceiptDateLessThanEqual(int clientId, String receiptDate);

}
