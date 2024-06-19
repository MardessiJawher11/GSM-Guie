package gsm.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class PieceOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String statusReparation;
    private Date date;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private Double totalPrice;

    private String modeleName; // New field for Modele name
    private String marqueName; // New field for Marque name

    @OneToMany(mappedBy = "pieceOrder", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pieceOrder")
    private List<PieceItem> pieceItems;

    // Getters and Setters for new fields
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
    }

    // Other Getters and Setters...

    // Existing getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<PieceItem> getPieceItems() {
        return pieceItems;
    }

    public void setPieceItems(List<PieceItem> pieceItems) {
        this.pieceItems = pieceItems;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
