package com.Igor.CarSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.model.ClientReceipt;
import com.Igor.CarSystem.repo.ClientReceiptRepository;
import com.Igor.CarSystem.repo.ClientRepository;



@Service
public class ClientReceiptServiceImpl implements ClientReceiptService, Facade {


	@Autowired
	private ClientReceiptRepository clientReceiptRepository;

	@Autowired
	private ClientRepository clientRepository;
	

	//Store Receipt
	@Override
	public ClientReceipt takeReceipt(ClientReceipt clientReceipt) throws Exception {
		System.out.println("************************StartTakeReceipt************************");
		try {
			System.out.println("Succsess. Receipt was stored " + clientReceipt);
			System.out.println("************************EndTakeReceipt************************");
			return clientReceiptRepository.save(clientReceipt);
		} catch (Exception e) {
			System.out.println("Failed to store Receipt: " + clientReceipt);
			throw new Exception("Failed to store Receipt: " + e.getMessage());
		}

	}

	//Get All Receipts
	public List<ClientReceipt> getAllReceipts() throws Exception {
		System.out.println("************************StartGetAllReceipts************************");
		List<ClientReceipt> receipts = null;
		try {
			if (clientReceiptRepository.findAll().isEmpty()) {
				throw new Exception("Failed to get all receipts! Data is empty.");
			} else {
				receipts = clientReceiptRepository.findAll();
				System.out.println("Success on get all receipts: " + receipts);
				System.out.println("************************EndGetAllReceipts************************");
				return receipts;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all receipts " + e.getMessage());
		}
	}

	//Get Receipts By Client
	public List<ClientReceipt> getReceiptsByClient(int clientId) throws Exception {
		System.out.println("************************StartGetReceiptsByClient************************");
		Client client = clientRepository.findById(clientId).get();
		List<ClientReceipt> receiptsByClient = null;
		try {
			if (clientReceiptRepository.findAllByClientId(client.getId()).isEmpty()) {
				throw new Exception("Failed to get all receipts by client! Data is empty.");
			} else {
				receiptsByClient = clientReceiptRepository.findAllByClientId(client.getId());
				System.out.println("Success on get receipts by Client " + client.getName() + ": " + receiptsByClient);
				System.out.println("************************EndGetReceiptsByClient************************");
				return receiptsByClient;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all receipts by client " + e.getMessage());
		}
	}


}
