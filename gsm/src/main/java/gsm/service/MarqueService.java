package gsm.service;

import java.util.List;

import gsm.entities.Marque;

public interface MarqueService {
	  List<Marque> getAllMarques();
	    Marque findMarqueById(Long id);
	    Marque createMarque(Marque marque);
	    Marque updateMarqueById(Long id, Marque updatedMarque);
	    void deleteMarque(Long id);

}
