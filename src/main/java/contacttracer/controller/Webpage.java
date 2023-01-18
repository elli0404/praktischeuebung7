package contacttracer.controller;

import contacttracer.aggregates.kontaktliste.Kontaktperson;
import contacttracer.service.KontaktListen;
import java.util.SortedMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Webpage {

  final
  KontaktListen listen;

  public Webpage(KontaktListen listen) {
    this.listen = listen;
  }


  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("listen", listen.alle());
    return "index";
  }

  @GetMapping("/liste/{id}")
  public String details(@PathVariable("id") long id, Model model) {
    model.addAttribute("liste", listen.finde(id));
    return "details";
  }

  @PostMapping("/")
  public String erzeugeListe(String nachname, String vorname) {
    listen.createListe(nachname, vorname);
    return "redirect:/";
  }

  @PostMapping("/liste/{id}")
  public String kontaktpersonHinzufuegen(@PathVariable("id") long id,
                                         Kontaktperson kontaktperson) {
    listen.kontaktpersonHinzufuegen(id, kontaktperson);
    return "redirect:/liste/" + id;
  }

  @PostMapping("/remove/from/{id}")
  public String kontaktpersonEntfernen(@PathVariable("id") long id,
                                       Kontaktperson kontaktperson) {
    listen.kontaktpersonEntfernen(id, kontaktperson);
    return "redirect:/liste/" + id;
  }

  @GetMapping("/report")
  public String report(Model model) {
    SortedMap<Integer, Long> sortedByAge = listen.kontakteNachZeitpunktSortiert();
    model.addAttribute("alter", sortedByAge);
    return "report";
  }




}