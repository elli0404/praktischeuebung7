package contacttracer.aggregates.kontaktliste;

import contacttracer.stereotypes.ClassOnly;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;

public class KontaktListe {

  @Id
  private Long id = null;
  private Index index = null;
  private Set<Kontaktperson> kontakte = new HashSet<>();
  private LocalDateTime changed;

  private LocalDate created;


  public void addKontakt(Kontaktperson person) {
    kontakte.add(person);
    touch();
  }

  @ClassOnly
  private void touch() {
    changed = LocalDateTime.now();
  }


  public Index getIndex() {
    return index;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    if (id == null) {
      this.id = id;
    }
    else {
      throw new IllegalStateException("ID ist schon gesetzt");
    }
  }

  public void setIndex(Index index) {
    if (this.index == null) {
      this.index = index;
      touch();
    }
    else {
      throw new IllegalStateException("Index ist schon gesetzt");
    }
  }

  public Set<Kontaktperson> getKontakte() {
    return kontakte;
  }


  public String getChanged() {
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    String formated = changed.format(formatter);
    return formated;
  }

  public void removeKontakt(Kontaktperson kontaktperson) {
    for (Kontaktperson k : kontakte) {
      System.out.println(k.equals(kontaktperson));
    }
    kontakte.remove(kontaktperson);
    touch();
  }

  public int size() {
    return kontakte.size();
  }

  @Override
  public String toString() {
    return "KontaktListe{" +
           "id=" + id +
           ", index=" + index +
           ", changed=" + changed +
           ", created=" + created +
           '}';
  }
}
