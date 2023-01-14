package contacttracer.aggregates.kontaktliste;

import java.time.LocalDate;
import java.time.Period;

public record Index(String nachname,String vorname,LocalDate erstbefund) {

  public long getTage() {
    return Period.between(erstbefund, LocalDate.now()).getDays();
  }

  public LocalDate getErstbefund() {
    return erstbefund;
  }
}
