package com.semantalytics.stardog.kibble.strings.string;

import com.semantalytics.stardog.kibble.AbstractStardogTest;
import org.junit.*;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import static org.junit.Assert.*;

public class TestCenter extends AbstractStardogTest {

    @Test
    public void testAbbreviateMiddleTwoArg() {
    
        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?result where { bind(string:center(\"Stardog\", 15) AS ?result) }";

            try (final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final String aValue = aResult.next().getValue("result").stringValue();

                assertEquals("    Stardog    ", aValue);
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testAbbreviateMiddleThreeArg() {
      
        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?result where { bind(string:center(\"Stardog\", 15, \"*\") AS ?result) }";

            try (final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final String aValue = aResult.next().getValue("result").stringValue();

                assertEquals("****Stardog****", aValue);
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testEmptyString() {

        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?abbreviation where { bind(string:center(\"\", 5) as ?abbreviation) }";

            try(final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final String aValue = aResult.next().getValue("abbreviation").stringValue();

                assertEquals("     ", aValue);
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testTooFewArgs() {

        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?abbreviation where { bind(string:center(\"one\") as ?abbreviation) }";

            try(final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final BindingSet aBindingSet = aResult.next();

                assertTrue("Should have no bindings", aBindingSet.getBindingNames().isEmpty());
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testTooManyArgs() {
     
        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?abbreviation where { bind(string:center(\"one\", 2, \"three\", \"four\") as ?abbreviation) }";

            try(final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final BindingSet aBindingSet = aResult.next();

                assertTrue("Should have no bindings", aBindingSet.getBindingNames().isEmpty());
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testWrongTypeFirstArg() {
     
        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?abbreviation where { bind(string:center(4, 5) as ?abbreviation) }";

            try(final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final BindingSet aBindingSet = aResult.next();

                assertTrue("Should have no bindings", aBindingSet.getBindingNames().isEmpty());
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testWrongTypeSecondArg() {
 
        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?abbreviation where { bind(string:center(\"one\", \"two\") as ?abbreviation) }";

            try(final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final BindingSet aBindingSet = aResult.next();

                assertTrue("Should have no bindings", aBindingSet.getBindingNames().isEmpty());
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }

    @Test
    public void testWrongTypeThirdArg() {
       
        final String aQuery = StringVocabulary.sparqlPrefix("string") +
                    "select ?result where { bind(string:center(\"one\", 2, 3) as ?result) }";

            try(final TupleQueryResult aResult = connection.select(aQuery).execute()) {

                assertTrue("Should have a result", aResult.hasNext());

                final BindingSet aBindingSet = aResult.next();

                assertTrue("Should have no bindings", aBindingSet.getBindingNames().isEmpty());
                assertFalse("Should have no more results", aResult.hasNext());
            }
    }
}