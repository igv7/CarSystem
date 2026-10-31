package com.Igor.CarSystem.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.Igor.CarSystem.model.Client;

/** Client table access. */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

	/** @return true if a client with this name exists (names act as unique usernames) */
	public boolean existsByName(String name);
	
	/** @return true if a client with this clientID (personal identity number) exists */
	public boolean existsByClientID(String clientID);

	/** Finds a client by clientID (personal identity number). */
	public Optional<Client> findByClientID(String clientID);

	/** Finds a client by name, or null. */
	public Client findByName(String name);
	
	/**
	 * Replaces a client's stored password with {@code password} (already hashed) in a single UPDATE, without
	 * loading the client, so the rules on its other fields are not re-checked.
	 */
	@Transactional
	@Modifying
	@Query("UPDATE Client c SET c.password = :password WHERE c.id = :id")
	public int updatePassword(@Param("id") int id, @Param("password") String password);
	
	/** The client renting the car with this ID, or null. Fails if several clients rent it. */
	@Query("SELECT client from Client as client join client.cars As c WHERE c.id=:id")
	public Client findClientByCar(int id);
	
	/** All clients renting the car with this ID. */
	@Query("SELECT client FROM Client as client join client.cars As c WHERE c.id=:id")
	List<Client> findClientsByCar(int id);

}
