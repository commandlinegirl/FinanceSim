package com.codelemma.finances;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TypedContainer implements Iterable<Entry<TypedKey<?>, Object>> {
	private enum ParserState {
		EXPECT_KEYWORD,
		EXPECT_EQUALS,
		EXPECT_VALUE,
		EXPECT_SEMICOLON,
	};
	
	private Map<TypedKey<?>, Object> values = new HashMap<TypedKey<?>, Object>();
	
	// Empty TypedContainer.
	public TypedContainer() {
	}	

	public TypedContainer(String string) throws ParseException {
	    TokenIterator iterator = new TokenIterator(string);
	    if (!iterator.hasNext()) {
	    	throw new ParseException("Cannot parse empty string");
	    }
		String token = iterator.next();
		if (!token.equals("{")) {
			throw new ParseException("Container must begin with '{'");
		}
		this.values = parse(iterator);
	}
	
	TypedContainer(TokenIterator iterator) throws ParseException {
		this.values = parse(iterator);
	}
	
	private Map<TypedKey<?>, Object> parse(TokenIterator iterator) throws ParseException {
		if (!iterator.hasNext()) {
			throw new ParseException("Cannot parse empty string");
		}
		Map<TypedKey<?>, Object> values = new HashMap<TypedKey<?>, Object>();
		ParserState state = ParserState.EXPECT_KEYWORD;
		String token = "";
		TypedKey<?> key = null;
		Object value = null;
		while (iterator.hasNext() && !(token = iterator.next()).equals("}")) {
			switch (state) {
			case EXPECT_KEYWORD:
				key = TypedKey.getKey(token);
				if (key == null) {
					throw new ParseException("Invalid key: " + token);
				}
				state = ParserState.EXPECT_EQUALS;
				break;
			case EXPECT_EQUALS:
				if (!token.equals("=")) {
					throw new ParseException("Expected equal sign");
				}
				state = ParserState.EXPECT_VALUE;
				break;
			case EXPECT_VALUE:
				if (token.equals("{")) {
					value = new TypedContainer(iterator);
				} else {
					value = key.parseValue(token);
				}
				state = ParserState.EXPECT_SEMICOLON;
				break;
			case EXPECT_SEMICOLON:
				if (!token.equals(";")) {
					throw new ParseException("Expected semicolon");
				}
				values.put(key, value);
				state = ParserState.EXPECT_KEYWORD;
				break;
			default:
				throw new ParseException("Unknown parser state");
			}
		}
		if (!token.equals("}")) {
			throw new ParseException("Unexpected end of string");
		}
		switch (state) {
		case EXPECT_SEMICOLON:
			values.put(key, value);
			break;
		case EXPECT_KEYWORD:
			break;
		default:
			throw new ParseException("Unexpected '}'");
		}
		return values;
	}	
	
	public <T> void put(TypedKey<T> key, T value) {
		values.put(key, value);
	}

	public <T> T get(TypedKey<T> key) {
		return key.cast(values.get(key));
	}

	public int size() {
		return values.size();
	}
	
	@Override
	public Iterator<Entry<TypedKey<?>, Object>> iterator() {
		return values.entrySet().iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{");
		for (Map.Entry<TypedKey<?>, Object> entry : values.entrySet()) {
			buf.append(entry.getKey())
			   .append("=")
			   .append(entry.getValue())
			   .append(";");
		}
		buf.append("}");
		return buf.toString();
	}
}