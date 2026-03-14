package org.theoriok.inventory;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.theoriok.inventory.command.Command;
import org.theoriok.inventory.query.Query;

@AnalyzeClasses(packages = "org.theoriok.inventory", importOptions = ImportOption.DoNotIncludeTests.class)
class AnnotationArchTest {

    @ArchTest
    static final ArchRule commandClassesShouldBeAnnotatedWithCommand = classes()
        .that().resideInAPackage("org.theoriok.inventory.command")
        .and().areNotInterfaces()
        .and().areNotMemberClasses()
        .should().beAnnotatedWith(Command.class);

    @ArchTest
    static final ArchRule queryClassesShouldBeAnnotatedWithQuery = classes()
        .that().resideInAPackage("org.theoriok.inventory.query")
        .and().areNotInterfaces()
        .and().areNotMemberClasses()
        .should().beAnnotatedWith(Query.class);
}
