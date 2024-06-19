package gsm.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gsm.entities.Modele;
import gsm.repositories.ModeleRepository;

@Service
public class ModeleServiceImpl implements ModeleService {

    @Autowired
    private ModeleRepository modeleRepository;

    public List<Modele> getAllModeles() {
        return modeleRepository.findAll();
    }

    public Modele findModeleById(Long id) {
        return modeleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Modele not found with ID: " + id));
    }

    public Modele createModele(Modele modele) {
        return modeleRepository.save(modele);
    }

    public Modele updateModeleById(Long id, Modele updatedModele) {
        Optional<Modele> modeleOptional = modeleRepository.findById(id);
        if (modeleOptional.isPresent()) {
            Modele existingModele = modeleOptional.get();
            if (updatedModele.getNom() != null) {
                existingModele.setNom(updatedModele.getNom());
            }
            return modeleRepository.save(existingModele);
        } else {
            throw new NoSuchElementException("Modele not found with ID: " + id);
        }
    }

    public void deleteModele(Long id) {
        modeleRepository.deleteById(id);
    }



   
}



