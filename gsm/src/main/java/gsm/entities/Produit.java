package gsm.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity

public class Produit implements Serializable {
	 @Override
	public String toString() {
		return "Produit [id=" + id + ", nom=" + nom + ", color=" + color + ", prix=" + prix + ", description="
				+ description + ", etat=" + etat + ", image=" + Arrays.toString(image) + ", categorie=" + categorie
				+ "]";
	}
	@Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;
	 private String nom;
	 private String color;
	 private Double prix;
	 private String description;
	 private String etat;
	 @Lob
		@Column(columnDefinition = "LONGBLOB")
	    private byte[] image;
	 @ManyToOne // Many products can belong to one category
	    private Categorie categorie;

	    
	    public Categorie getCategorie() {
	        return categorie;
	    }

	    public void setCategorie(Categorie categorie) {
	        this.categorie = categorie;
	    }
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Double getPrix() {
		return prix;
	}
	public void setPrix(Double prix) {
		this.prix = prix;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	
}}
