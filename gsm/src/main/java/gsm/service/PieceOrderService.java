package gsm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gsm.dto.PieceItemDto;
import gsm.entities.Piece;
import gsm.entities.PieceItem;
import gsm.entities.PieceOrder;
import gsm.repositories.PieceItemRepository;
import gsm.repositories.PieceOrderRepository;
import gsm.repositories.PieceRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PieceOrderService {

    @Autowired
    private PieceOrderRepository pieceOrderRepository;

    @Autowired
    private PieceItemRepository pieceItemRepository;

    @Autowired
    private PieceRepository pieceRepository;

    public List<PieceOrder> getAllPieceOrders() {
        return pieceOrderRepository.findAll();
    }

    public PieceOrder createPieceOrder(List<PieceItemDto> pieceItems, String firstName, String lastName, String address, String phoneNumber, String modeleName, String marqueName) {
        PieceOrder pieceOrder = new PieceOrder();
        pieceOrder.setStatus("Not Delivered"); // Initial status
        pieceOrder.setDate(new Date()); // Current date/time
        pieceOrder.setStatusReparation("Pending"); // Initial status for reparation

        pieceOrder.setFirstName(firstName);
        pieceOrder.setLastName(lastName);
        pieceOrder.setAddress(address);
        pieceOrder.setPhoneNumber(phoneNumber);

        List<PieceItem> savedPieceItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (PieceItemDto pieceItemDto : pieceItems) {
            Optional<Piece> optionalPiece = pieceRepository.findById(pieceItemDto.getPieceId());
            if (optionalPiece.isPresent()) {
                Piece piece = optionalPiece.get();
                PieceItem pieceItem = new PieceItem();
                pieceItem.setPiece(piece);
                pieceItem.setQuantity(pieceItemDto.getQuantity());
                pieceItem.setPieceOrder(pieceOrder);
                savedPieceItems.add(pieceItem);
                if (piece.getId() != 6) {
                    totalPrice += piece.getPrix() * pieceItemDto.getQuantity();
                } else {
                    // Set price to 0 if pieceId is 6
                    totalPrice = 0;
                }
                // Check if the pieceId is 6 and use the provided marqueName and modeleName
                if (piece.getId() == 6) {
                    if (modeleName == null || marqueName == null) {
                        throw new IllegalArgumentException("Modele name and marque name must be provided for diagnostic piece.");
                    }
                    pieceOrder.setModeleName(modeleName);
                    pieceOrder.setMarqueName(marqueName);
                } else {
                    // Automatically set modeleName and marqueName based on the first non-diagnostic piece item
                    if (modeleName == null && marqueName == null) {
                        modeleName = piece.getModele().getNom();
                        marqueName = piece.getModele().getMarque().getNom();
                    }
                }
            }
        }

        pieceOrder.setPieceItems(savedPieceItems);
        pieceOrder.setTotalPrice(totalPrice);
        if (pieceOrder.getModeleName() == null && modeleName != null) {
            pieceOrder.setModeleName(modeleName);
        }
        if (pieceOrder.getMarqueName() == null && marqueName != null) {
            pieceOrder.setMarqueName(marqueName);
        }

        return pieceOrderRepository.save(pieceOrder);
    }

    public PieceOrder updatePieceOrderStatus(Long id, String status) {
        Optional<PieceOrder> optionalPieceOrder = pieceOrderRepository.findById(id);
        if (optionalPieceOrder.isPresent()) {
            PieceOrder pieceOrder = optionalPieceOrder.get();
            pieceOrder.setStatus(status);
            return pieceOrderRepository.save(pieceOrder);
        } else {
            // Handle case where PieceOrder with given id is not found
            return null;
        }
    }
}
