package com.semantalytics.stardog.kibble.util;

import com.complexible.stardog.Stardog;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.plan.eval.ExecutionException;
import com.semantalytics.stardog.kibble.util.UtilVocabulary;
import org.junit.*;
import org.openrdf.query.TupleQueryResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class TestBindPrevious  extends AbstractStardogTest {

   
    @Ignore
    @Test
    public void testBindPrev() throws Exception {

     
            final String aQuery = "prefix util: <" + UtilVocabulary.bindPrev.NAMESPACE + "> " +
                    "select ?result where { bind(util:bindPrev(?v) as ?result) values ?v {1 2 3 4 5} } order by ?v";


            List<String> results = new ArrayList(5);
            List<String> expected = Arrays.asList(null, "1", "2", "3", "4");

            try(final TupleQueryResult aResult = aConn.select(aQuery).execute()) {

                //final String aValue = aResult.next().getValue("result").stringValue();
                while(aResult.hasNext()) {
                    results.add(aResult.next().getValue("result").stringValue());
                }
            }

            assertEquals(expected, results);

        
    }

}
