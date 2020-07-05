package com.Igor.CarSystem.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Igor.CarSystem.model.ClientReceipt;

@Repository
public interface ClientReceiptRepository extends MongoRepository<ClientReceipt, Long> {
	
	public boolean existsByClientName(String clientName);
	
	public ClientReceipt findByClientId(int clientId);

	public List<ClientReceipt> findAllByClientId(int clientId);

}
