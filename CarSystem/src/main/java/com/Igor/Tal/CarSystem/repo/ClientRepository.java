package com.Igor.Tal.CarSystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Igor.Tal.CarSystem.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

	public boolean existsByName(String name);
	
	public Client findByName(String name);
	
	public Client findByNameAndPassword(String name, String password);
	
	@Query("SELECT client from Client as client join client.cars As c WHERE c.id=:id")
	public Client findClientByCar(int id);
	
	@Query("SELECT client FROM Client as client join client.cars As c WHERE c.id=:id")
	List<Client> findClientsByCar(int id);

}
