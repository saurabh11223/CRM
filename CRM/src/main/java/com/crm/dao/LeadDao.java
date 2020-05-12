package com.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.LeadDetail;

public interface LeadDao extends JpaRepository<LeadDetail,Integer>
{
	
}
