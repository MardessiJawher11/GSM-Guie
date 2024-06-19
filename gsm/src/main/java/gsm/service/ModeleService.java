package gsm.service;

import java.util.List;

import gsm.entities.Modele;

public interface ModeleService {
	  List<Modele> getAllModeles();
	    Modele findModeleById(Long id);
	    Modele createModele(Modele modele);
	    Modele updateModeleById(Long id, Modele updatedModele);
	    void deleteModele(Long id);
}