package com.semantalytics.stardog.kibble.strings.string;

import com.complexible.common.rdf.model.Values;
import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.model.Value;

public final class IndexOfAnyChar extends AbstractFunction implements StringFunction {

    protected IndexOfAnyChar() {
        super(2, StringVocabulary.indexOfAnyChar.stringValue());
    }

    private IndexOfAnyChar(final IndexOfAnyChar indexOfAnyChar) {
        super(indexOfAnyChar);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String string = assertStringLiteral(values[0]).stringValue();
        final String searchChars = assertStringLiteral(values[1]).stringValue();

        return Values.literal(StringUtils.indexOfAny(string, searchChars));
    }

    @Override
    public IndexOfAnyChar copy() {
        return new IndexOfAnyChar(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return StringVocabulary.indexOfAnyChar.name();
    }
}