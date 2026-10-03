package com.Igor.CarSystem.service;

import java.util.ArrayList;
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

/** Admin operations. A single shared instance; it is the {@link Facade} stored in every admin session. */
@Service
public class AdminServiceImpl implements AdminService, Facade {

	private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CarRepository carRepository;

	// Client Operations
	/**
	 * Saves a new client. Field rules are checked by {@code @Valid} in the controller; any id or cars sent
	 * in the body are ignored, so this always creates a new client with no rentals.
	 * @throws Exception if the name is already taken or a required field is missing
	 */
	@Override
	public Client createClient(Client client) throws Exception {
		log.debug("************************StartCreateClient************************");
		try {
			if (clientRepository.existsByName(client.getName())) {
				throw new Exception("This client name already exist in system, please try another name.");
			} else if (client.getName() == null || client.getBirthday() == null || client.getPassword() == null
					|| client.getPhoneNumber() == null || client.getEmail() == null) {
				throw new Exception("Missing required fields (name, birthday, password, phone, email).");
			} else {
				client.setId(0);
				client.setCars(new ArrayList<>());
				clientRepository.save(client);
				log.info("Success on create client: " + client.getName() + " -> " + client);
				log.debug("************************EndCreateClient************************");
			}
		} catch (Exception e) {
			log.error("Cannot create client " + e.getMessage());
			throw new Exception("Cannot create client " + e.getMessage());
		}
		return client;
	}

	/**
	 * Copies all details from {@code client} onto the stored client with the same ID.
	 * @throws Exception if no client has that ID
	 */
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

	/** @throws Exception if no client has that ID */
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

	/** @throws Exception if there are no clients */
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

	/**
	 * Marks every car the client rents available (amount = 1), then deletes the client. The cars stay in the catalogue.
	 * @return the deleted client
	 * @throws Exception if no client has that ID
	 */
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
					car.setAmount(1);
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
	/**
	 * Saves a new car as available (amount = 1, whatever was sent). Field rules are checked by
	 * {@code @Valid} in the controller; any id sent in the body is ignored, so this always creates a new car.
	 * @throws Exception if the number is already taken, or the number, color, type or image is missing or the price is 0
	 */
	@Override
	public Car createCar(Car car) throws Exception {
		log.debug("************************StartCreateCar************************");
		try {
			if (carRepository.existsByNumber(car.getNumber())) {
				throw new Exception("This car number already exist in system, please try another number.");
			} else if (car.getNumber() == null || car.getColor() == null || car.getType() == null
					|| car.getPrice() == 0 || car.getImage() == null) {
				throw new Exception("Missing required fields (number, color, type, price, image).");
			} else {
				car.setId(0);
				car.setAmount(1);
				carRepository.save(car);
				log.info("Success on create car. Car number: " + car.getNumber() + " -> " + car);
				log.debug("************************EndCreateCar************************");
			}
		} catch (Exception e) {
			log.error("Cannot create car " + e.getMessage());
			throw new Exception("Cannot create car " + e.getMessage());
		}
		return car;
	}

	/**
	 * Copies all details from {@code car} onto the stored car with the same ID.
	 * @throws Exception if no car has that ID, or the amount is not 0 (rented) or 1 (available)
	 */
	@Override
	public Car updateCar(Car car) throws Exception {
		log.debug("************************StartUpdateCar************************");
		Car temp = null;
		try {
			Optional<Car> optional = carRepository.findById(car.getId());
			if (!optional.isPresent()) {
				throw new Exception("Car doesn't exist");
			} else if (car.getAmount() != 0 && car.getAmount() != 1) {
				throw new Exception("Car amount must be 0 (rented) or 1 (available), got " + car.getAmount());
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

	/** @throws Exception if no car has that ID */
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

	/** @throws Exception if no car has that number */
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

	/** @throws Exception if there are no cars */
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

	/**
	 * Removes the car from the client renting it (if any) and deletes it.
	 * @return the deleted car
	 * @throws Exception if no car has that ID
	 */
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

	/**
	 * Takes the car back from the client renting it and marks it available (amount = 1). Does nothing
	 * if nobody rents it.
	 * @throws Exception if no car has that ID
	 */
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
					temp.setAmount(1);
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

	/** @throws Exception if there are no cars at all */
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

	/** @throws Exception if there are no cars at all */
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

	/**
	 * Finds a car by number, provided the client rents at least one car.
	 * Note: the car itself is not checked to be one of the client's cars.
	 * @throws Exception if the client rents no cars or the number doesn't exist
	 */
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

	/** @throws Exception if the client rents no cars */
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

	/** @throws Exception if the client rents no cars */
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

	/** @throws Exception if the client rents no cars */
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

	/**
	 * Cars rented by the client costing at most {@code price}.
	 * @throws Exception if the client rents no cars
	 */
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
