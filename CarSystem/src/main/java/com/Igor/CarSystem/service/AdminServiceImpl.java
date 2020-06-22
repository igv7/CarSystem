package com.Igor.CarSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.exceptions.CarDoesntExist;
import com.Igor.CarSystem.exceptions.ClientDoesntExist;
import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.repo.CarRepository;
import com.Igor.CarSystem.repo.ClientRepository;

@Service
public class AdminServiceImpl implements AdminService, Facade {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CarRepository carRepository;

	// Client Operations
	// Create Client
	@Override
	public Client createClient(Client client) throws Exception {
		System.out.println("************************StartCreateClient************************");
		try {
			if (clientRepository.existsByName(client.getName())) {
				throw new Exception("This client name already exist in system, please try another name.");
			} else {
				clientRepository.save(client);
				System.out.println("Success on create client: " + client.getName() + " -> " + client);
				System.out.println("************************EndCreateClient************************");
			}
		} catch (Exception e) {
			throw new Exception("Cannot create client " + e.getMessage());
		}
		return client;
	}

	// Update Client
	@Override
	public Client updateClient(Client client) throws Exception {
		System.out.println("************************StartUpdateClient************************");
		Client temp = null;
		try {
			Optional<Client> optional = clientRepository.findById(client.getId());
			if (!optional.isPresent()) {
				throw new Exception("Client doesn't exist");
			} else {
				temp = optional.get();
				temp.setName(client.getName());
				temp.setPassword(client.getPassword());
				temp.setBirthday(client.getBirthday());
				temp.setPhoneNumber(client.getPhoneNumber());
				temp.setEmail(client.getEmail());
				temp.setBalance(client.getBalance());
				clientRepository.save(temp);
				System.out.println("Success to update Client: " + temp);
				System.out.println("************************EndUpdateClient************************");
			}
		} catch (Exception e) {
			throw new Exception("Cannot update Client " + e.getMessage());
		}
		return temp;

	}

	// Get Client By Id
	@Override
	public Client getClientById(int id) throws Exception {
		System.out.println("************************StartGetClientById************************");
		Client temp = null;
		try {
			Optional<Client> optional = clientRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get client - this client id doesn't exist: " + id);
			} else {
				temp = optional.get();
				System.out.println("Success on get Client: " + temp);
				System.out.println("************************EndGetClientById************************");
			}
		} catch (ClientDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to get client - this client id doesn't exist: " + id);
		}
		return temp;
	}

	// Get All Clients
	@Override
	public List<Client> getAllClients() throws Exception {
		System.out.println("************************StartGetAllClientsById************************");
		List<Client> clients = null;
		try {
			if (clientRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all clients. The list is empty!");
			} else {
				clients = clientRepository.findAll();
				System.out.println("Success on get All Clients: " + clients);
				System.out.println("************************EndGetAllClientsById************************");
				return clients;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all clients");
		}

	}

	// Delete Client
	@Override
	public Client deleteClient(int id) throws Exception {
		System.out.println("************************StartDeleteClient************************");
		List<Car> cars = carRepository.findAll();
		Client temp = null;
		try {
			Optional<Client> optional = clientRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to remove Client - this Client id doesn't exist: " + id);
			} else {
				temp = optional.get();
				for (Car car : temp.getCars()) {
					car.setAmount(car.getAmount() + 1);
					carRepository.save(car);
				}
				carRepository.saveAll(cars);
				temp.getCars().removeAll(cars);
				clientRepository.deleteById(id);
				System.out.println("Client removed successfully. Client id: " + id + " Client name: " + temp.getName());
				System.out.println("************************EndDeleteClient************************");
			}
		} catch (ClientDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to remove Client. Client id: " + id);
		}
		return temp;

	}

	// **************************************************************************************************************

	// Car Operations
	// Create Car
	@Override
	public Car createCar(Car car) throws Exception {
		System.out.println("************************StartCreateCar************************");
		try {
			if (carRepository.existsByNumber(car.getNumber())) {
				throw new Exception("This car number already exist in system, please try another number.");
			} else {
				carRepository.save(car);
				System.out.println("Success on create car. Car number: " + car.getNumber() + " -> " + car);
				System.out.println("************************EndCreateCar************************");
			}
		} catch (Exception e) {
			throw new Exception("Cannot create car " + e.getMessage());
		}
		return car;
	}

	// Update Car
	@Override
	public Car updateCar(Car car) throws Exception {
		System.out.println("************************StartUpdateCar************************");
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findById(car.getId());
			if (!optional.isPresent()) {
				throw new Exception("Car doesn't exist");
			} else {
				temp = optional.get();
				temp.setNumber(car.getNumber());
				temp.setColor(car.getColor());
				temp.setType(car.getType());
				temp.setAmount(car.getAmount());
				temp.setPrice(car.getPrice());
				temp.setImage(car.getImage());
				carRepository.save(temp);
				System.out.println("Success to update Car: " + temp);
				System.out.println("************************EndUpdateCar************************");
			}
		} catch (Exception e) {
			throw new Exception("Cannot update Car " + e.getMessage());
		}
		return temp;

	}

	// Get Car By Id
	@Override
	public Car getCarById(int id) throws Exception {
		System.out.println("************************StartGetCarById************************");
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get car - this car id doesn't exist: " + id);
			} else {
				temp = optional.get();
				System.out.println("Success on get Car by id " + id + ": " + temp);
				System.out.println("************************EndGetCarById************************");
			}
		} catch (CarDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to get car - this car id doesn't exist: " + id);
		}
		return temp;
	}

	// Get Car By CarNumber
	@Override
	public Car getCarByNumber(String number) throws Exception {
		System.out.println("************************StartGetCarByNumber************************");
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findByNumber(number);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get car - this car number doesn't exist: " + number);
			} else {
				temp = optional.get();
				System.out.println("Success on get Car by number " + number + ": " + temp);
				System.out.println("************************EndGetCarByNumber************************");
			}
		} catch (CarDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to get car - this car number doesn't exist: " + number);
		}
		return temp;
	}

	// Get All Cars
	@Override
	public List<Car> getAllCars() throws Exception {
		System.out.println("************************StartGetAllCars************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAll();
				System.out.println("Success on get all Cars: " + cars);
				System.out.println("************************EndGetAllCars************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all cars");
		}

	}

	// Delete Car
	@Override
	public Car deleteCar(int id) throws Exception {
		System.out.println("************************StartDeleteCar************************");
		Client client = clientRepository.findClientByCar(id);
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to remove Car - this Car id doesn't exist: " + id);
			} else {
				temp = optional.get();
				if (client != null) {
					client.getCars().remove(temp);
					clientRepository.save(client);
				}
				carRepository.deleteById(id);
				System.out.println("Car removed successfully. Car id: " + id + " Car number: " + temp.getNumber());
				System.out.println("************************EndDeleteCar************************");
			}
		} catch (CarDoesntExist e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Failed to remove Car. Car id: " + id);
		}
		return temp;

	}

	// Get all Cars By CarType
	public List<Car> getAllCarsByType(CarType type) throws Exception {
		System.out.println("************************StartGetAllCarsByType************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAllByType(type);
				System.out.println("Success on get all Cars by type " + type + ": " + cars);
				System.out.println("************************EndGetAllCarsByType************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all cars by type " + type);
		}

	}

	// Get all Cars By CarColor
	public List<Car> getAllCarsByColor(CarColor color) throws Exception {
		System.out.println("************************StartGetAllCarsByColor************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAllByColor(color);
				System.out.println("Success on get all Cars by color " + color + ": " + cars);
				System.out.println("************************EndGetAllCarsByColor************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all cars by color " + color);
		}

	}

	// Get Client Car By CarNumber
	public Car getClientCarByNumber(int clientId, String number) throws Exception {
		System.out.println("************************StartGetClientCarByNumber************************");
		Client client = clientRepository.findById(clientId).get();
		Car temp = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get " + client.getName() + "'s cars. Cars do not exist");
			}
			Optional<Car> optional = carRepository.findByNumber(number);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get car - this car number doesn't exist: " + number);
			} else {
				temp = optional.get();
				System.out.println("Success on get Client Car by number. Client name: " + client.getName()
						+ ", car number: " + number + ": " + temp);
				System.out.println("************************EndGetClientCarByNumber************************");
			}
		} catch (CarDoesntExist e) {
			System.err.println(e.getMessage());
			;
		} catch (Exception e) {
			throw new Exception("Failed to get car: " + number);
		}
		return temp;
	}

	// Get All Client Cars
	public List<Car> getAllClientCars(int clientId) throws Exception {
		System.out.println("************************StartGetAllClientCars************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + "'s cars. Cars do not exist.");
			} else {
				cars = carRepository.findClientCar(client.getId());
				System.out.println(
						"Success on get all Client Cars. Client name: " + client.getName() + ", cars: " + cars);
				System.out.println("************************EndGetAllClientCars************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Admin failed to get all client cars: " + cars);
		}

	}

	// Get All Client Cars By CarType
	public List<Car> getAllClientCarsByType(int clientId, CarType type) throws Exception {
		System.out.println("************************StartGetAllClientCarsByType************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + "'s cars by type " + type
						+ ". Cars do not exist");
			} else {
				cars = carRepository.findClientCarByType(client.getId(), type);
				System.out.println("Success on get all Client Cars by type. Client name: " + client.getName()
						+ ", car type: " + type + ": " + cars);
				System.out.println("************************EndGetAllClientCarsByType************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Admin failed to get all client cars by type " + type);
		}

	}

	// Get All Client Cars By CarColor
	public List<Car> getAllClientCarsByColor(int clientId, CarColor color) throws Exception {
		System.out.println("************************StartGetAllClientCarsByColor************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + " cars by color " + color
						+ ". Cars do not exist");
			} else {
				cars = carRepository.findClientCarByColor(client.getId(), color);
				System.out.println("Success on get all Client Cars by color. Client name: " + client.getName()
						+ ", car color: " + color + ": " + cars);
				System.out.println("************************EndGetAllClientCarsByColor************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Admin failed to get all client cars by color " + color);
		}

	}

	// Get All Client Cars By Price (until)
	public List<Car> getAllClientCarsByPrice(int clientId, double price) throws Exception {
		System.out.println("************************StartGetAllClientCarsByPrice************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + " cars by price " + price
						+ ". Cars do not exist");
			} else {
				cars = carRepository.findClientCarByPrice(client.getId(), price);
				System.out.println("Success on get all Client Cars by price. Client name: " + client.getName()
						+ ", car price until: " + price + ": " + cars);
				System.out.println("************************EndGetAllClientCarsByPrice************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Admin failed to get all client cars by price until " + price);
		}

	}

}
