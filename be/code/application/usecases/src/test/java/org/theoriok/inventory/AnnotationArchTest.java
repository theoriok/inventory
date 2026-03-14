package org.theoriok.inventory;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.theoriok.inventory.command.Command;
import org.theoriok.inventory.query.Query;

@AnalyzeClasses(packages = "org.theoriok.inventory", importOptions = ImportOption.DoNotIncludeTests.class)
class AnnotationArchTest {

    @ArchTest
    void commandClassesShouldBeAnnotatedWithCommand(JavaClasses classes) {
        classes()
            .that().resideInAPackage("org.theoriok.inventory.command")
            .and().areNotInterfaces()
            .and().areNotMemberClasses()
            .should().beAnnotatedWith(Command.class)
            .check(classes);
    }

    @ArchTest
    void queryClassesShouldBeAnnotatedWithQuery(JavaClasses classes) {
        classes()
            .that().resideInAPackage("org.theoriok.inventory.query")
            .and().areNotInterfaces()
            .and().areNotMemberClasses()
            .should().beAnnotatedWith(Query.class)
            .check(classes);
    }
}
