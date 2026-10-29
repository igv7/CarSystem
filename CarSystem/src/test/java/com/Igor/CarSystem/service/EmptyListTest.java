package com.Igor.CarSystem.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.repo.CarRepository;
import com.Igor.CarSystem.repo.ClientReceiptRepository;

/** An empty table gives an empty list (shown as an empty page), not an error. No database needed. */
@ExtendWith(MockitoExtension.class)
class EmptyListTest {

	@Mock
	private CarRepository carRepository;

	@Mock
	private ClientReceiptRepository clientReceiptRepository;

	@InjectMocks
	private CarServiceImpl carService;

	@InjectMocks
	private ClientReceiptServiceImpl clientReceiptService;

	@Test
	void noCarsGivesEmptyList() throws Exception {
		when(carRepository.findAll()).thenReturn(new ArrayList<>());
		assertTrue(carService.getAllCars().isEmpty());
	}

	@Test
	void noCarsOfATypeGivesEmptyList() throws Exception {
		when(carRepository.findAllByType(CarType.AUDI)).thenReturn(new ArrayList<>());
		assertTrue(carService.getAllCarsByType(CarType.AUDI).isEmpty());
	}

	@Test
	void noReceiptsGivesEmptyList() throws Exception {
		when(clientReceiptRepository.findAll()).thenReturn(new ArrayList<>());
		assertTrue(clientReceiptService.getAllReceipts().isEmpty());
	}
}
