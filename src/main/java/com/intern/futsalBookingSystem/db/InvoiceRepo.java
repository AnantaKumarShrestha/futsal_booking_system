package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.InvoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepo extends JpaRepository<InvoiceModel,Integer> {

    List<InvoiceModel> findAllByFutsalId(UUID futsalId);

}
