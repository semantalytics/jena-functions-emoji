package com.semantalytics.stardog.kibble.string.emoji;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import emoji4j.EmojiUtils;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;

public final class Emoticon extends AbstractFunction implements StringFunction {

    protected Emoticon() {
        super(1, EmojiVocabulary.emoticon.stringValue());
    }

    private Emoticon(final Emoticon emoticon) {
        super(emoticon);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String string = assertStringLiteral(values[0]).stringValue();

        return literal(String.join(",", EmojiUtils.getEmoji(string).getEmoticons()));
    }

    @Override
    public Emoticon copy() {
        return new Emoticon(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return EmojiVocabulary.emoticon.name();
    }
}
