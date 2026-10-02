package com.Igor.CarSystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.model.ClientReceipt;
import com.Igor.CarSystem.repo.ClientReceiptRepository;
import com.Igor.CarSystem.repo.ClientRepository;



@Service
public class ClientReceiptServiceImpl implements ClientReceiptService, Facade {

	private static final Logger log = LoggerFactory.getLogger(ClientReceiptServiceImpl.class);


	@Autowired
	private ClientReceiptRepository clientReceiptRepository;

	@Autowired
	private ClientRepository clientRepository;
	

	//Store Receipt
	@Override
	public ClientReceipt takeReceipt(ClientReceipt clientReceipt) throws Exception {
		log.debug("************************StartTakeReceipt************************");
		try {
			ClientReceipt saved = clientReceiptRepository.save(clientReceipt);
			log.info("Receipt stored: {}", saved);
			log.debug("************************EndTakeReceipt************************");
			return saved;
		} catch (Exception e) {
			log.error("Failed to store Receipt {}: {}", clientReceipt, e.getMessage());
			throw new Exception("Failed to store Receipt: " + e.getMessage());
		}

	}

	//Get All Receipts
	public List<ClientReceipt> getAllReceipts() throws Exception {
		log.debug("************************StartGetAllReceipts************************");
		List<ClientReceipt> receipts = null;
		try {
			if (clientReceiptRepository.findAll().isEmpty()) {
				throw new Exception("Failed to get all receipts! Data is empty.");
			} else {
				receipts = clientReceiptRepository.findAll();
				log.debug("Success on get all receipts: " + receipts);
				log.debug("************************EndGetAllReceipts************************");
				return receipts;
			}
		} catch (Exception e) {
			log.error("Failed to get all receipts " + e.getMessage());
			throw new Exception("Failed to get all receipts " + e.getMessage());
		}
	}

	//Get Receipts By Client
	public List<ClientReceipt> getReceiptsByClient(int clientId) throws Exception {
		log.debug("************************StartGetReceiptsByClient************************");
		Client client = clientRepository.findById(clientId).get();
		List<ClientReceipt> receiptsByClient = null;
		try {
			if (clientReceiptRepository.findAllByClientId(client.getId()).isEmpty()) {
				throw new Exception("Failed to get all receipts by client! Data is empty.");
			} else {
				receiptsByClient = clientReceiptRepository.findAllByClientId(client.getId());
				log.debug("Success on get receipts by Client " + client.getName() + ": " + receiptsByClient);
				log.debug("************************EndGetReceiptsByClient************************");
				return receiptsByClient;
			}
		} catch (Exception e) {
			log.error("Failed to get all receipts by client " + e.getMessage());
			throw new Exception("Failed to get all receipts by client " + e.getMessage());
		}
	}


}
