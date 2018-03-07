package com.semantalytics.stardog.kibble.string.emoji;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import emoji4j.EmojiUtils;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;

public final class HexHtmlify extends AbstractFunction implements StringFunction {

    protected HexHtmlify() {
        super(1, EmojiVocabulary.hexHtmlify.stringValue());
    }

    private HexHtmlify(final HexHtmlify hexHtmlify) {
        super(hexHtmlify);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String string = assertStringLiteral(values[0]).stringValue();

        return literal(EmojiUtils.hexHtmlify(string));
    }

    @Override
    public HexHtmlify copy() {
        return new HexHtmlify(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return EmojiVocabulary.hexHtmlify.name();
    }
}
