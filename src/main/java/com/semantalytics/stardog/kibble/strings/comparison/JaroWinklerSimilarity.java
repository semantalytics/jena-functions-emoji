package com.semantalytics.stardog.kibble.strings.comparison;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.Function;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import com.google.common.collect.Range;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;

public final class JaroWinklerSimilarity extends AbstractFunction implements StringFunction {


    protected JaroWinklerSimilarity() {
        super(Range.closed(2, 5), StringComparisonVocabulary.jaroWinklerSimilarity.stringValue());
    }

    private JaroWinklerSimilarity(final JaroWinklerSimilarity jaroWinklerSimilarity) {
        super(jaroWinklerSimilarity);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String firstString = assertStringLiteral(values[0]).stringValue();
        final String secondString = assertStringLiteral(values[1]).stringValue();

        float boostThreshold = 0.7f;
        float prefixScale = 0.1f;
        int maxPrefixLength = 4;

        if(values.length >= 3) {
            boostThreshold = assertNumericLiteral(values[2]).floatValue();
        }
        if(values.length >= 4) {
            prefixScale = assertNumericLiteral(values[3]).floatValue();
        }
        if(values.length == 5) {
            maxPrefixLength = assertNumericLiteral(values[4]).intValue();
        }

        final org.simmetrics.metrics.JaroWinkler jaroWinkler;
        jaroWinkler = new org.simmetrics.metrics.JaroWinkler(boostThreshold, prefixScale, maxPrefixLength);

        return literal(jaroWinkler.compare(firstString, secondString));
    }

    public Function copy() {
        return new JaroWinklerSimilarity(this);
    }

    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return StringComparisonVocabulary.jaroWinklerSimilarity.name();
    }
}