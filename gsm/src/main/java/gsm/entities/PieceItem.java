package gsm.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PieceItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private Piece piece;

    private Integer quantity;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public PieceOrder getPieceOrder() {
		return pieceOrder;
	}

	public void setPieceOrder(PieceOrder pieceOrder) {
		this.pieceOrder = pieceOrder;
	}
    @JsonIgnore

	@ManyToOne
    @JoinColumn(name = "piece_order_id")
    private PieceOrder pieceOrder;

    // Getters and Setters
}