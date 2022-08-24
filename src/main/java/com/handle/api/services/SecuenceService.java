package com.handle.api.services;

import com.handle.api.entities.Secuence_names;

public interface SecuenceService {
	
	public Iterable<Secuence_names> findAll();
	
	public Secuence_names getMax();
	
	public Secuence_names save(Secuence_names secuence);
	
	public Secuence_names getInactive();
	
	public Secuence_names findBySecuence(int secuence);
}
