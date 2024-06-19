package gsm.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gsm.entities.Marque;
import gsm.repositories.MarqueRepository;

@Service
public class MarqueServiceImpl implements MarqueService {

    @Autowired
    private MarqueRepository marqueRepository;

    public List<Marque> getAllMarques() {
        return marqueRepository.findAll();
    }

    public Marque findMarqueById(Long id) {
        return marqueRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Marque not found with ID: " + id));
    }

    public Marque createMarque(Marque marque) {
        return marqueRepository.save(marque);
    }

    public Marque updateMarqueById(Long id, Marque updatedMarque) {
        Optional<Marque> marqueOptional = marqueRepository.findById(id);
        if (marqueOptional.isPresent()) {
            Marque existingMarque = marqueOptional.get();
            if (updatedMarque.getNom() != null) {
                existingMarque.setNom(updatedMarque.getNom());
            }
            return marqueRepository.save(existingMarque);
        } else {
            throw new NoSuchElementException("Marque not found with ID: " + id);
        }
    }

    public void deleteMarque(Long id) {
        marqueRepository.deleteById(id);
    }



   
}



