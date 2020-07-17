package com.Igor.CarSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.exceptions.ClientDoesntExist;
import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.model.ClientReceipt;
import com.Igor.CarSystem.repo.CarRepository;
import com.Igor.CarSystem.repo.ClientReceiptRepository;
import com.Igor.CarSystem.repo.ClientRepository;
import com.Igor.CarSystem.utils.DateFormatter;

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

			System.out.println("(client.getCars().contains(car)) = " + (client.getCars().contains(car)));
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
					clientReceipt.setClientBalance(client.getBalance());
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

	// Get Cars
	@Override
	public List<Car> getCars() throws Exception {
		System.out.println("************************StartGetCars************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get cars. The list is empty!");
			} else {
				cars = carRepository.findAll();
				System.out.println("Success on get Cars: " + cars);
				System.out.println("************************EndGetCars************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all cars");
		}
	}

	// Get My Cars
	public List<Car> getMyCars() throws Exception {
		System.out.println("************************StartGetMyCars************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> myCars = null;
		try {
			if (carRepository.findClientCar(client.getId()).isEmpty()) {
				throw new Exception("Failed to get My Cars. Client name: " + client.getName() + ", Cars: " + myCars
						+ " Data is empty.");
			} else {
				myCars = carRepository.findClientCar(client.getId());
				System.out.println("Success on get My Cars. Client name: " + client.getName() + ", Cars: " + myCars);
				System.out.println("************************EndGetMyCars************************");
				return myCars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all My Cars " + myCars);
		}

	}

	// Return Car
	@Override
	public Car returnCar(int id) throws Exception {
		System.out.println("************************StartReturnCar************************");
		List<Car> cars = carRepository.findAll();
		Client client = clientRepository.findById(clientId).get();
		Car car = null;
		try {
			if (carRepository.findClientCar(client.getId()).isEmpty()) {
				throw new Exception("Failed to get all " + client.getName() + " cars! Data is empty.");
			} else {
				car = carRepository.getOne(id);
				car.setAmount(car.getAmount() + 1);
				carRepository.save(car);
				carRepository.saveAll(cars);
				client.getCars().remove(car);
				clientRepository.save(client);
				System.out.println("Success on return Car. Client name: " + client.getName() + ", Car: " + car);
				System.out.println("************************EndReturnCar************************");
				return car;
			}
		} catch (Exception e) {
			throw new Exception("Failed to return Car " + car);
		}

	}

	// Get Receipts By Client
	public List<ClientReceipt> getReceiptsByClient() throws Exception {
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

	// Get Balance
	public double getBalance() throws Exception {
		System.out.println("************************StartGetBalance************************");
		Client temp = null;
		try {
			Optional<Client> optional = clientRepository.findById(clientId);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get client - this client id doesn't exist: " + clientId);
			} else {
				temp = optional.get();
				System.out.println("Success on get Client: " + temp);
				System.out.println("Client balance: " + temp.getBalance());
				System.out.println("************************EndGetBalance************************");
			}
		} catch (ClientDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to get client - this client id doesn't exist: " + clientId);
		}
		return temp.getBalance();
	}

	// Delete Account
	public Client deleteAccount() throws Exception {
		System.out.println("************************StartDeleteAccount************************");
		List<Car> cars = carRepository.findAll();
		Client temp = null;
		try {
			Optional<Client> optional = clientRepository.findById(clientId);
			if (!optional.isPresent()) {
				throw new Exception("Failed to remove Account - this Account doesn't exist ");
			} else {
				temp = optional.get();
				for (Car car : temp.getCars()) {
					car.setAmount(car.getAmount() + 1);
					carRepository.save(car);
				}
				carRepository.saveAll(cars);
				temp.getCars().removeAll(cars);
				clientRepository.deleteById(clientId);
				System.out.println(
						"Account removed successfully. Client id: " + clientId + " Client name: " + temp.getName());
				System.out.println("************************EndDeleteAccount************************");
			}
		} catch (ClientDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to remove Account. Client id: " + clientId);
		}
		return temp;
	}

}
