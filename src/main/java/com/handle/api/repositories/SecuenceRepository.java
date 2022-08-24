package com.handle.api.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.handle.api.entities.Secuence_names;

@Repository("SecuenceRepository")
public interface SecuenceRepository extends CrudRepository<Secuence_names, Integer>{
	
	@Query(value = "SELECT * FROM secuence_names ORDER BY secuence DESC LIMIT 1", nativeQuery = true)
	public Secuence_names getMax();
	
	@Query(value = "SELECT * FROM secuence_names WHERE status = 0 ORDER BY secuence ASC LIMIT 1", nativeQuery = true)
	public Secuence_names getInactive();
	
	@Query(value = "SELECT * FROM secuence_names WHERE secuence = :secuence", nativeQuery = true)
	public Secuence_names findBySecuence(@Param("secuence") int secuence);
	
}
