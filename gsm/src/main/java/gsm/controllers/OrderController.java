package gsm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gsm.dto.OrderItemDto;
import gsm.entities.Command;
import gsm.service.OrderService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping
    public ResponseEntity<List<Command>> getAllOrders() {
        List<Command> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @PostMapping
    public ResponseEntity<Command> createOrder(@RequestBody OrderRequest orderRequest) {
        Command createdOrder = orderService.createOrder(orderRequest.getOrderItems(), orderRequest.getAddress(), orderRequest.getFirstName(), orderRequest.getLastName(), orderRequest.getPhoneNumber());
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Command> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest updateStatusRequest) {
        Command updatedOrder = orderService.updateOrderStatus(id, updateStatusRequest.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }
}

class OrderRequest {
	 private List<OrderItemDto> orderItems;
	    private String address;
	    private String firstName;
	    private String lastName;
	    private String phoneNumber;

    // Getters and Setters

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

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

	public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

class UpdateStatusRequest {
    private String status;

    // Getters and Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
