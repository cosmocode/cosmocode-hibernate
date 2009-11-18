package de.cosmocode.hibernate;

import java.util.Arrays;

import org.hibernate.criterion.MatchMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * A version of {@link MatchMode} which uses the property
 * value as a like expression.
 * 
 * @see ReverseIlikeExpression
 * 
 * @author Willi Schoenborn
 */
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
    
    /**
     * Renders the given column into a like statement using this {@link PropertyMatchMode}.
     * 
     * @param factory the {@link SessionFactoryImplementor} used to retrieve {@link SQLFunction}s
     * @param columnName the column to use in the like statement
     * @return the sql like statement
     */
    public abstract String toMatchString(SessionFactoryImplementor factory, String columnName);
    
    private static String render(SessionFactoryImplementor factory, Object... args) {
        final Dialect dialect = factory.getDialect();
        final SQLFunction concat = SQLFunction.class.cast(dialect.getFunctions().get("concat"));
        return concat.render(Arrays.asList(args), factory);
    }
    
}
