package contacttracer.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import contacttracer.aggregates.kontaktliste.Index;
import contacttracer.aggregates.kontaktliste.KontaktListe;
import contacttracer.aggregates.kontaktliste.Kontaktperson;
import contacttracer.persistence.KontaktListeRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class KontaktListen {

  final
  KontaktListeRepository repo;

  public KontaktListen(KontaktListeRepository repo) {
    this.repo = repo;
  }

  public SortedMap<Integer, Long> kontakteNachZeitpunktSortiert() {
    List<KontaktListe> listen = repo.findAll();
    Map<Integer, Long> alter =
        listen.stream().collect(groupingBy(KontaktListen::alter, counting()));
    SortedMap<Integer, Long> sortedByAge = new TreeMap<>();
    sortedByAge.putAll(alter);
    return sortedByAge;
  }

  private static int alter(KontaktListe liste) {
    LocalDate erstbefund = liste.getIndex().getErstbefund();
    LocalDate now = LocalDate.now();
    return Period.between(erstbefund, now).getDays();
  }

  public List<KontaktListe> alle() {
    return repo.findAll();
  }

  public KontaktListe finde(long id) {
    return repo.findById(id).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,
            "Keine Liste mit id " + id + " vorhanden."));
  }

  public void createListe(String nachname, String vorname) {
    KontaktListe liste = new KontaktListe();
    liste.setIndex(new Index(nachname, vorname, LocalDate.now()));
    repo.save(liste);
  }

  public void kontaktpersonEntfernen(long id, Kontaktperson kontaktperson) {
    KontaktListe liste = finde(id);
    liste.removeKontakt(kontaktperson);
    repo.save(liste);
  }

  public void kontaktpersonHinzufuegen(long id, Kontaktperson kontaktperson) {
    KontaktListe liste = finde(id);
    liste.addKontakt(kontaktperson);
    repo.save(liste);
  }


}