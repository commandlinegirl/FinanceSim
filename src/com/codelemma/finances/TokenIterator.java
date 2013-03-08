package com.codelemma.finances;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class TokenIterator implements Iterator<String> {
	private String[] tokens;
	private int next;

	private static class Tokenizer {
		private String buffer;
		private int position;
		
		public Tokenizer(String buffer) {
			this.buffer = buffer;
			this.position = 0;
		}
		
		private int getCharacterPosition(char c, String s, int start) {
			int p = s.indexOf(c, start);
			return p >= 0 ? p : s.length();
		}
		
		private int getNextPosition() {
			int openCurl = getCharacterPosition('{', buffer, position);
			int closeCurl = getCharacterPosition('}', buffer, position);
			int equals = getCharacterPosition('=', buffer, position);
			int semicolon = getCharacterPosition(';', buffer, position);
			int separator = Math.min(Math.min(openCurl, closeCurl), Math.min(equals, semicolon));
			if (position == separator) {
				return position + 1;
			} else {
				return separator;
			}
		}
		
		public String[] getTokens() {
			List<String> tokens = new LinkedList<String>();
			while (position < buffer.length()) {
				int nextPosition = getNextPosition();
				tokens.add(buffer.substring(position, nextPosition));
				position = nextPosition;
			}
			return tokens.toArray(new String[0]);
		}		
	}
	
	public TokenIterator(String buffer) {
		this.tokens = new Tokenizer(buffer).getTokens();
		this.next = 0;
	}
	
	@Override
	public boolean hasNext() {
		return next < tokens.length;
	}
	
	@Override
	public String next() {
		return tokens[next++];
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove from String.");
	}
}
