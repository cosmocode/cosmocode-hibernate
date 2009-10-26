package de.cosmocode.hibernate;

import java.util.Arrays;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.SessionFactoryImplementor;

public enum PropertyMatchMode {
    
    EXACT {
        
        @Override
        public String toMatchString(SessionFactoryImplementor factory, String columnName) {
            return columnName;
        }
        
    },
    START {
        
        @Override
        public String toMatchString(SessionFactoryImplementor factory, String columnName) {
            return render(factory, columnName, LIKE_SYMBOL);
        }
        
    },
    END {
        
        @Override
        public String toMatchString(SessionFactoryImplementor factory, String columnName) {
            return render(factory, LIKE_SYMBOL, columnName);
        }
        
    },
    ANYWHERE {
        
        @Override
        public String toMatchString(SessionFactoryImplementor factory, String columnName) {
            return render(factory,  LIKE_SYMBOL, columnName, LIKE_SYMBOL);
        }
        
    };
    
    private static final String LIKE_SYMBOL = "'%'";
    
    public abstract String toMatchString(SessionFactoryImplementor factory, String columnName);
    
    private static String render(SessionFactoryImplementor factory, Object... args) {
        final Dialect dialect = factory.getDialect();
        final SQLFunction concat = SQLFunction.class.cast(dialect.getFunctions().get("concat"));
        return concat.render(Arrays.asList(args), factory);
    }
    
}