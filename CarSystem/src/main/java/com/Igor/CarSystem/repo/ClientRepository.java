package com.Igor.CarSystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Igor.CarSystem.model.Client;

/** Client table access. */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

	/** @return true if a client with this name exists (names act as unique usernames) */
	public boolean existsByName(String name);
	
	/** Finds a client by name, or null. */
	public Client findByName(String name);
	
	/** Login lookup: the client with this name and password, or null. */
	public Client findByNameAndPassword(String name, String password);
	
	/** The client renting the car with this ID, or null. Fails if several clients rent it. */
	@Query("SELECT client from Client as client join client.cars As c WHERE c.id=:id")
	public Client findClientByCar(int id);
	
	/** All clients renting the car with this ID. */
	@Query("SELECT client FROM Client as client join client.cars As c WHERE c.id=:id")
	List<Client> findClientsByCar(int id);

}
