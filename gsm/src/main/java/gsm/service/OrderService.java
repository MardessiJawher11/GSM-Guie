package gsm.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gsm.dto.OrderItemDto;
import gsm.entities.Command;
import gsm.entities.OrderItem;
import gsm.entities.Produit;
import gsm.repositories.CommandRepository;
import gsm.repositories.OrderItemRepository;
import gsm.repositories.ProduitRepository;

@Service
public class OrderService {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Command createOrder(List<OrderItemDto> orderItems, String address, String firstName, String lastName, String phoneNumber) {
        // Check if orderItems is null or empty
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order items list cannot be null or empty");
        }

        Command command = new Command();
        command.setDate(new Date());
        command.setAddress(address);
        command.setStatus("Pending");
        command.setTotalPrice(0.0);
        command.setFirstName(firstName);
        command.setLastName(lastName);
        command.setPhoneNumber(phoneNumber);

        command = commandRepository.save(command);

        double totalPrice = 0;

        for (OrderItemDto itemDto : orderItems) {
            Produit product = produitRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setCommand(command);
            orderItem.setQuantity(itemDto.getQuantity());

            totalPrice += product.getPrix() * itemDto.getQuantity();
            orderItemRepository.save(orderItem);
        }

        command.setTotalPrice(totalPrice);
        commandRepository.save(command);

        return command;
    }
    @Transactional(readOnly = true)

    public List<Command> getAllOrders() {
        return commandRepository.findAll();
    }
    @Transactional

    public Command updateOrderStatus(Long orderId, String status) {
        Command command = commandRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        command.setStatus(status);
        return commandRepository.save(command);
    }
}
