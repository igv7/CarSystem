package com.Igor.Tal.CarSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.Tal.CarSystem.model.Car;
import com.Igor.Tal.CarSystem.model.Client;
import com.Igor.Tal.CarSystem.model.ClientReceipt;
import com.Igor.Tal.CarSystem.repo.CarRepository;
import com.Igor.Tal.CarSystem.repo.ClientReceiptRepository;
import com.Igor.Tal.CarSystem.repo.ClientRepository;
import com.Igor.Tal.CarSystem.utils.DateFormatter;



@Service
public class ClientServiceImpl implements ClientService, Facade {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private ClientReceiptRepository clientReceiptRepository;

	@Autowired
	private ClientReceiptServiceImpl clientReceiptServiceImpl;

	private int clientId;

	@Override
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}


	// Add Car
	@Override
	public Car getCar(int id) throws Exception {
		System.out.println("************************StartClientGetCar************************");
		Client client = clientRepository.findById(clientId).get();
		System.out.println(client);
		Car car = null;
		Optional<Car> optional = carRepository.findById(id);
		try {
			car = optional.get();
			System.out.println("This car to get: " + car);

			if (client.getBalance() <= -1.0) {
				throw new Exception("Your balance is in the red! Please replenish your account.");
			}

			if (car.getAmount() <= 0) {
				throw new Exception("Client failed to get car - wrong amount: " + car.getAmount());
			}

			System.out.println("(client.getCars().contains(car)) = " +(client.getCars().contains(car)));
			if (client.getCars().contains(car)) {
				throw new Exception(
						"Client " + client.getName() + " unable to get car id: " + id + " - already got same car. ");
			}

			if (optional.isPresent()) {
				car = carRepository.getOne(id);
				if (car.getAmount() > 0) {
					client = clientRepository.getOne(clientId);
					car.setAmount(car.getAmount() - 1);
					client.getCars().add(car);
					client.setBalance(client.getBalance() - car.getPrice());
					clientRepository.save(client);
					carRepository.save(car);
					ClientReceipt clientReceipt = new ClientReceipt();
					clientReceipt.setReceiptId(ClientReceipt.incrementId());
					clientReceipt.setClientId(client.getId());
					clientReceipt.setClientName(client.getName());
					clientReceipt.setClientPhoneNumber(client.getPhoneNumber());
					clientReceipt.setClientEmail(client.getEmail());
					clientReceipt.setReceiptDate(DateFormatter.getCurrentDate());
					clientReceipt.setCarId(car.getId());
					clientReceipt.setCarNumber(car.getNumber());
					clientReceipt.setCarColor(car.getColor());
					clientReceipt.setCarType(car.getType());
					clientReceipt.setCarPrice(car.getPrice());
					clientReceiptServiceImpl.takeReceipt(clientReceipt);
					System.out.println("Success. Car id: " + car.getId() + " number: " + car.getNumber()
							+ " was added by Client id: " + client.getId() + " name: " + client.getName());
					System.out.println("************************EndClientGetCar************************");
					return car;
				}
			} else {
				throw new Exception("Car does not exixts");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Failed to get car!");
		}
		return null;
	}

	
	// Get All Client Receipts By ReceiptDate (until)
	public List<ClientReceipt> getAllClientReceiptsByDate(String receiptDate) throws Exception {
		System.out.println("************************StartGetAllClientReceiptsByDate************************");
		Client client = clientRepository.findById(clientId).get();
		List<ClientReceipt> receiptsByDate = null;
		try {
			if (clientReceiptRepository.findAllByClientId(client.getId()).isEmpty()) {
				throw new Exception("Failed to get all " + client.getName() + " receipts! Data is empty.");
			} else {
				receiptsByDate = clientReceiptRepository.findAllByClientIdAndReceiptDateLessThanEqual(client.getId(), receiptDate);
				System.out.println("Success on get all Client Receipts by receipt date. Client name: " + client.getName()
						+ ", receipt date until: " + receiptDate + ": " + receiptsByDate);
				System.out.println("************************EndGetAllClientReceiptsByDate************************");
				return receiptsByDate;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all Client Receipts by date until " + receiptDate);
		}

	}


}
