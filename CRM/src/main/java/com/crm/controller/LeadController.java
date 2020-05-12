package com.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dao.LeadDao;
import com.crm.entity.Enum.Status;
import com.crm.entity.LeadDetail;
import com.crm.entity.MarkLead;
import com.crm.entity.Response;
import com.crm.exception.CustomException;

@RequestMapping("/api")
@RestController
public class LeadController {

	@Autowired
	LeadDao dao;

	// Getting Lead detail
	@GetMapping("/leads/{lead_id}")
	public ResponseEntity<?> getData(@PathVariable Integer lead_id) {
		LeadDetail detail = null;
		try {
			detail = dao.findById(lead_id).orElse(null);
			if (detail == null)
				return new ResponseEntity<Response>(new Response(null, null, null), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response("Failure", e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}
		detail.setId(null);
		return new ResponseEntity<LeadDetail>(detail, HttpStatus.OK);
	}

	// Creating new Lead
	@PostMapping("/leads")
	public ResponseEntity<?> createLead(@RequestBody LeadDetail detail) {
		LeadDetail detailData = null;
		try {
			if (detail.getMobile().toString().length() > 10)
				throw new CustomException("Mobile number should be 10 digit");
			detail.setStatus(Status.valueOf("Created"));
			detailData = dao.save(detail);
		} catch (Exception e) {
			String msg = e.getMessage();
			if (e instanceof DataIntegrityViolationException)
				msg = "Please check mobile or email request Data";
			return new ResponseEntity<Response>(new Response("failure", msg, null), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<LeadDetail>(detailData, HttpStatus.CREATED);
	}

	// Updating Lead Detail
	@PutMapping("/leads/{lead_id}")
	public ResponseEntity<Response> updateLead(@RequestBody LeadDetail detail, @PathVariable Integer lead_id) {
		LeadDetail leaddetail = null;
		try {
			if (detail.getMobile().toString().length() > 10)
				throw new CustomException("Mobile number should be 10 digit");
			leaddetail = dao.findById(lead_id).orElse(null);
			if (leaddetail == null)
				return new ResponseEntity<Response>(new Response("failure", "Entity Not Exist for Given Id", null),
						HttpStatus.BAD_REQUEST);
			leaddetail.setFirst_name(detail.getFirst_name());
			leaddetail.setLast_name(detail.getLast_name());
			leaddetail.setEmail(detail.getEmail());
			leaddetail.setMobile(detail.getMobile());
			leaddetail.setLocation_type(detail.getLocation_type());
			leaddetail.setLocation_String(detail.getLocation_String());
			dao.save(leaddetail);
		} catch (Exception e) {
			String msg = e.getMessage();
			if (e instanceof DataIntegrityViolationException)
				msg = "Please check mobile or email request Data";
			return new ResponseEntity<Response>(new Response("failure",msg, null), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Response>(new Response("success", null, null), HttpStatus.ACCEPTED);
	}

	// Deleting lead
	@DeleteMapping("/leads/{lead_id}")
	public ResponseEntity<Response> deleteLead(@PathVariable Integer lead_id) {
		try {
			dao.deleteById(lead_id);
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response("failure", e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Response>(new Response("success", null, null), HttpStatus.ACCEPTED);
	}

	// Marking Lead
	@PutMapping("/mark_lead/{lead_id}")
	public ResponseEntity<Response> updateLead(@RequestBody MarkLead lead, @PathVariable Integer lead_id) {
		try {
			LeadDetail leaddetail = dao.getOne(lead_id);

			leaddetail.setStatus(Status.valueOf("Contacted"));
			dao.save(leaddetail);
		} catch (Exception e) {
			return new ResponseEntity<Response>(new Response("failure", e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Response>(new Response("Contacted", null, lead.getCommunication()),
				HttpStatus.ACCEPTED);
	}

	// handle the exception coming during parsing request body
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> handleException() {
		return new ResponseEntity<Response>(
				new Response("failure", "Please check Request data,Error in parsing the Request", null),
				HttpStatus.BAD_REQUEST);
	}

}
