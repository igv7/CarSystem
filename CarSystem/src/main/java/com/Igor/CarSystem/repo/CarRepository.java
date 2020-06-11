package com.Igor.CarSystem.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
	
	public List<Car> deleteCarsById(int id);

	public boolean existsByNumber(String number);
	
	public Optional<Car> findByNumber(String number);
	
	public List<Car> findAllByType(CarType type);
	
	public List<Car> findAllByColor(CarColor color);
	
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.number=:number")
	public Car findClientCarByNumber(int id, String number);
	
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.type=:type")
	public List<Car> findClientCarByType(int id, CarType type);
	
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.color=:color")
	public List<Car> findClientCarByColor(int id, CarColor color);
		
	@Query("SELECT c from Client as client join client.cars As c WHERE client.id=:id AND c.price<=:price")
	public List<Car> findClientCarByPrice(int id, double price);

}
