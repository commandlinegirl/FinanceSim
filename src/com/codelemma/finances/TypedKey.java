package com.codelemma.finances;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;

import com.codelemma.finances.accounting.Investment401k;

public class TypedKey<T> {
	private static HashMap<String, TypedKey<?>> keywordToTypedKey = new HashMap<String, TypedKey<?>>();
	private static HashMap<String, ValueFactory<?>> nameToFactory = new HashMap<String, ValueFactory<?>>();
	
	public static final TypedKey<Integer> INCOME_ID = of("income_id", Integer.class);
	public static final TypedKey<String> INCOME_NAME = of("income_name", String.class);	
	public static final TypedKey<BigDecimal> YEARLY_INCOME = of("yearly_income", BigDecimal.class);
	public static final TypedKey<BigDecimal> INCOME_TAX_RATE = of("income_tax_rate", BigDecimal.class);
	public static final TypedKey<BigDecimal> YEARLY_INCOME_RISE = of("yearly_income_rise", BigDecimal.class);
	public static final TypedKey<BigDecimal> INCOME_INSTALLMENTS = of("income_installments", BigDecimal.class);	
	public static final TypedKey<Integer> INCOME_TERM = of("income_term", Integer.class);	
	public static final TypedKey<Integer> INCOME_START_YEAR = of("income_start_year", Integer.class);	
	public static final TypedKey<Integer> INCOME_START_MONTH = of("income_start_month", Integer.class);	
		
	public static final TypedKey<Integer> INVESTMENTSAV_ID = of("investmentsav_id", Integer.class);
	public static final TypedKey<String> INVESTMENTSAV_NAME = of("investmentsav_name", String.class);
	public static final TypedKey<BigDecimal> INVESTMENTSAV_INIT_AMOUNT = of("investmentsav_init_amount", BigDecimal.class);
	public static final TypedKey<BigDecimal> INVESTMENTSAV_TAX_RATE = of("investmentsav_tax_rate", BigDecimal.class);
	public static final TypedKey<BigDecimal> INVESTMENTSAV_PERCONTRIB = of("investmentsav_percontrib", BigDecimal.class);			
	public static final TypedKey<Integer> INVESTMENTSAV_CAPITALIZATION = of("investmentsav_capitalization", Integer.class);
	public static final TypedKey<BigDecimal> INVESTMENTSAV_INTEREST_RATE = of("investmentsav_interest_rate", BigDecimal.class);
	public static final TypedKey<Integer> INVESTMENTSAV_START_YEAR = of("investmentsav_start_year", Integer.class);	
	public static final TypedKey<Integer> INVESTMENTSAV_START_MONTH = of("investmentsav_start_month", Integer.class);	
	
	public static final TypedKey<Integer> INVESTMENTCHECK_ID = of("investmentcheck_id", Integer.class);
	public static final TypedKey<String> INVESTMENTCHECK_NAME = of("investmentcheck_name", String.class);
	public static final TypedKey<BigDecimal> INVESTMENTCHECK_INIT_AMOUNT = of("investmentcheck_init_amount", BigDecimal.class);
	public static final TypedKey<BigDecimal> INVESTMENTCHECK_TAX_RATE = of("investmentcheck_tax_rate", BigDecimal.class);
	public static final TypedKey<Integer> INVESTMENTCHECK_CAPITALIZATION = of("investmentcheck_capitalization", Integer.class);
	public static final TypedKey<BigDecimal> INVESTMENTCHECK_INTEREST_RATE = of("investmentcheck_interest_rate", BigDecimal.class);	
	public static final TypedKey<Integer> INVESTMENTCHECK_START_YEAR = of("investmentsav_start_year", Integer.class);	
	public static final TypedKey<Integer> INVESTMENTCHECK_START_MONTH = of("investmentsav_start_month", Integer.class);	
	
	public static final TypedKey<Integer> INVESTMENT401K_ID = of("investment401k_id", Integer.class);
	public static final TypedKey<String> INVESTMENT401K_NAME = of("investment401k_name", String.class);	
	public static final TypedKey<BigDecimal> INVESTMENT401K_INIT_AMOUNT = of("investment401k_init_amount", BigDecimal.class);
	public static final TypedKey<BigDecimal> INVESTMENT401K_PERCONTRIB = of("investment401k_percontrib", BigDecimal.class);	
	public static final TypedKey<Integer> INVESTMENT401K_PERIOD = of("investment401k_period", Integer.class);
	public static final TypedKey<BigDecimal> INVESTMENT401K_INTEREST_RATE = of("investment401k_interest_rate", BigDecimal.class);	
	public static final TypedKey<Integer> INVESTMENT401K_INCOMEID = of("investment401k_incomeid", Integer.class);	//TODO: how to recover id?
	public static final TypedKey<BigDecimal> INVESTMENT401K_WITHDRAWAL_TAX_RATE = of("investment401k_withdrawal_tax_rate", BigDecimal.class);
	public static final TypedKey<BigDecimal> INVESTMENT401K_EMPLOYER_MATCH = of("investment401k_employer_match", BigDecimal.class);	
	public static final TypedKey<Integer> INVESTMENT401K_START_YEAR = of("investment401k_start_year", Integer.class);	
	public static final TypedKey<Integer> INVESTMENT401K_START_MONTH = of("investment401k_start_month", Integer.class);	
	
	public static final TypedKey<Integer> EXPENSE_ID = of("expense_id", Integer.class);
	public static final TypedKey<String> EXPENSE_NAME = of("expense_name", String.class);
	public static final TypedKey<BigDecimal> INIT_EXPENSE = of("init_expense", BigDecimal.class);
	public static final TypedKey<BigDecimal> INFLATION_RATE = of("inflation_rate", BigDecimal.class);
	public static final TypedKey<Integer> EXPENSE_FREQUENCY = of("expense_frequency", Integer.class);
	public static final TypedKey<Integer> EXPENSE_START_YEAR = of("expense_start_year", Integer.class);	
	public static final TypedKey<Integer> EXPENSE_START_MONTH = of("expense_start_month", Integer.class);	
	
	public static final TypedKey<Integer> DEBTLOAN_ID = of("debtloan_id", Integer.class);
	public static final TypedKey<String> DEBTLOAN_NAME = of("debtloan_name", String.class);
	public static final TypedKey<BigDecimal> DEBTLOAN_AMOUNT = of("debtloan_amount", BigDecimal.class);
	public static final TypedKey<BigDecimal> DEBTLOAN_INTEREST_RATE = of("debtloan_interest_rate", BigDecimal.class);
	public static final TypedKey<Integer> DEBTLOAN_TERM = of("debtloan_term", Integer.class);
	public static final TypedKey<BigDecimal> DEBTLOAN_EXTRA_PAYMENT = of("debtloan_extra_payment", BigDecimal.class);		
	public static final TypedKey<Integer> DEBTLOAN_START_YEAR = of("debtloan_start_year", Integer.class);	
	public static final TypedKey<Integer> DEBTLOAN_START_MONTH = of("debtloan_start_month", Integer.class);
	
	public static final TypedKey<Integer> DEBTMORTGAGE_ID = of("debtmortgage_id", Integer.class);
	public static final TypedKey<String> DEBTMORTGAGE_NAME = of("debtmortgage_name", String.class);
	public static final TypedKey<BigDecimal> DEBTMORTGAGE_PURCHASE_PRICE = of("debtmortgage_purchase_price", BigDecimal.class);
	public static final TypedKey<BigDecimal> DEBTMORTGAGE_DOWNPAYMENT = of("debtmortgage_downpayment", BigDecimal.class);
	public static final TypedKey<BigDecimal> DEBTMORTGAGE_INTEREST_RATE = of("debtmortgage_interest_rate", BigDecimal.class);
	public static final TypedKey<Integer> DEBTMORTGAGE_TERM = of("debtmortgage_term", Integer.class);
	public static final TypedKey<BigDecimal> DEBTMORTGAGE_PROPERTY_INSURANCE = of("debtmortgage_property_insurance", BigDecimal.class);
	public static final TypedKey<BigDecimal> DEBTMORTGAGE_PROPERTY_TAX = of("debtmortgage_property_tax", BigDecimal.class);
	public static final TypedKey<BigDecimal> DEBTMORTGAGE_PMI = of("debtmortgage_pmi", BigDecimal.class);
	public static final TypedKey<Integer> DEBTMORTGAGE_START_YEAR = of("debtmortgage_start_year", Integer.class);	
	public static final TypedKey<Integer> DEBTMORTGAGE_START_MONTH = of("debtmortgage_start_month", Integer.class);	
	
	public static final TypedKey<TypedContainer> INCOMES  = of("incomes", TypedContainer.class);
	public static final TypedKey<TypedContainer> INVESTMENTS  = of("investments", TypedContainer.class);
	public static final TypedKey<TypedContainer> EXPENSES  = of("expenses", TypedContainer.class);
	public static final TypedKey<TypedContainer> DEBTS  = of("debts", TypedContainer.class);
	
	private static <T> TypedKey<T> of(String keyword, Class<T> clazz) {
		try {
	        Preconditions.checkStringConstructor(clazz);			
		} catch (NoSuchMethodException nsme) {
			nsme.printStackTrace();
			throw new RuntimeException(nsme); //TODO: Remove this to prevent Crash!
		}
		TypedKey<T> key = new TypedKey<T>(keyword, clazz);
		TypedKey<?> collision = keywordToTypedKey.put(keyword, key); 
		assert collision == null;
		return key;
	}
	
	private static boolean isNumeric(String keyword) {
		for(int i = 0; i < keyword.length(); i++) {
			if (!Character.isDigit(keyword.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private static TypedKey<TypedContainer> createNumericKey(String keyword) {
		assert isNumeric(keyword);
		return new TypedKey<TypedContainer>(keyword, TypedContainer.class);
	}
	
	public static TypedKey<?> getKey(String keyword) {
		TypedKey<?> key = keywordToTypedKey.get(keyword);
		if (key != null) {
			return key;
		}
		if (isNumeric(keyword)) {
			return createNumericKey(keyword);
		}
		assert false;
		return null;
	}
	
	public static TypedKey<TypedContainer> getNumericKey(int number) {
		return createNumericKey(String.valueOf(number));
	}
	
	private String keyword;
	private Class<T> clazz;
	private ValueFactory<T> factory;
	
	private TypedKey(String keyword, Class<T> clazz) {
		this.keyword = keyword;
		this.clazz = clazz;
		this.factory = new SimpleValueFactory<T>(keyword, clazz);
		//TODO: Use more factories to allow creation of income, investment etc objects.
	}
	
	public T cast(Object object) {
		return clazz.cast(object);
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public T parseValue(String token) throws ParseException {
		return factory.parseValue(token);
	}
	
	@Override
	public String toString() {
		return keyword;
	}
	
	private interface ValueFactory<T> {
		T parseValue(String token) throws ParseException;
		String getName();
	}
	
	public abstract class NamedValueFactory<T> implements ValueFactory<T> {
		private String name;
		
		protected NamedValueFactory(String name) {
			this.name = name;
			nameToFactory.put(name, this);
		}
		
		@Override
		public String getName() {
			return name;
		}
	}
	
	private class SimpleValueFactory<T> extends NamedValueFactory<T> {
		private Class<T> clazz;
		
		public SimpleValueFactory(String name, Class<T> clazz) {
			super(name);
			this.clazz = clazz;
		}
		
		@Override
		public T parseValue(String token) throws ParseException {
			try {
				return clazz.getConstructor(String.class).newInstance(token);
			} catch (NoSuchMethodException nsme) {
				throw new ParseException(nsme);
			} catch (IllegalArgumentException iae) {
				throw new ParseException(iae);			
			} catch (InstantiationException ie) {
				throw new ParseException(ie);			
			} catch (IllegalAccessException iaxe) {
				throw new ParseException(iaxe);			
			} catch (InvocationTargetException ite) {
				throw new ParseException(ite);			
			}
		}
	}
	
	private abstract class TypedContainerBasedFactory<T> extends NamedValueFactory<T> {
		protected TypedContainerBasedFactory(String name) {
			super(name);
		}
		
		@Override
		public T parseValue(String token) throws ParseException {
			return create(null); // TODO: new TypedContainer(iterator));
		}
		
		protected abstract T create(TypedContainer values);
	}
	
	private class Investment401kFactory extends TypedContainerBasedFactory<Investment401k> {
		Investment401kFactory(String name) {
			super(name);
		}
		
		@Override
		public Investment401k create(TypedContainer values) {
			throw new RuntimeException("Not implemented yet"); //TODO
		}
	}
}
