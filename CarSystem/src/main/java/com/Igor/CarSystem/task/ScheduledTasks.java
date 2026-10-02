package com.Igor.CarSystem.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.repo.CarRepository;
import com.Igor.CarSystem.repo.ClientRepository;


@Component
public class ScheduledTasks {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CarRepository carRepository;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	
	@Scheduled(fixedRate = 1000 * 60 * 2) //1000 * 60 * 60 * 24
	public void reportCurrentTime() {
		log.debug("Billing job started at {}", dateFormat.format(new Date()));
		List<Client> clients = clientRepository.findAll();
		for (Client client : clients) {
			if (client != null && client.getBalance() <= 0) {
				List<Car> cars = carRepository.findClientCar(client.getId());
				log.info("About to return cars : Client name: " +client.getName()+ ", balance: " +client.getBalance()+ ", cars: " + cars);
				for (Car car : cars) {
					if (car.getAmount() == 0) {
						car.setAmount(car.getAmount() + 1);
					}
					carRepository.save(car);
					log.debug("The saved car: " +car);
					carRepository.saveAll(cars);
					log.debug("Checking Car to return: "+client.getCars().remove(car)+ " "+car);
					client.getCars().remove(car);
					clientRepository.save(client);
					log.debug("Checking Car to return: "+client.getCars().remove(car)+ " "+car);
					log.info("Car number: " +car.getNumber()+ " was returned by client " +client.getName()+ ". Your balance is: " +client.getBalance());
				}
			} else if (client != null && client.getBalance() > 0) {
				List<Car> cars = carRepository.findClientCar(client.getId());
				for (Car car : cars) {
					client.setBalance(client.getBalance() - car.getPrice());
					clientRepository.save(client);
					log.info(client.getName()+ ", Thanks for your payment! Have a nice day! Your balance is: " +client.getBalance());
				}
				
			}
		}

	}

}