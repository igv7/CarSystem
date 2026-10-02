package com.Igor.CarSystem.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CarRepository carRepository;

	// Client Operations
	// Create Client
	@Override
	public Client createClient(Client client) throws Exception {
		log.debug("************************StartCreateClient************************");
		try {
			if (clientRepository.existsByName(client.getName())) {
				throw new Exception("This client name already exist in system, please try another name.");
			} else {
				if (client.getName() != null && client.getBirthday() != null && client.getPassword() != null
						&& client.getPhoneNumber() != null && client.getEmail() != null) {
					clientRepository.save(client);
					log.info("Success on create client: " + client.getName() + " -> " + client);
				} else {
					log.error("Client not created - missing required fields: {}", client);
				}
				log.debug("************************EndCreateClient************************");
			}
		} catch (Exception e) {
			log.error("Cannot create client " + e.getMessage());
			throw new Exception("Cannot create client " + e.getMessage());
		}
		return client;
	}

	// Update Client
	@Override
	public Client updateClient(Client client) throws Exception {
		log.debug("************************StartUpdateClient************************");
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
				log.info("Success to update Client: " + temp);
				log.debug("************************EndUpdateClient************************");
			}
		} catch (Exception e) {
			log.error("Cannot update Client " + e.getMessage());
			throw new Exception("Cannot update Client " + e.getMessage());
		}
		return temp;

	}

	// Get Client By Id
	@Override
	public Client getClientById(int id) throws Exception {
		log.debug("************************StartGetClientById************************");
		Client temp = null;
		try {
			Optional<Client> optional = clientRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get client - this client id doesn't exist: " + id);
			} else {
				temp = optional.get();
				log.debug("Success on get Client: " + temp);
				log.debug("************************EndGetClientById************************");
			}
		} catch (ClientDoesntExist e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Failed to get client - this client id doesn't exist: " + id + ": {}", e.getMessage());
			throw new Exception("Failed to get client - this client id doesn't exist: " + id);
		}
		return temp;
	}

	// Get All Clients
	@Override
	public List<Client> getAllClients() throws Exception {
		log.debug("************************StartGetAllClientsById************************");
		List<Client> clients = null;
		try {
			if (clientRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all clients. The list is empty!");
			} else {
				clients = clientRepository.findAll();
				log.debug("Success on get All Clients: " + clients);
				log.debug("************************EndGetAllClientsById************************");
				return clients;
			}
		} catch (Exception e) {
			log.error("Failed to get all clients" + ": {}", e.getMessage());
			throw new Exception("Failed to get all clients");
		}

	}

	// Delete Client
	@Override
	public Client deleteClient(int id) throws Exception {
		log.debug("************************StartDeleteClient************************");
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
				log.info("Client removed successfully. Client id: " + id + " Client name: " + temp.getName());
				log.debug("************************EndDeleteClient************************");
			}
		} catch (ClientDoesntExist e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Failed to remove Client. Client id: " + id + ": {}", e.getMessage());
			throw new Exception("Failed to remove Client. Client id: " + id);
		}
		return temp;

	}

	// **************************************************************************************************************

	// Car Operations
	// Create Car
	@Override
	public Car createCar(Car car) throws Exception {
		log.debug("************************StartCreateCar************************");
		try {
			if (carRepository.existsByNumber(car.getNumber())) {
				throw new Exception("This car number already exist in system, please try another number.");
			} else {
				if (car.getNumber() != null && car.getColor() != null && car.getType() != null && car.getAmount() != 0
						&& car.getPrice() != 0 && car.getImage() != null) {
					carRepository.save(car);
					log.info("Success on create car. Car number: " + car.getNumber() + " -> " + car);
				} else {
					log.error("Car not created - missing required fields: {}", car);
				}
				log.debug("************************EndCreateCar************************");
			}
		} catch (Exception e) {
			log.error("Cannot create car " + e.getMessage());
			throw new Exception("Cannot create car " + e.getMessage());
		}
		return car;
	}

	// Update Car
	@Override
	public Car updateCar(Car car) throws Exception {
		log.debug("************************StartUpdateCar************************");
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
				log.info("Success to update Car: " + temp);
				log.debug("************************EndUpdateCar************************");
			}
		} catch (Exception e) {
			log.error("Cannot update Car " + e.getMessage());
			throw new Exception("Cannot update Car " + e.getMessage());
		}
		return temp;

	}

	// Get Car By Id
	@Override
	public Car getCarById(int id) throws Exception {
		log.debug("************************StartGetCarById************************");
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get car - this car id doesn't exist: " + id);
			} else {
				temp = optional.get();
				log.debug("Success on get Car by id " + id + ": " + temp);
				log.debug("************************EndGetCarById************************");
			}
		} catch (CarDoesntExist e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Failed to get car - this car id doesn't exist: " + id + ": {}", e.getMessage());
			throw new Exception("Failed to get car - this car id doesn't exist: " + id);
		}
		return temp;
	}

	// Get Car By CarNumber
	@Override
	public Car getCarByNumber(String number) throws Exception {
		log.debug("************************StartGetCarByNumber************************");
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findByNumber(number);
			if (!optional.isPresent()) {
				throw new Exception("Failed to get car - this car number doesn't exist: " + number);
			} else {
				temp = optional.get();
				log.debug("Success on get Car by number " + number + ": " + temp);
				log.debug("************************EndGetCarByNumber************************");
			}
		} catch (CarDoesntExist e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Failed to get car - this car number doesn't exist: " + number + ": {}", e.getMessage());
			throw new Exception("Failed to get car - this car number doesn't exist: " + number);
		}
		return temp;
	}

	// Get All Cars
	@Override
	public List<Car> getAllCars() throws Exception {
		log.debug("************************StartGetAllCars************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAll();
				log.debug("Success on get all Cars: " + cars);
				log.debug("************************EndGetAllCars************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Failed to get all cars" + ": {}", e.getMessage());
			throw new Exception("Failed to get all cars");
		}

	}

	// Delete Car
	@Override
	public Car deleteCar(int id) throws Exception {
		log.debug("************************StartDeleteCar************************");
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
				log.info("Car removed successfully. Car id: " + id + " Car number: " + temp.getNumber());
				log.debug("************************EndDeleteCar************************");
			}
		} catch (CarDoesntExist e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Failed to remove Car. Car id: " + id + ": {}", e.getMessage());
			throw new Exception("Failed to remove Car. Car id: " + id);
		}
		return temp;

	}

	// Return Car
	public Car returnCar(int id) throws Exception {
		log.debug("************************StartReturnCar************************");
		Client client = clientRepository.findClientByCar(id);
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findById(id);
			if (!optional.isPresent()) {
				throw new Exception("Failed to return Car - this Car id doesn't exist: " + id);
			} else {
				temp = optional.get();
				if (client != null) {
					temp.setAmount(temp.getAmount() + 1);
					carRepository.save(temp);
					client.getCars().remove(temp);
					clientRepository.save(client);
				}
				log.info("Car returned successfully. Car id: " + id + " Car number: " + temp.getNumber());
				log.debug("************************EndReturnCar************************");
			}
		} catch (CarDoesntExist e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Failed to return Car. Car id: " + id + ": {}", e.getMessage());
			throw new Exception("Failed to return Car. Car id: " + id);
		}
		return temp;

	}

	// Get all Cars By CarType
	public List<Car> getAllCarsByType(CarType type) throws Exception {
		log.debug("************************StartGetAllCarsByType************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAllByType(type);
				log.debug("Success on get all Cars by type " + type + ": " + cars);
				log.debug("************************EndGetAllCarsByType************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Failed to get all cars by type " + type + ": {}", e.getMessage());
			throw new Exception("Failed to get all cars by type " + type);
		}

	}

	// Get all Cars By CarColor
	public List<Car> getAllCarsByColor(CarColor color) throws Exception {
		log.debug("************************StartGetAllCarsByColor************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAllByColor(color);
				log.debug("Success on get all Cars by color " + color + ": " + cars);
				log.debug("************************EndGetAllCarsByColor************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Failed to get all cars by color " + color + ": {}", e.getMessage());
			throw new Exception("Failed to get all cars by color " + color);
		}

	}

	// Get Client Car By CarNumber
	public Car getClientCarByNumber(int clientId, String number) throws Exception {
		log.debug("************************StartGetClientCarByNumber************************");
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
				log.debug("Success on get Client Car by number. Client name: " + client.getName()
						+ ", car number: " + number + ": " + temp);
				log.debug("************************EndGetClientCarByNumber************************");
			}
		} catch (CarDoesntExist e) {
			log.error(e.getMessage());
			;
		} catch (Exception e) {
			log.error("Failed to get car: " + number + ": {}", e.getMessage());
			throw new Exception("Failed to get car: " + number);
		}
		return temp;
	}

	// Get All Client Cars
	public List<Car> getAllClientCars(int clientId) throws Exception {
		log.debug("************************StartGetAllClientCars************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + "'s cars. Cars do not exist.");
			} else {
				cars = carRepository.findClientCar(client.getId());
				log.debug(
						"Success on get all Client Cars. Client name: " + client.getName() + ", cars: " + cars);
				log.debug("************************EndGetAllClientCars************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Admin failed to get all client cars: " + cars + ": {}", e.getMessage());
			throw new Exception("Admin failed to get all client cars: " + cars);
		}

	}

	// Get All Client Cars By CarType
	public List<Car> getAllClientCarsByType(int clientId, CarType type) throws Exception {
		log.debug("************************StartGetAllClientCarsByType************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + "'s cars by type " + type
						+ ". Cars do not exist");
			} else {
				cars = carRepository.findClientCarByType(client.getId(), type);
				log.debug("Success on get all Client Cars by type. Client name: " + client.getName()
						+ ", car type: " + type + ": " + cars);
				log.debug("************************EndGetAllClientCarsByType************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Admin failed to get all client cars by type " + type + ": {}", e.getMessage());
			throw new Exception("Admin failed to get all client cars by type " + type);
		}

	}

	// Get All Client Cars By CarColor
	public List<Car> getAllClientCarsByColor(int clientId, CarColor color) throws Exception {
		log.debug("************************StartGetAllClientCarsByColor************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + " cars by color " + color
						+ ". Cars do not exist");
			} else {
				cars = carRepository.findClientCarByColor(client.getId(), color);
				log.debug("Success on get all Client Cars by color. Client name: " + client.getName()
						+ ", car color: " + color + ": " + cars);
				log.debug("************************EndGetAllClientCarsByColor************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Admin failed to get all client cars by color " + color + ": {}", e.getMessage());
			throw new Exception("Admin failed to get all client cars by color " + color);
		}

	}

	// Get All Client Cars By Price (until)
	public List<Car> getAllClientCarsByPrice(int clientId, double price) throws Exception {
		log.debug("************************StartGetAllClientCarsByPrice************************");
		Client client = clientRepository.findById(clientId).get();
		List<Car> cars = null;
		try {
			if (client.getCars().isEmpty()) {
				throw new Exception("Admin failed to get all " + client.getName() + " cars by price " + price
						+ ". Cars do not exist");
			} else {
				cars = carRepository.findClientCarByPrice(client.getId(), price);
				log.debug("Success on get all Client Cars by price. Client name: " + client.getName()
						+ ", car price until: " + price + ": " + cars);
				log.debug("************************EndGetAllClientCarsByPrice************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Admin failed to get all client cars by price until " + price + ": {}", e.getMessage());
			throw new Exception("Admin failed to get all client cars by price until " + price);
		}

	}

}
