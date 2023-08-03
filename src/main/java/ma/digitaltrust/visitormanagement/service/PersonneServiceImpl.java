package ma.digitaltrust.visitormanagement.service;

import ma.digitaltrust.visitormanagement.dao.PersonneRepository;
import ma.digitaltrust.visitormanagement.model.Personne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonneServiceImpl implements IPersonneService {

    @Autowired
    private PersonneRepository personneRepository;

    @Override
    public List<Personne> getAll() {
        return personneRepository.findAll();
    }
}
