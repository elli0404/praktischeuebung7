package contacttracer.persistence;

import contacttracer.aggregates.kontaktliste.KontaktListe;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KontaktListeRepository extends CrudRepository<KontaktListe, Long> {
  public List<KontaktListe> findAll();
}