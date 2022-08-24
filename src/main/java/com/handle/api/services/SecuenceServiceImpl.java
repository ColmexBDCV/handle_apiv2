package com.handle.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handle.api.entities.Secuence_names;
import com.handle.api.repositories.SecuenceRepository;

@Service("secuenceService")
public class SecuenceServiceImpl implements SecuenceService{
	
	@Autowired
	private SecuenceRepository secuenceRepository;
	
	@Override
	public Iterable<Secuence_names> findAll() {
		return secuenceRepository.findAll();
	}

	@Override
	public Secuence_names getMax() {
		return secuenceRepository.getMax();
	}

	@Override
	public Secuence_names save(Secuence_names secuence) {
		return secuenceRepository.save(secuence);
	}

	@Override
	public Secuence_names getInactive() {
		return secuenceRepository.getInactive();
	}

	@Override
	public Secuence_names findBySecuence(int secuence) {
		return secuenceRepository.findBySecuence(secuence);
	}
	
}
