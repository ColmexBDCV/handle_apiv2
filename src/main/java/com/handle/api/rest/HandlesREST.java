package com.handle.api.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.handle.api.model.Handle;
import com.handle.api.model.HandleResponse;
import com.handle.api.repositories.entities.Secuence_names;
import com.handle.api.services.HandleService;
import com.handle.api.services.HandleServiceImpl;
import com.handle.api.services.SecuenceService;

import net.handle.hdllib.HandleException;

@RestController
@RequestMapping("/handle")
public class HandlesREST {
	
	@Autowired
	private HandleService handleService;
	
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ResponseEntity<HandleResponse> createHandle(@RequestBody Handle handleRequest){
		HandleResponse handleRes = null;
		try {
			
			handleRes = handleService.beforeCreate(handleRequest);
			
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			handleRes = new HandleResponse(0, null, null, "ERROR", "Ocurrio un error durante la creación del handle.");
			e.printStackTrace();
		}
		return ResponseEntity.ok(handleRes);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<HandleResponse> modifyHandle(@RequestBody Handle handleRequest){
		HandleResponse handleRes = null;
		try {
			handleRes = handleService.updateHandle(handleRequest);
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			handleRes = new HandleResponse(0, null, null, "ERROR", "Ocurrio un error durante la actualización del handle.");
			e.printStackTrace();
		}
		return ResponseEntity.ok(handleRes);
	}
	
	@RequestMapping(value = "/delete/{handle}/{collection}/{handleId}", method = RequestMethod.DELETE)
	public ResponseEntity<HandleResponse> deleteHandle(@PathVariable("handle") String handle, @PathVariable("collection") String collection ,@PathVariable("handleId") String handleId){
		HandleResponse handleRes = new HandleResponse();
		try {
			handleRes = handleService.deleteHandle(handle, collection, handleId);
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			handleRes = new HandleResponse(0, null, null, "ERROR", "Ocurrio un error durante la eliminación del handle.");
			e.printStackTrace();
		}
		return ResponseEntity.ok(handleRes);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listHandle(){
		List<String> lista = null;
		try {
			
			lista = handleService.listHandles();
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(lista);
	}
	
	//@GetMapping
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<String> hello() {		
		/*for(Secuence_names secuence : secuenceService.findAll()) {
			System.out.println("Id: " + secuence.getId_secuence());
			System.out.println("Name: " + secuence.getSecuence());
		}
		
		Secuence_names secuence = secuenceService.getMax();
		System.out.println("Max Value: " + secuence.getSecuence());
		int number = secuence.getSecuence();
		Secuence_names newSecuence = new Secuence_names();
		newSecuence.setSecuence(number + 1);
		secuence = secuenceService.save(newSecuence);*/
		return ResponseEntity.ok("Hello World");
	}

}
