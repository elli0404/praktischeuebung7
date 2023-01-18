import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.codeUnits;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMembers;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static com.tngtech.archunit.thirdparty.com.google.common.io.Resources.getResource;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import contacttracer.ContacttracerApplication;
import contacttracer.stereotypes.AggregateRoot;
import contacttracer.stereotypes.ClassOnly;
import contacttracer.stereotypes.Wertobjekt;
import org.springframework.stereotype.Component;

@AnalyzeClasses(packagesOf = ContacttracerApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)

public class ArchUnitTest {


  // Kein Java Artefakt (z.B. Klasse, Interface, …) oder Methode ist mit @Deprecated annotiert.
  @ArchTest
  static final ArchRule noDeprecatedClasses =
      noClasses().should().beAnnotatedWith(Deprecated.class);

  @ArchTest
  static final ArchRule noDeprecatedMethods =
      noMembers().should().beAnnotatedWith(Deprecated.class);





  // Controller verwenden niemals direkt ein Repository, sondern Application-Services.
  @ArchTest
  static final ArchRule controllersShouldNotDirectlyAccessRepositories = noClasses().that()
      .resideInAnyPackage("..controller..")
      .should()
      .accessClassesThat().resideInAnyPackage("..persistence..");





  // Es wird ausschließlich Konstruktor-Injection verwendet.
  // Vermutlich ist es hier einfacher, die anderen Formen von Injection auszuschließen.
  @ArchTest
  static final ArchRule onlyConstructorInjection =
      GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;





  // Die Architektur ist eine Schichtenarchitektur mit 3 Schichten (keine Onion Architektur!).
  // Verwenden Sie die in ArchUnit eingebaute Methode zum Check einer Schichtarchitektur.
  @ArchTest
  static final ArchRule layerTest = layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("controller").definedBy("..controller..")
      .layer("model").definedBy("..aggregates.kontaktliste..")
      .layer("service").definedBy("..service..")
      .layer("repository").definedBy("..persistence..");




  // TODO: berichtigen
  // Für die Dependency Injection werden die Stereotypen @Controller, @Service und @Repository verwendet.
  // Vermutlich ist es hier geschickter auszuschließen, dass die vierte mögliche Annotation benutzt wird.
  @ArchTest
  static final ArchRule dependencyInjShouldNotUseComponentAnnotation =
      noClasses()
      .should()
      .dependOnClassesThat().areAnnotatedWith(Component.class);




  // Erzeugen Sie zwei Annotationen AggregateRoot und Wertobjekt im Package stereotypes, die für
  // ArchUnit-Tests zugreifbar sind und schreiben Sie einen Test, der sicherstellt, dass alle Klassen,
  // die außerhalb des jeweiligen Aggregat-Packages sichtbar sind,
  // mit einer der beiden Annotationen markiert wurden.
  @ArchTest
  static final ArchRule dumm = classes()
      .that()
      .resideOutsideOfPackage("..aggregates.kontaktliste..")
      .should()
      .beAnnotatedWith(AggregateRoot.class)
      .orShould()
      .beAnnotatedWith(Wertobjekt.class);





  // Alle Methoden und Konstruktoren von Klassen sind public, wenn sie nicht mit @ClassOnly annotiert sind.
  @ArchTest
  static final ArchRule codeUnitsNotAnnotatedWithClassOnlyShouldBePublic =
      codeUnits().that().areNotAnnotatedWith(ClassOnly.class).should().bePublic();






  // TODO: Erstellen Sie ein Plant-UML Diagramm der Architektur und schreiben Sie einen ArchUnit
  //  der prüft, dass der Code das Diagramm korrekt implementiert. Das Plant-UML Plugin für
  //  IntelliJ kann hier ganz hilfreich sien, ansonsten können Sie aber auch den Online Server auf
  //  der Webseite https://plantuml.com/ verwenden.

  @ArchTest
  static final ArchRule rule7 = classes()
      .should(adhereToPlantUmlDiagram(getResource("HA2.puml"),
          consideringOnlyDependenciesInDiagram()));




}