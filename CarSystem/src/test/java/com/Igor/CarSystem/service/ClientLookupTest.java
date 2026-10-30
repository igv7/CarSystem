package com.Igor.CarSystem.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Igor.CarSystem.exceptions.ClientDoesntExist;
import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.repo.CarRepository;
import com.Igor.CarSystem.repo.ClientReceiptRepository;
import com.Igor.CarSystem.repo.ClientRepository;

/** Admin lookups by client ID: a missing client is an error, a client without cars gives an empty list. No database needed. */
@ExtendWith(MockitoExtension.class)
class ClientLookupTest {

	private static final int CLIENT_ID = 7;

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private CarRepository carRepository;

	@Mock
	private ClientReceiptRepository clientReceiptRepository;

	@InjectMocks
	private AdminServiceImpl adminService;

	@InjectMocks
	private ClientReceiptServiceImpl clientReceiptService;

	@Test
	void missingClientThrowsClientDoesntExist() {
		when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());
		assertThrows(ClientDoesntExist.class, () -> adminService.getAllClientCars(CLIENT_ID));
	}

	@Test
	void missingClientReceiptsThrowsClientDoesntExist() {
		when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());
		assertThrows(ClientDoesntExist.class, () -> clientReceiptService.getReceiptsByClient(CLIENT_ID));
	}

	@Test
	void clientWithoutCarsGivesEmptyList() throws Exception {
		Client client = new Client();
		client.setId(CLIENT_ID);
		when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
		when(carRepository.findClientCar(CLIENT_ID)).thenReturn(new ArrayList<>());
		assertTrue(adminService.getAllClientCars(CLIENT_ID).isEmpty());
	}
}
