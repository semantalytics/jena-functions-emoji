package com.semantalytics.stardog.kibble.strings.string;

import com.complexible.common.rdf.model.Values;
import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import com.google.common.base.Joiner;
import com.google.common.collect.Range;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.model.Value;

import java.util.Arrays;

public final class StripAll extends AbstractFunction implements StringFunction {

    protected StripAll() {
        super(Range.all(), StringVocabulary.stripAll.stringValue());
    }

    private StripAll(final StripAll stripAll) {
        super(stripAll);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        for(final Value value : values) {
            assertStringLiteral(values[0]).stringValue();
        }

        final String[] strings = Arrays.stream(values).map(Value::stringValue).toArray(String[]::new);

        return Values.literal(Joiner.on("\u001f").join(StringUtils.stripAll(strings)));
    }

    @Override
    public StripAll copy() {
        return new StripAll(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return StringVocabulary.stripAll.name();
    }
}
