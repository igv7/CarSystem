package com.Igor.CarSystem.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;

/** Car table access. The {@code findClientCar*} queries look only at cars linked to one client. */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
	
	/** Deletes the car with this ID and returns the deleted rows. */
	public List<Car> deleteCarsById(int id);

	/** @return true if a car with this number already exists */
	public boolean existsByNumber(String number);
	
	/** Finds a car by its unique number. */
	public Optional<Car> findByNumber(String number);
	
	/** All cars of the given make. */
	public List<Car> findAllByType(CarType type);
	
	/** All cars of the given color. */
	public List<Car> findAllByColor(CarColor color);
	
	/** The car with this number among the cars rented by client {@code id}, or null. */
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.number=:number")
	public Car findClientCarByNumber(int id, String number);
	
	/** All cars rented by client {@code id}. */
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id")
	public List<Car> findClientCar(int id);
	
	/** Cars of the given make rented by client {@code id}. */
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.type=:type")
	public List<Car> findClientCarByType(int id, CarType type);
	
	/** Cars of the given color rented by client {@code id}. */
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.color=:color")
	public List<Car> findClientCarByColor(int id, CarColor color);
		
	/** Cars rented by client {@code id} costing at most {@code price}. */
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.price<=:price")
	public List<Car> findClientCarByPrice(int id, double price);

}
