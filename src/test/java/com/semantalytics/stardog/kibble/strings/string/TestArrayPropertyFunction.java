/*
 * Copyright (c) 2010-2016 Complexible, Inc <http://complexible.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.semantalytics.stardog.kibble.strings.string;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.complexible.common.openrdf.query.BindingSets;
import com.complexible.common.rdf.model.Namespaces;
import com.complexible.common.rdf.model.StardogValueFactory;
import com.complexible.stardog.index.statistics.Accuracy;
import com.complexible.stardog.index.statistics.Cardinality;
import com.complexible.stardog.plan.PlanNode;
import com.complexible.stardog.plan.PlanNodes;
import com.complexible.stardog.plan.eval.ExecutionException;
import com.complexible.stardog.plan.parser.QueryParserImpl;
import com.google.common.collect.Lists;
import com.semantalytics.stardog.kibble.AbstractStardogTest;
import info.aduna.iteration.Iterations;
import org.junit.Test;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import static com.complexible.common.rdf.model.Values.literal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestArrayPropertyFunction extends AbstractStardogTest {
    @Test(expected = ExecutionException.class)
    public void tooManyResultsThrowsError() {

        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { (?too ?many ?args) string:array (\"stardog\") }";

        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }


    @Test(expected = ExecutionException.class)
    public void resultTermsWhichAreNotVariablesShouldBeAnError() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { (\"no literals allowed\") string:array (\"stardog\") }";

        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test(expected = ExecutionException.class)
    public void tooManyInputsThrowsError() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { ?result string:array (\"stardog\" 5) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();

        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test(expected = ExecutionException.class)
    public void argCannotBeANonnumericLiteral() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { ?result string:array (5) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test(expected = ExecutionException.class)
    public void argCannotBeAnIRI() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { ?result string:array (<http://example.com>) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test
    public void argCannotBeABNode() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { ?result string:array (_:bnode) }";

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {
            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    @Test
    public void varInputWithNoResultsShouldProduceZeroResults() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { ?result string:array (?input) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            assertFalse(aResult.hasNext());
        } finally {
            aResult.close();
        }
    }

    @Test
    public void simpleStringArray() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { ?result string:array (\"star\u001fdog\") }";

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            final List<Value> aExpected = Lists.newArrayList(literal("star"), literal("dog"));
            final List<Value> aResults = Iterations.stream(aResult).map(BindingSets.select("result")).collect(Collectors.toList());

            assertEquals(aExpected, aResults);
        }
    }

    @Test
    public void stringArrayWithIndex() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { (?result ?idx) string:array (\"star\u001fdog\") }";

        BindingSet aBindingSet;

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            aBindingSet = aResult.next();

            assertEquals(literal("star"), aBindingSet.getValue("result"));
            assertEquals(literal(0, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            aBindingSet = aResult.next();

            assertEquals(literal("dog"), aBindingSet.getValue("result"));
            assertEquals(literal(1, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    @Test
    public void repeatWithVarInput() {
        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                " select * where { (?result ?idx) string:array (?in) . values ?in { \"star\u001fdog\u001fdatabase\"} }";

        BindingSet aBindingSet;

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            aBindingSet = aResult.next();

            assertEquals(literal("star"), aBindingSet.getValue("result"));
            assertEquals(literal(0, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            aBindingSet = aResult.next();

            assertEquals(literal("dog"), aBindingSet.getValue("result"));
            assertEquals(literal(1, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            aBindingSet = aResult.next();

            assertEquals(literal("database"), aBindingSet.getValue("result"));
            assertEquals(literal(2, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    /*
    @Test
    public void costAndCardinalityShouldBeCorrect() throws Exception {
        final String aQueryStr = "select * where { (?result ?idx) <tag:stardog:api:repeat> (\"foo\" 5) }";

        Optional<PlanNode> aResult = PlanNodes.find(new QueryParserImpl().parseQuery(aQueryStr, Namespaces.STARDOG).getNode(),
                                                    PlanNodes.is(ArrayPropertyFunction.ArrayPlanNode.class));

        assertTrue(aResult.isPresent());

        ArrayPropertyFunction.ArrayPlanNode aNode = (ArrayPropertyFunction.ArrayPlanNode) aResult.get();

        new ArrayPropertyFunction().estimate(aNode);

        assertEquals(5d, aNode.getCost(), .00001);

        assertEquals(Accuracy.ACCURATE, aNode.getCardinality().accuracy());
        assertEquals(5d, aNode.getCardinality().value(), .00001);
    }

    @Test
    public void costAndCardinalityShouldBeCorrectWithArg() throws Exception {
        final String aQueryStr = "select * where { (?result ?idx) <tag:stardog:api:repeat> (?in 5) . values ?in { \"foo\" \"bar\" \"baz\"} }";

        Optional<PlanNode> aResult = PlanNodes.find(new QueryParserImpl().parseQuery(aQueryStr, Namespaces.STARDOG).getNode(),
                                                  PlanNodes.is(ArrayPropertyFunction.ArrayPlanNode.class));

        assertTrue(aResult.isPresent());

        ArrayPropertyFunction.ArrayPlanNode aNode = (ArrayPropertyFunction.ArrayPlanNode) aResult.get();

        aNode.getArg().setCardinality(Cardinality.of(3, Accuracy.ACCURATE));
        aNode.getArg().setCost(3);

        new ArrayPropertyFunction().estimate(aNode);

        assertEquals(18d, aNode.getCost(), .00001);

        assertEquals(Accuracy.ACCURATE, aNode.getCardinality().accuracy());
        assertEquals(15d, aNode.getCardinality().value(), .00001);
    }

*/
    @Test
    public void shouldRenderACustomExplanation() {

        final String aQueryStr = StringVocabulary.sparqlPrefix("string") +
                "select * where { (?result ?idx) string:array (\"star\u001fdog\") }";

        System.out.println(connection.select(aQueryStr).explain());
        assertTrue(connection.select(aQueryStr).explain().contains("StringArray("));
    }
}