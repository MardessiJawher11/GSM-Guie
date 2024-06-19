package gsm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import gsm.dto.PieceItemDto;
import gsm.entities.PieceOrder;
import gsm.repositories.PieceOrderRepository;
import gsm.service.PieceOrderService;
import gsm.service.SmsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.io.IOException;
import java.util.Optional;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/piece-orders")
public class PieceOrderController {

    private final PieceOrderService pieceOrderService;
    private final PieceOrderRepository pieceOrderRepository;
    @Autowired
    private SmsService smsService;

    @Autowired
    public PieceOrderController(PieceOrderService pieceOrderService, PieceOrderRepository pieceOrderRepository) {
        this.pieceOrderService = pieceOrderService;
        this.pieceOrderRepository = pieceOrderRepository;
    }

    @GetMapping
    public ResponseEntity<List<PieceOrder>> getAllPieceOrders() {
        List<PieceOrder> pieceOrders = pieceOrderService.getAllPieceOrders();
        return ResponseEntity.ok(pieceOrders);
    }
    @PostMapping
    public ResponseEntity<PieceOrder> createPieceOrder(@RequestBody PieceOrderRequest pieceOrderRequest) {
        PieceOrder createdPieceOrder = pieceOrderService.createPieceOrder(
                pieceOrderRequest.getPieceItems(),
                pieceOrderRequest.getFirstName(),
                pieceOrderRequest.getLastName(),
                pieceOrderRequest.getAddress(),
                pieceOrderRequest.getPhoneNumber(),
                pieceOrderRequest.getModeleName(),
                pieceOrderRequest.getMarqueName()
        );
        return ResponseEntity.ok(createdPieceOrder);
    }
    @GetMapping("/by-phone/{phoneNumber}")
    public ResponseEntity<List<PieceOrder>> getPieceOrdersByPhoneNumber(@PathVariable String phoneNumber) {
        List<PieceOrder> pieceOrders = pieceOrderRepository.findByPhoneNumber(phoneNumber);
        if (pieceOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pieceOrders);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<PieceOrder> updatePieceOrderStatus(@PathVariable Long id, @RequestBody UpdatePieceOrderRequest updateRequest) {
        Optional<PieceOrder> optionalPieceOrder = pieceOrderRepository.findById(id);
        if (optionalPieceOrder.isPresent()) {
            PieceOrder pieceOrder = optionalPieceOrder.get();
            boolean sendSms = false;
            String message = "";

            if (updateRequest.getStatus() != null) {
                pieceOrder.setStatus(updateRequest.getStatus());
                switch (updateRequest.getStatus().toLowerCase()) {
                    case "received":
                        message = String.format(" Mr./Ms %s %s GSM GUIDE informs you that your %s %s has been received.", 
                                                pieceOrder.getFirstName(), 
                                                pieceOrder.getLastName(), 
                                                pieceOrder.getMarqueName(), 
                                                pieceOrder.getModeleName());
                        sendSms = true;
                      
                        break;
                    // Add more cases as needed for other statuses
                }
            }

            if (updateRequest.getStatusReparation() != null) {
                pieceOrder.setStatusReparation(updateRequest.getStatusReparation());
                switch (updateRequest.getStatusReparation().toLowerCase()) {
                    case "in progress":
                        message = String.format("Mr./Ms %s %s GSM GUIDE informs you that  your %s %s is now in progress.",
                                                pieceOrder.getFirstName(),
                                                pieceOrder.getLastName(),
                                                pieceOrder.getMarqueName(),
                                                pieceOrder.getModeleName());
                        sendSms = true;
                        break;
                    case "repaired":
                        message = String.format("Mr./Ms %s %s GSM GUIDE informs you that  your %s %s has been repaired.",
                                                pieceOrder.getFirstName(),
                                                pieceOrder.getLastName(),
                                                pieceOrder.getMarqueName(),
                                                pieceOrder.getModeleName());
                        sendSms = true;
                        break;
                    case "checked":
                        message = String.format("Mr./Ms %s %s GSM GUIDE informs you that we will call you soon for more information about your %s %s.", 
                                                pieceOrder.getFirstName(), 
                                                pieceOrder.getLastName(), 
                                                pieceOrder.getMarqueName(), 
                                                pieceOrder.getModeleName());
                        sendSms = true; 
                        break;
  }
            }

            PieceOrder updatedPieceOrder = pieceOrderRepository.save(pieceOrder);

            if (sendSms) {
                smsService.sendSms(pieceOrder.getPhoneNumber(), message);
            }

            return ResponseEntity.ok(updatedPieceOrder);
        } else {
            // Handle case where PieceOrder with given id is not found
            return ResponseEntity.notFound().build();
        }
    }




    // Inner class for request body containing update details
    static class UpdatePieceOrderRequest {
        private String status;
        private String statusReparation;

        // Getters and Setters for all fields

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusReparation() {
            return statusReparation;
        }

        public void setStatusReparation(String statusReparation) {
            this.statusReparation = statusReparation;
        }
    }

    // Inner class for request body containing piece items and order details
    static class PieceOrderRequest {
        private List<PieceItemDto> pieceItems;
        private String firstName;
        private String lastName;
        private String address;
        private String phoneNumber;

        // Getters and Setters for all fields
        public String getModeleName() {
            return modeleName;
        }

        public void setModeleName(String modeleName) {
            this.modeleName = modeleName;
        }

        public String getMarqueName() {
            return marqueName;
        }

        public void setMarqueName(String marqueName) {
            this.marqueName = marqueName;
        } private String modeleName;  // New field
        private String marqueName;  
        public List<PieceItemDto> getPieceItems() {
            return pieceItems;
        }

        public void setPieceItems(List<PieceItemDto> pieceItems) {
            this.pieceItems = pieceItems;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
